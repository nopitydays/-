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

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MyFriends extends AppCompatActivity {

    DisplayImageOptions options;        // DisplayImageOptions是用于设置图片显示的类

    MyFriendsClass myc;
    HorizontalListView listView;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friends);
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
      /*  options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.head)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.head)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.head)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
                .build();                                   // 创建配置过得DisplayImageOption对象
*/
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
        NetHelper netHelper = new NetHelper("http://10.4.20.136/tp5/public/index.php/getFriend");
        netHelper.setParams("user_id", Integer.toString(GlobalVariable.getInstance().getUserid()));
        netHelper.setParams("token", GlobalVariable.getInstance().getToken());

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

                        String[] avatar = new String[count];
                        String[] username = new String[count];
                        String[] id = new String[count];


                        for (int i = 0; i < dataJson.length(); i++) {
                            JSONObject tempJson = dataJson.optJSONObject(i);

                            avatar[i] = tempJson.getString("avatar");
                            username[i] = tempJson.getString("name");
                            id[i] = tempJson.getString("id");
                        }
                        myc = new MyFriendsClass(count);

                        myc.avatar = avatar;
                        myc.username = username;
                        myc.id = id;


                        listView = (HorizontalListView) findViewById(R.id.frilist);
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
            public ImageView friend;
            public TextView friName;
        }

        @Override
        public int getCount() {
            return myc.Count;
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
                view = getLayoutInflater().inflate(R.layout.fri_list, parent, false);
                holder = new ViewHolder();

                holder.friend = (ImageView) view.findViewById(R.id.friend);
                try {
                    Field field = R.drawable.class.getField("h" + myc.avatar[position]);
                    holder.friend.setImageResource((Integer) field.getInt(new R.drawable()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                holder.friend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MyFriends.this, PersonalInformationActivity.class);
                        intent.putExtra("user_id", myc.id[position]); // 没有user_id

                        GlobalVariable.getInstance().setView_id(Integer.parseInt(myc.id[position]));
                        startActivity(intent);
                    }
                });

                holder.friName = (TextView) view.findViewById(R.id.friName);

                view.setTag(holder);        // 给View添加一个格外的数据
            } else {
                holder = (ViewHolder) view.getTag(); // 把数据取出来
            }

            // TextView设置文本

            holder.friName.setText(myc.username[position]);

            /**
             * 显示图片
             * 参数1：图片url
             * 参数2：显示图片的控件
             * 参数3：显示图片的设置
             * 参数4：监听器
             */
            imageLoader.displayImage(myc.avatar[position], holder.friend, options, animateFirstListener);

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
