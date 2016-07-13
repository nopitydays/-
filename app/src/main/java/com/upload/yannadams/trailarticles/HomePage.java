package com.upload.yannadams.trailarticles;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

public class HomePage extends AppCompatActivity {

    private FragmentPagerAdapter mAdapter;      //适配器
    private ViewPager mViewPager;

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
        setContentView(R.layout.activity_home_page);
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

                Intent i = new Intent(HomePage.this, PersonalInformationActivity.class);

                GlobalVariable.getInstance().setView_id(GlobalVariable.getInstance().getUserid());
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
                startActivity(i);

            }
        });

        TextView tx = (TextView) findViewById(R.id.MyFri);

        tx.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(HomePage.this, MyFriends.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
                startActivity(i);
            }
        });

        TextView tx1 = (TextView) findViewById(R.id.MyClt);

        tx1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalVariable.getInstance().setView_id(GlobalVariable.getInstance().getUserid());
                Intent i = new Intent(HomePage.this, MyCollect.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
                startActivity(i);
            }
        });

        TextView username = (TextView) findViewById(R.id.username);
        username.setText(GlobalVariable.getInstance().getUsername());

        TextView tx2 = (TextView) findViewById(R.id.EditPer);

        tx2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                GlobalVariable.getInstance().setView_id(GlobalVariable.getInstance().getUserid());
                Intent i = new Intent(HomePage.this, EditPersonalInfoActivity.class);

                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
                startActivity(i);
            }
        });


        TextView tx3 = (TextView) findViewById(R.id.Exit);

        tx3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ExitDialog(HomePage.this).show();
            }
        });
        //页面列表
     //   mViewPager = (ViewPager) findViewById(R.id.id_vp);

        initDatas();    //初始化Fragment数据


        //底部导航栏切换

        TextView square = (TextView) findViewById(R.id.square);

        square.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomePage.this, MainActivity.class);

                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
                startActivity(i);
                finish();
            }
        });

        TextView dynamic = (TextView) findViewById(R.id.dynamic);

        dynamic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalVariable.getInstance().setSource("Homepage");
                GlobalVariable.getInstance().setView_id(GlobalVariable.getInstance().getUserid());
                Intent i = new Intent(HomePage.this, Dynamic.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
                startActivity(i);
                finish();
            }
        });

/*        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    //初始化数据
    private void initDatas()
    {


        NetHelper netHelper = new NetHelper(GlobalVariable.getInstance().getHost()+"today");
        netHelper.setParams("user_id", Integer.toString(GlobalVariable.getInstance().getUserid()));
        netHelper.setParams("token", GlobalVariable.getInstance().getToken());
       // netHelper.setParams("email", "574380444@qq.com");  //传递参数
        netHelper.setResultListener(new NetHelper.ResultListener() {

            @Override
            public void getResult(String result) {      //返回串

                final VpSimpleFragment fragment1 = new VpSimpleFragment();

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

                        String[] ID = new String[count];
                        String[] PIC = new String[count];
                        String[] Title = new String[count];

                        for (int i = 0; i < dataJson.length(); i++) {
                            JSONObject tempJson = dataJson.optJSONObject(i);

                            ID[i] = tempJson.getString("a_id");
                            Title[i] = tempJson.getString("a_title");
                            PIC[i] = tempJson.getString("a_picture");

                        }

                        fragment1.essay = new Essay(count);

                        fragment1.essay.PicUrl = PIC;
                        fragment1.essay.ID = ID;
                        fragment1.essay.Title = Title;

                    } else {
                        fragment1.essay = new Essay(0);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
                {
                    @Override
                    public int getCount()
                    {
                        return 1;
                    }

                    @Override
                    public Fragment getItem(int position)
                    {
                        return fragment1;
                    }
                };

                mViewPager = (ViewPager) findViewById(R.id.pager);

                mViewPager.setAdapter(mAdapter);       //只能在所有通讯结束后设置adapter，所以必须放在最后执行
                //设置关联的ViewPager

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            ExitDialog(HomePage.this).show();
            return true;
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
        NetHelper netHelper = new NetHelper(GlobalVariable.getInstance().getHost()+"logOut");
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
