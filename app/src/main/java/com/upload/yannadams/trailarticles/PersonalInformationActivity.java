package com.upload.yannadams.trailarticles;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.upload.yannadams.trailarticles.NetUtil.NetManager;
import com.upload.yannadams.trailarticles.NetUtil.ghdNetHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;


public class PersonalInformationActivity extends AppCompatActivity {
    private TextView nickname;
    private TextView birthday;
    private TextView area;
    private TextView sex;
    private TextView blog;
    private TextView qq;
    private TextView email;
    private TextView signature;
    private TextView addFri;
    private ghdNetHelper mGhdNetHelper;
    private ghdNetHelper.NetHelperListener mListener;
    private ImageView headphotoimage;
    private RelativeLayout collection;
    private RelativeLayout activity;
    private String  head_id;
    private String view_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("个人信息");
        toolbar.setNavigationIcon(R.drawable.icon__home_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        init();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        nickname=(TextView)findViewById(R.id.nickname);
        birthday=(TextView)findViewById(R.id.textView3);
        area=(TextView)findViewById(R.id.textView4);
        sex=(TextView)findViewById(R.id.textview5);
        blog=(TextView)findViewById(R.id.textView6);
        qq=(TextView)findViewById(R.id.textView7);
        email=(TextView)findViewById(R.id.textview8);
        headphotoimage=(ImageView)findViewById(R.id.icon);
        signature=(TextView)findViewById(R.id.personal_include).findViewById(R.id.saying);
        collection=(RelativeLayout)findViewById(R.id.collectionr);
        activity=(RelativeLayout)findViewById(R.id.activityr);
        addFri=(TextView)findViewById(R.id.add_friend);


        int fri_id=GlobalVariable.getInstance().getView_id();
        int user_id=GlobalVariable.getInstance().getUserid();

        if (fri_id==user_id)
        {
            ViewGroup.LayoutParams para;
            para = addFri.getLayoutParams();
            para.height = 0;
            para.width = 0;
            addFri.setLayoutParams(para);
        }

        mListener = new ghdNetHelper.NetHelperListener() {
            @Override
            public void on_success(String result_json) {
                try {
                    System.out.println("看这里");
                    JSONObject json = new JSONObject(result_json);;
                    String data=json.optString("data");
                    JSONObject jdata=new JSONObject(data);
                    String fheadid=jdata.optString("avatar");
                    head_id = fheadid;
                    String fsex=jdata.optString("sex");
                    String fbirthday=jdata.optString("birthday");
                    String farea=jdata.optString("area");
                    String fsignature=jdata.optString("signature");
                    String femail=jdata.optString("email");
                    String fblog=jdata.optString("blog");
                    String fqq=jdata.optString("qq");
                    String fnickname=jdata.optString("nickname");
                    if (GlobalVariable.getInstance().getView_id()==GlobalVariable.getInstance().getUserid()) {
                        GlobalVariable.getInstance().setNickname(fnickname);
                        GlobalVariable.getInstance().setHeadid(Integer.parseInt(fheadid));
                        GlobalVariable.getInstance().setSignature(fsignature);
                        GlobalVariable.getInstance().setBirthday(fbirthday);
                        GlobalVariable.getInstance().setErea(farea);
                        GlobalVariable.getInstance().setSex(fsex);
                        GlobalVariable.getInstance().setBlog(fblog);
                        GlobalVariable.getInstance().setQq(fqq);
                        GlobalVariable.getInstance().setEmail(femail);
                    }
                    changeHeadPhoto();
                    nickname.setText(fnickname);
                    signature.setText(fsignature);
                    birthday.setText(fbirthday);
                    area.setText(farea);
                    sex.setText(fsex);
                    blog.setText(fblog);
                    qq.setText(fqq);
                    email.setText(femail);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void on_fail(String result_json) {
                try {
                    JSONObject json = new JSONObject(result_json);
                    String msg = json.optString("msg");
                    System.out.println(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void on_error() {

            }
        };

        getInformation();
        activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "状态", Toast.LENGTH_SHORT).show();
                GlobalVariable.getInstance().setSource("personal");
                Intent intent = new Intent(PersonalInformationActivity.this, Dynamic.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
                startActivity(intent);
            }
        });

        collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "收藏", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PersonalInformationActivity.this, MyCollect.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
                startActivity(intent);
            }
        });

        addFri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int fri_id=GlobalVariable.getInstance().getView_id();
                int user_id=GlobalVariable.getInstance().getUserid();
                String token = GlobalVariable.getInstance().getToken();
                if (fri_id != user_id)
                {
                    setFriend(user_id,fri_id,token);
                }
            }
        });

    }//init

    private void getInformation() {
        mGhdNetHelper = new ghdNetHelper();
        mGhdNetHelper.setAction("getProfile");
        mGhdNetHelper.setListener(mListener);
        mGhdNetHelper.execute(NetManager.getParam("token", GlobalVariable.getInstance().getToken())
                , NetManager.getParam("user_id", Integer.toString(GlobalVariable.getInstance().getUserid()))
                , NetManager.getParam("view_id", Integer.toString(GlobalVariable.getInstance().getView_id())));
        System.out.println(GlobalVariable.getInstance().getToken()+"+"+Integer.toString(GlobalVariable.getInstance().getUserid())+
                "+"+Integer.toString(GlobalVariable.getInstance().getView_id()));

    }
    private void changeHeadPhoto() {
        headphotoimage = (ImageView) findViewById(R.id.icon);
        try {
            Field field = R.drawable.class.getField("h" + head_id);
            headphotoimage.setImageResource((Integer) field.getInt(new R.drawable()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFriend(final int user_id,final int friend_id,final String token)
    {
        NetHelper netHelper = new NetHelper(GlobalVariable.getInstance().getHost()+"setFriend");
        netHelper.setParams("token", token);  //传递参数
        netHelper.setParams("user_id", Integer.toString(user_id));  //传递参数
        netHelper.setParams("friend_id", Integer.toString(friend_id));  //传递参数
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
                    } else if (code == 2) {
                        Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "您已添加过好友", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getError()
            {
                Toast.makeText(getApplicationContext(), "连接出错，请重试", Toast.LENGTH_SHORT).show();
            }
        });
        netHelper.doPost();
    }
}



