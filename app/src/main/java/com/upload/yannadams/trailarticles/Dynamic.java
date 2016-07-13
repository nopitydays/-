package com.upload.yannadams.trailarticles;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Dynamic extends AppCompatActivity {

    DisplayImageOptions options;        // DisplayImageOptions是用于设置图片显示的类
    DisplayImageOptions options1;

    DynamicClass dc;
    ListView listView;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    protected void onResume() {
        super.onResume();
        initDatas();
        RoundImageView riv = (RoundImageView) findViewById(R.id.head);
        try {
            Field field = R.drawable.class.getField("h" + GlobalVariable.getInstance().getHeadid());
            riv.setImageResource((Integer) field.getInt(new R.drawable()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic);
        //绑定导航栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //侧边导航栏
        DrawerLayout mDrawerLayout;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);       //drawer--样式

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,  R.string.drawer_open,
                R.string.drawer_close);

        mDrawerToggle.syncState();

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        RoundImageView ri = (RoundImageView) findViewById(R.id.head);
        try {
            Field field = R.drawable.class.getField("h" + GlobalVariable.getInstance().getHeadid());
            ri.setImageResource((Integer) field.getInt(new R.drawable()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        ri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Dynamic.this, PersonalInformationActivity.class);

                GlobalVariable.getInstance().setView_id(GlobalVariable.getInstance().getUserid());
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
                startActivity(i);
            }
        });

        TextView username = (TextView) findViewById(R.id.username);
        username.setText(GlobalVariable.getInstance().getUsername());
        TextView tx = (TextView) findViewById(R.id.MyFri);

        tx.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(Dynamic.this, MyFriends.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
                startActivity(i);
            }
        });

        TextView tx1 = (TextView) findViewById(R.id.MyCol);

        tx1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalVariable.getInstance().setView_id(GlobalVariable.getInstance().getUserid());
                Intent i = new Intent(Dynamic.this, MyCollect.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
                startActivity(i);
            }
        });
        TextView tx2 = (TextView) findViewById(R.id.EditPer);

        tx2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalVariable.getInstance().setView_id(GlobalVariable.getInstance().getUserid());
                Intent i = new Intent(Dynamic.this, EditPersonalInfoActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
                startActivity(i);
            }
        });

        TextView tx3 = (TextView) findViewById(R.id.Exit);

        tx3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ExitDialog(Dynamic.this).show();
            }
        });


        initDatas();    //初始化Fragment数据

        //创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(this);

        //Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(configuration);

   /*     // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.head)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.head)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.head)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
                .build();    */                               // 创建配置过得DisplayImageOption对象

        // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
 /*       options1 = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.nopic)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.nopic)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.nopic)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
                .build();   */                                // 创建配置过得DisplayImageOption对象


        //底部导航栏切换
        TextView home = (TextView) findViewById(R.id.home);

        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dynamic.this, HomePage.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
                startActivity(i);
                finish();
            }
        });

        TextView square = (TextView) findViewById(R.id.square);

        square.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dynamic.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
                startActivity(i);
                finish();
            }
        });

        //发布动态
        RoundImageView riv = (RoundImageView) findViewById(R.id.addDy);

        riv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.dyWord);
                String str = editText.getText().toString();
                addMyDy(str);
            }
        });
    }

    public void addMyDy(String str){
        if(str == "" || str==null || str.length()<1){
            Toast.makeText(this, "动态内容为空，请重新填写！",
                    Toast.LENGTH_LONG).show();
        }else {
            NetHelper netHelper = new NetHelper(GlobalVariable.getInstance().getHost()+"setShare");
            netHelper.setParams("user_id", Integer.toString(GlobalVariable.getInstance().getUserid()));
            netHelper.setParams("token", GlobalVariable.getInstance().getToken());
            netHelper.setParams("content", str);  //传递参数
            netHelper.setParams("article_id", "0");
            netHelper.setResultListener(new NetHelper.ResultListener() {
                @Override
                public void getResult(String result) {      //返回串

                    //返回字符串json格式化
                    JSONObject jsonObject;

                    try {
                        jsonObject = new JSONObject(result);
                        int code = jsonObject.getInt("code");
                        //有内容则加载到Fragment中
                        dyOK(code);

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
    }

    public void dyOK(int t){
        if(t == 1 || t == 2){
            Toast.makeText(this, "动态发布成功！",
                    Toast.LENGTH_LONG).show();
            initDatas();
        }
        else {
            Toast.makeText(this, "动态发布失败，请重新尝试！",
                    Toast.LENGTH_LONG).show();
        }
    }


    public void initDatas(){
        NetHelper netHelper = new NetHelper("http://10.4.20.136/tp5/public/index.php/getShare");
        netHelper.setParams("user_id", Integer.toString(GlobalVariable.getInstance().getUserid()));
        netHelper.setParams("token", GlobalVariable.getInstance().getToken());
        netHelper.setParams("view_id", Integer.toString(GlobalVariable.getInstance().getView_id()));  //传递参数
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
                        String[] S_content = new String[count];
                        String[] A_id = new String[count];
                        String[] A_title = new String[count];
                        String[] A_picture = new String[count];
                        String[] S_dayart = new String[count];
                        String[] id = new String[count];
                        GlobalVariable.getInstance().setFlag(new int[count]);

                        for (int i = 0; i < dataJson.length(); i++) {
                            JSONObject tempJson = dataJson.optJSONObject(i);

                            avatar[i] = tempJson.getString("avatar");
                            username[i] = tempJson.getString("name");
                            S_content[i] = tempJson.getString("s_content");
                            A_id[i] = tempJson.getString("a_id");
                            A_title[i] = tempJson.getString("a_title");
                            A_picture[i] = tempJson.getString("a_picture");
                            S_dayart[i] = tempJson.getString("s_dayart");
                            id[i] = tempJson.getString("id");
                            GlobalVariable.getInstance().setFlag_single(i,1);
                        }
                        dc = new DynamicClass(count);

                        dc.avatar = avatar;
                        dc.username = username;
                        dc.S_content = S_content;
                        dc.A_id = A_id;
                        dc.A_title = A_title;
                        dc.A_picture = A_picture;
                        dc.S_dayart = S_dayart;
                        dc.id = id;

                        listView = (ListView) findViewById(R.id.dylist);
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
            public TextView time;
            public TextView text;
            public ImageView image;
            public TextView word;
        }

        @Override
        public int getCount() {
            return dc.Count;
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
                view = getLayoutInflater().inflate(R.layout.dynamic_list, parent, false);
                holder = new ViewHolder();

                holder.friend = (ImageView) view.findViewById(R.id.friend);
                try {
                    Field field = R.drawable.class.getField("h" + dc.avatar[position]);
                    holder.friend.setImageResource((Integer) field.getInt(new R.drawable()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                holder.friend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Dynamic.this, PersonalInformationActivity.class);
                        intent.putExtra("user_id", dc.id[position]); // 没有user_id

                        GlobalVariable.getInstance().setView_id(Integer.parseInt(dc.id[position]));
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
                        startActivity(intent);
                    }
                });

                holder.text = (TextView) view.findViewById(R.id.text);
                holder.text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Dynamic.this, ArticleActivity.class);  //文章界面

                        GlobalVariable.getInstance().setArticle_id(Integer.parseInt(dc.A_id[position]));
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
                        startActivity(intent);
                    }
                });

                holder.time = (TextView) view.findViewById(R.id.time);
                holder.friName = (TextView) view.findViewById(R.id.friName);
                holder.image = (ImageView) view.findViewById(R.id.image);
                holder.word = (TextView) view.findViewById(R.id.word);
                view.setTag(holder);        // 给View添加一个格外的数据
            } else {
                holder = (ViewHolder) view.getTag(); // 把数据取出来
            }



            // TextView设置文本

            if(dc.A_picture[position].equals("null")||dc.A_picture[position].equals("")||dc.A_picture[position].equals("NULL"))
            {
                GlobalVariable.getInstance().setFlag_single(position, 0);
                ViewGroup.LayoutParams para;
                para = holder.image.getLayoutParams();
                para.height = 0;
                para.width = 0;
                holder.image.setLayoutParams(para);
                holder.text.setText("");
                holder.text.setPadding(0,0,0,0);
            }
            else if (GlobalVariable.getInstance().getFlag_single(position)==1){
                ViewGroup.LayoutParams para;
                para = holder.image.getLayoutParams();
                para.height = 119;
                para.width = 180;
                holder.image.setLayoutParams(para);
                imageLoader.displayImage(dc.A_picture[position], holder.image, options1, animateFirstListener);
                holder.text.setText("分享：" + dc.A_title[position]);

                holder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Dynamic.this, ArticleActivity.class);  //文章界面
                        intent.putExtra("a_id", dc.A_id[position]); // 没有user_id

                        GlobalVariable.getInstance().setArticle_id(Integer.parseInt(dc.A_id[position]));
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
                        startActivity(intent);
                    }
                });

                holder.text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Dynamic.this, ArticleActivity.class);  //文章界面
                        intent.putExtra("a_id", dc.A_id[position]); // 没有user_id

                        GlobalVariable.getInstance().setArticle_id(Integer.parseInt(dc.A_id[position]));
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
                        startActivity(intent);
                    }
                });
            }
            holder.time.setText(dc.S_dayart[position]);
            holder.friName.setText(dc.username[position]);
            holder.word.setText(dc.S_content[position]);

            holder.friend = (ImageView) view.findViewById(R.id.friend);
            try {
                Field field = R.drawable.class.getField("h" + dc.avatar[position]);
                holder.friend.setImageResource((Integer) field.getInt(new R.drawable()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.friend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Dynamic.this, PersonalInformationActivity.class);
                    intent.putExtra("user_id", dc.id[position]); // 没有user_id

                    GlobalVariable.getInstance().setView_id(Integer.parseInt(dc.id[position]));
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
                    startActivity(intent);
                }
            });
            /**
             * 显示图片
             * 参数1：图片url
             * 参数2：显示图片的控件
             * 参数3：显示图片的设置
             * 参数4：监听器
             */
            imageLoader.displayImage(dc.A_picture[position], holder.image, options1, animateFirstListener);

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

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (GlobalVariable.getInstance().getSource().equals("personal"))
            {
                Intent intent = new Intent(Dynamic.this, PersonalInformationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
                startActivity(intent);
            }
            else {
                ExitDialog(Dynamic.this).show();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    private Dialog ExitDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //builder.setTitle("弹出对话框");
        builder.setMessage("确定要退出吗？");
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //操作代码
                        logOut();
                        finish();
                    }
                });
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
        return builder.create();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    private void logOut()
    {
        NetHelper netHelper = new NetHelper("http://10.4.20.136/tp5/public/index.php/logOut");
        netHelper.setParams("token", GlobalVariable.getInstance().getToken());  //传递参数
        netHelper.setParams("user_id", Integer.toString(GlobalVariable.getInstance().getUserid()));  //传递参数
        netHelper.setResultListener(new NetHelper.ResultListener() {
            @Override
            public void getResult(String result) {
                //返回字符串json格式化
                JSONObject jsonObject;
                JSONArray dataJson;
                try {
                    jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        getError();
                    } else if (code == 2){
                    }else{
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
}
