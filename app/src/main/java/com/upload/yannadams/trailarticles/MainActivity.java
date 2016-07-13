package com.upload.yannadams.trailarticles;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Fragment> mTabContents = new ArrayList<Fragment>();    //设置添加Fragment
    private FragmentPagerAdapter mAdapter;      //适配器
    private ViewPager mViewPager;
    //	private List<String> mDatas = Arrays.asList("短信1", "短信2", "短信3", "短信4",
//			"短信5", "短信6", "短信7", "短信8", "短信9");
    private List<String> mDatas = Arrays.asList("全部", "科技", "文学","生活","新闻","其他");     //栏目分类--标题


    private ViewPagerIndicator mIndicator;      //自定义标题栏样式和动画

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
        setContentView(R.layout.activity_main);

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

        RoundImageView riv = (RoundImageView) findViewById(R.id.head);
        try {
            Field field = R.drawable.class.getField("h" + GlobalVariable.getInstance().getHeadid());
            riv.setImageResource((Integer) field.getInt(new R.drawable()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        riv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, PersonalInformationActivity.class);

                GlobalVariable.getInstance().setView_id(GlobalVariable.getInstance().getUserid());

                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
                startActivity(i);
            }
        });

        TextView tx = (TextView) findViewById(R.id.MyFri);

        tx.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, MyFriends.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
                startActivity(i);
            }
        });

        TextView username = (TextView) findViewById(R.id.username);
        username.setText(GlobalVariable.getInstance().getUsername());

        TextView tx1 = (TextView) findViewById(R.id.MyCol);

        tx1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalVariable.getInstance().setView_id(GlobalVariable.getInstance().getUserid());
                Intent i = new Intent(MainActivity.this, MyCollect.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
                startActivity(i);
            }
        });

        TextView tx2 = (TextView) findViewById(R.id.EditPer);

        tx2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalVariable.getInstance().setView_id(GlobalVariable.getInstance().getUserid());
                Intent i = new Intent(MainActivity.this, EditPersonalInfoActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
                startActivity(i);
            }
        });

        TextView tx3 = (TextView) findViewById(R.id.Exit);

        tx3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ExitDialog(MainActivity.this).show();
            }
        });


        initView();     //初始化
        //最初定义fragment,防止点击后闪退

        initDatas();    //初始化Fragment数据

        //设置Tab上的标题
        mIndicator.setTabItemTitles(mDatas);


        //底部导航栏切换

        TextView home = (TextView) findViewById(R.id.home);

        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, HomePage.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
                startActivity(i);
                finish();
            }
        });

        TextView dynamic = (TextView) findViewById(R.id.dynamic);
        dynamic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalVariable.getInstance().setSource("MainActivity");
                GlobalVariable.getInstance().setView_id(GlobalVariable.getInstance().getUserid());
                Intent i = new Intent(MainActivity.this, Dynamic.class);
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

    //报错处理
    private void error(){
        Toast.makeText(this, "连接数据出错，请重新尝试！" ,
                Toast.LENGTH_LONG).show();
    }

    //根据类别type初始化每个Fragment
    private void getData(final int type){
        //POST方法进行http通讯
        NetHelper netHelper = new NetHelper(GlobalVariable.getInstance().getHost()+"plaza");
        netHelper.setParams("user_id", Integer.toString(GlobalVariable.getInstance().getUserid()));
        netHelper.setParams("token", GlobalVariable.getInstance().getToken());
        netHelper.setParams("type", Integer.toString(type));
        netHelper.setResultListener(new NetHelper.ResultListener() {

            @Override
            public void getResult(String result) {      //返回串

                VpSimpleFragment fragment1 = new VpSimpleFragment();

                //返回字符串json格式化
                JSONObject jsonObject;
                JSONArray dataJson;

                try {
                    jsonObject = new JSONObject(result);

                    int code = jsonObject.getInt("code");

                    //有内容则加载到Fragment中
                    if(code == 1) {

                        dataJson = jsonObject.getJSONArray("data");
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
                        mTabContents.set(type, fragment1);    //添加Fragment
                    }
                    else
                    {
                        fragment1.essay = new Essay(0);
                        mTabContents.set(type, fragment1);
                  //          mTabContents.add(new Fragment());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(type == 3)
                {
                    Handler handler = new Handler();
                    handler.postDelayed(mRunnable, 800);
                }
            }

            @Override
            public void getError() {
                error();
            }
        });

        netHelper.doPost();
    }

    private final Runnable mRunnable = new Runnable() {

        public void run() {
            mViewPager.setAdapter(mAdapter);       //只能在所有通讯结束后设置adapter，所以必须放在最后执行
            //设置关联的ViewPager
            mIndicator.setViewPager(mViewPager, 0);

        }
    };

    //初始化数据
    private void initDatas()
    {
        /*
        for (String data : mDatas)
        {
            VpSimpleFragment fragment ;
            fragment = new VpSimpleFragment();


            fragment.data = data;
            mTabContents.add(fragment);
        }
        */

        for(int i=0; i<mDatas.size(); i++)
        {
            VpSimpleFragment fragment1 = new VpSimpleFragment();
            mTabContents.add(fragment1);
        }

        for(int i=0; i<mDatas.size();i++)
            getData(i);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
        {
            @Override
            public int getCount()
            {
                return 6;
            }

            @Override
            public Fragment getItem(int position)
            {
                return mTabContents.get(position);
            }
        };
    }

    private void initView()
    {
        mViewPager = (ViewPager) findViewById(R.id.id_vp);
        mIndicator = (ViewPagerIndicator) findViewById(R.id.id_indicator);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

  /*  @Override
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
            ExitDialog(MainActivity.this).show();
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

    private void firstdo(){
        for(int i=0; i<mDatas.size(); i++)
        {
            Fragment fragment = new Fragment();
            mTabContents.add(fragment);
        }

        for(int i=0; i<mDatas.size();i++)
            getData(i);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
        {
            @Override
            public int getCount()
            {
                return 6;
            }

            @Override
            public Fragment getItem(int position)
            {
                return mTabContents.get(position);
            }
        };

        mViewPager.setAdapter(mAdapter);       //只能在所有通讯结束后设置adapter，所以必须放在最后执行
        //设置关联的ViewPager
        mIndicator.setViewPager(mViewPager, 0);
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
