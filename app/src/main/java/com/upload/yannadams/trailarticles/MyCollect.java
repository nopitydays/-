package com.upload.yannadams.trailarticles;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MyCollect extends AppCompatActivity {
    DisplayImageOptions options;        // DisplayImageOptions是用于设置图片显示的类

    MyCollectClass mcc;
    ListView listView;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initDatas();    //初始化Fragment数据

        //创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(this);

        //Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(configuration);

        // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.head)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.head)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.head)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
                .build();                                   // 创建配置过得DisplayImageOption对象

/*        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    public void initDatas(){
        NetHelper netHelper = new NetHelper("http://10.4.20.136/tp5/public/index.php/getCollect");
        netHelper.setParams("user_id", Integer.toString(GlobalVariable.getInstance().getUserid()));
        netHelper.setParams("token", GlobalVariable.getInstance().getToken());
        netHelper.setParams("view_id", Integer.toString(GlobalVariable.getInstance().getView_id()));

        netHelper.setResultListener(new NetHelper.ResultListener() {

            @Override
            public void getResult(String result) {      //返回串

                //返回字符串json格式化
                JSONObject jsonObject, data;
                JSONArray dataJson;

                try {
                    jsonObject = new JSONObject(result);

                    int code = jsonObject.getInt("code");
                    data = jsonObject.getJSONObject("data");

                    //有内容则加载到Fragment中
                    if (code == 1) {

                        dataJson = data.getJSONArray("list");
                        int count = dataJson.length();

                        String[] a_picture = new String[count];
                        String[] a_title = new String[count];
                        String[] id = new String[count];


                        for (int i = 0; i < dataJson.length(); i++) {
                            JSONObject tempJson = dataJson.optJSONObject(i);

                            a_picture[i] = tempJson.getString("a_picture");
                            a_title[i] = tempJson.getString("a_title");
                            id[i] = tempJson.getString("a_id");
                        }
                        mcc = new MyCollectClass(count);

                        mcc.picture = a_picture;
                        mcc.title = a_title;
                        mcc.id = id;


                        listView = (ListView) findViewById(R.id.collect_list);
                        listView.setAdapter(new ItemAdapter());

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void getError() {
                error();
            }
        });

        netHelper.doPost();
    }

    //报错处理
    private void error(){
        Toast.makeText(this, "连接数据出错，请重新尝试！",
                Toast.LENGTH_LONG).show();
    }


    /**
     *
     * 自定义列表项适配器
     *
     */
    class ItemAdapter extends BaseAdapter {

        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

        private class ViewHolder {
            public ImageView clt_img;
            public TextView clt_tle;
        }

        @Override
        public int getCount() {
            return mcc.Count;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            final ViewHolder holder;
            if (convertView == null) {
                view = getLayoutInflater().inflate(R.layout.clt_list, parent, false);
                holder = new ViewHolder();

                holder.clt_img = (ImageView) view.findViewById(R.id.clt_img);

                holder.clt_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GlobalVariable.getInstance().setArticle_id(Integer.parseInt(mcc.id[position]));
                        Intent intent = new Intent(MyCollect.this, ArticleActivity.class);
                        //intent.putExtra("user_id", mcc.id[position]); // 没有user_id
                        startActivity(intent);
                        finish();
                    }
                });

                holder.clt_tle = (TextView) view.findViewById(R.id.clt_tle);

                view.setTag(holder);        // 给View添加一个格外的数据
            } else {
                holder = (ViewHolder) view.getTag(); // 把数据取出来
            }

            // TextView设置文本

            holder.clt_tle.setText(mcc.title[position]);

            /**
             * 显示图片
             * 参数1：图片url
             * 参数2：显示图片的控件
             * 参数3：显示图片的设置
             * 参数4：监听器
             */
            imageLoader.displayImage(mcc.picture[position], holder.clt_img, options, animateFirstListener);

            return view;
        }
    }
    /**
     * 图片加载第一次显示监听器
     * @author Administrator
     *
     */
    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                // 是否第一次显示
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    // 图片淡入效果
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
