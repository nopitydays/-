package com.upload.yannadams.trailarticles;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ArticleActivity extends AppCompatActivity {

    private int user_id;
    private int article_id;
    private String token;
    private TextView article_title;     //文章标题
    private TextView article_source;    //文章来源
    private TextView article_tag;       //文章类型
    private ImageView article_img;      //文章图片
    private TextView article_body;      //文章正文
    private TextView username;          //评论者用户名
    private TextView comment_body;      //评论内容
    private TextView comment_time;      //评论时间
    private Button collect_action;      //收藏按钮
    private Button upvote_action;       //点赞按钮
    private Button comment_action;      //评论按钮
    private int comment_up_num;         //评论点赞次数
    private int article_up_num;         //文章点赞次数
    private EditText sending_comment;     //即将发表的评论
    private EditText share_content;       //分享文章时的附加文字


    protected ImageLoader imageLoader = ImageLoader.getInstance();

    /************************评论部分变量**********************************/

    DisplayImageOptions options;        // DisplayImageOptions是用于设置图片显示的类
    DisplayImageOptions options1;
    CommentClass dc;
    ListView listView;
    protected ImageLoader imageLoader_commnet = ImageLoader.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.article_toolbar);
        // Title
        toolbar.setTitle("小道文章");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.action_share) {
                    share_content = new EditText(ArticleActivity.this);
                    share_content.setText("");
                    AlertDialog.Builder builder = new AlertDialog.Builder(ArticleActivity.this);
                    builder.setTitle("此刻的想法...");
                    builder.setIcon(android.R.drawable.ic_menu_share);
                    builder.setView(share_content);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setShare(user_id, article_id, token);
                        }
                    });
                    builder.setNegativeButton("取消", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                return true;
            }
        });


        initView();
        initData();

    }

    private void initView()
    {
        article_title=(TextView)findViewById(R.id.article_title);
        article_body=(TextView)findViewById(R.id.article_body);
        article_tag=(TextView)findViewById(R.id.article_tag);
        article_source=(TextView)findViewById(R.id.article_source);
        article_img=(ImageView)findViewById(R.id.article_img);
        collect_action=(Button)findViewById(R.id.button_collect);
        upvote_action=(Button)findViewById(R.id.button_upvote);
        comment_action=(Button)findViewById(R.id.button_comment);
        sending_comment=(EditText)findViewById(R.id.input_comment);

        //图片自适应
        int screenWidth = getScreenWidth(this);
        ViewGroup.LayoutParams lp = article_img.getLayoutParams();
        lp.width = screenWidth;
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        article_img.setLayoutParams(lp);
        article_img.setMaxWidth(screenWidth);
        article_img.setMaxHeight(screenWidth * 3);

        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(this);
        //Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(configuration);

        //创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration_comment = ImageLoaderConfiguration
                .createDefault(this);

        //Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(configuration_comment);

   /*     // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.head)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.head)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.head)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
                .build();                                   // 创建配置过得DisplayImageOption对象

        // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
        options1 = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.nopic)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.nopic)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.nopic)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
                .build();*/
    }

    private int getScreenWidth(ArticleActivity mainActivity) {
        WindowManager manager = (WindowManager) mainActivity
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }

    private void initData()
    {
        user_id=GlobalVariable.getInstance().getUserid();
        article_id=GlobalVariable.getInstance().getArticle_id();
        token=GlobalVariable.getInstance().getToken();
        getAritcle(user_id, article_id, token);
        initData_comment();
        upvote_action.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                upvoteArticle(user_id, article_id, token);
            }
        });
        collect_action.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectArticle(user_id, article_id, token);
            }
        });
        comment_action.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                setComment(user_id, article_id, token);
            }
        });

    }

    public void initData_comment(){
        NetHelper netHelper = new NetHelper(GlobalVariable.getInstance().getHost()+"getComment");
        netHelper.setParams("user_id", Integer.toString(user_id));
        netHelper.setParams("token", token);
        netHelper.setParams("article_id",Integer.toString(article_id));  //传递参数
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
                        String[] name = new String[count];
                        String[] CM_content = new String[count];
                        String[] CM_id = new String[count];
                        String[] CM_user_id = new String[count];
                        String[] CM_dayart = new String[count];
                        String[] CM_upvote_num = new String[count];

                        for (int i = 0; i < dataJson.length(); i++) {
                            JSONObject tempJson = dataJson.optJSONObject(i);

                            avatar[i] = tempJson.getString("avatar");
                            name[i] = tempJson.getString("name");
                            CM_content[i] = tempJson.getString("cm_content");
                            CM_id[i] = tempJson.getString("cm_id");
                            CM_dayart[i] = tempJson.getString("cm_dayart");
                            CM_upvote_num[i] = tempJson.getString("cm_upvote_num");
                            CM_user_id[i] = tempJson.getString("cm_user_id");

                        }
                        dc = new CommentClass(count);

                        dc.avatar = avatar;
                        dc.name = name;
                        dc.CM_content = CM_content;
                        dc.CM_id = CM_id;
                        dc.CM_dayart = CM_dayart;
                        dc.CM_user_id = CM_user_id;
                        dc.CM_upvote_num = CM_upvote_num;

                        listView = (ListView) findViewById(R.id.commentlist);
                        listView.setAdapter(new ItemAdapter());
                        setListViewHeightBasedOnChildren(listView);
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
        Toast.makeText(this, "连接数据出错，请重新尝试！" ,
                Toast.LENGTH_LONG).show();
    }
    private void upError(){
        Toast.makeText(this, "您已经赞过！" ,
                Toast.LENGTH_LONG).show();
    }
    private void collectError(){
        Toast.makeText(this, "您已经收藏过！" ,
                Toast.LENGTH_LONG).show();
    }
    private void collectSuccess(){
        Toast.makeText(this, "收藏成功！" ,
                Toast.LENGTH_LONG).show();
    }



    private  void getAritcle(final int user_id,final int article_id,final String token)
    {
        NetHelper netHelper = new NetHelper(GlobalVariable.getInstance().getHost()+"article");
        netHelper.setParams("token", token);  //传递参数
        netHelper.setParams("user_id", Integer.toString(user_id));  //传递参数
        netHelper.setParams("article_id", Integer.toString(article_id));  //传递参数
        netHelper.setResultListener(new NetHelper.ResultListener() {
            @Override
            public void getResult(String result) {
                //返回字符串json格式化
                JSONObject jsonObject;
                JSONArray dataJson;
                try {
                    jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    String article_title_value;
                    String article_tag_value;
                    String article_body_value;
                    String article_img_value;
                    //有内容则加载
                    if (code == 1) {
                        jsonObject = jsonObject.getJSONObject("data");
                        article_title.setText(jsonObject.getString("a_title"));
                        String tag;
                        if (jsonObject.getString("a_type").equals("1"))
                            tag="科技";
                        else if (jsonObject.getString("a_type").equals("2"))
                            tag="文学";
                        else if (jsonObject.getString("a_type").equals("3"))
                            tag="生活";
                        else if (jsonObject.getString("a_type").equals("4"))
                            tag="新闻";
                        else
                            tag="其他";
                        article_tag.setText(tag);
                        article_body.setText(jsonObject.getString("a_content"));
                        article_source.setText(jsonObject.getString("a_author"));
                        imageLoader.displayImage(jsonObject.getString("a_picture"), article_img);
                        //article_img.setText(tempJson.getString("a_picture"));
                        article_up_num=jsonObject.getInt("a_upvote_num");
                        upvote_action.setText("赞("+Integer.toString(article_up_num)+")");
                        collect_action.setText("收藏");
                    } else if (code == 2){
                        upError();
                    } else {
                        getError();
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

    private void upvoteArticle(final int user_id,final int article_id,final String token)
    {
        NetHelper netHelper = new NetHelper(GlobalVariable.getInstance().getHost()+"upvoteArticle");
        netHelper.setParams("token", token);  //传递参数
        netHelper.setParams("user_id", Integer.toString(user_id));  //传递参数
        netHelper.setParams("article_id", Integer.toString(article_id));  //传递参数
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
                        article_up_num++;
                        upvote_action.setText("赞(" + Integer.toString(article_up_num) + ")");
                    }else{
                        upError();
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

    private void collectArticle(final int user_id,final int article_id,final String token)
    {
        NetHelper netHelper = new NetHelper(GlobalVariable.getInstance().getHost()+"collectArticle");
        netHelper.setParams("token", token);  //传递参数
        netHelper.setParams("user_id", Integer.toString(user_id));  //传递参数
        netHelper.setParams("article_id", Integer.toString(article_id));  //传递参数
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
                        collectSuccess();
                    }else{
                        collectError();
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

    private void setComment(final int user_id,final int article_id,final String token)
    {
        NetHelper netHelper = new NetHelper(GlobalVariable.getInstance().getHost()+"setComment");
        netHelper.setParams("token", token);  //传递参数
        netHelper.setParams("user_id", Integer.toString(user_id));  //传递参数
        netHelper.setParams("article_id", Integer.toString(article_id));  //传递参数
        netHelper.setParams("content",sending_comment.getText().toString());  //传递参数
        netHelper.setResultListener(new NetHelper.ResultListener() {
            @Override
            public void getResult(String result) {
                //返回字符串json格式化
                JSONObject jsonObject;
                JSONArray dataJson;
                try {
                    jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    if (code == 2) {
                        sending_comment.setText("");
                        initData_comment();
                    }else{
                        getError();
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

    private void setShare(final int user_id,final int article_id,final String token)
    {
        NetHelper netHelper = new NetHelper(GlobalVariable.getInstance().getHost()+"setShare");
        netHelper.setParams("token", token);  //传递参数
        netHelper.setParams("user_id", Integer.toString(user_id));  //传递参数
        netHelper.setParams("article_id", Integer.toString(article_id));  //传递参数
        netHelper.setParams("content",share_content.getText().toString());  //传递参数
        netHelper.setResultListener(new NetHelper.ResultListener() {
            @Override
            public void getResult(String result) {
                //返回字符串json格式化
                JSONObject jsonObject;
                JSONArray dataJson;
                try {
                    jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    if (code == 2) {
                        Toast.makeText(ArticleActivity.this, "分享成功！" ,
                                Toast.LENGTH_LONG).show();
                    }else{
                        getError();
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

    private void upvoteComment(final int user_id,final int comment_id,final String token)
    {
        NetHelper netHelper = new NetHelper(GlobalVariable.getInstance().getHost()+"upvoteComment");
        netHelper.setParams("token", token);  //传递参数
        netHelper.setParams("user_id", Integer.toString(user_id));  //传递参数
        netHelper.setParams("comment_id", Integer.toString(comment_id));  //传递参数
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
                        initData_comment();
                    }else{
                        upError();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.article_toolbar_menu, menu);
        return true;
    }

    class ItemAdapter extends BaseAdapter {

        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

        private class ViewHolder {
            public ImageView userHead;
            public TextView userName;
            public TextView time;
            public TextView text;
            public ImageView upvote;
            public TextView upvote_num;
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
                view = getLayoutInflater().inflate(R.layout.comment_list_layout, parent, false);
                holder = new ViewHolder();

                holder.userHead = (ImageView) view.findViewById(R.id.userHead);
                try {
                    Field field = R.drawable.class.getField("h" + dc.avatar[position]);
                    holder.userHead.setImageResource((Integer) field.getInt(new R.drawable()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                holder.userHead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GlobalVariable.getInstance().setView_id(Integer.parseInt(dc.CM_user_id[position]));
                        Intent i = new Intent(ArticleActivity.this, PersonalInformationActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
                        startActivity(i);
                    }
                });
                holder.upvote = (ImageView) view.findViewById(R.id.upvote);
                holder.upvote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        upvoteComment(user_id, Integer.parseInt(dc.CM_id[position]), token);
                    }
                });

                holder.text = (TextView) view.findViewById(R.id.text);
                holder.time = (TextView) view.findViewById(R.id.time);
                holder.userName = (TextView) view.findViewById(R.id.userName);
                holder.upvote_num = (TextView) view.findViewById(R.id.upvote_num);

                view.setTag(holder);        // 给View添加一个格外的数据
            } else {
                holder = (ViewHolder) view.getTag(); // 把数据取出来
            }



            // TextView设置文本
            holder.text.setText(dc.CM_content[position]);
            holder.time.setText(dc.CM_dayart[position]);
            holder.userName.setText(dc.name[position]);
            holder.upvote_num.setText(dc.CM_upvote_num[position]);
            /**
             * 显示图片
             * 参数1：图片url
             * 参数2：显示图片的控件
             * 参数3：显示图片的设置
             * 参数4：监听器
             */
            imageLoader.displayImage(dc.avatar[position], holder.userHead, options, animateFirstListener);

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

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
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

