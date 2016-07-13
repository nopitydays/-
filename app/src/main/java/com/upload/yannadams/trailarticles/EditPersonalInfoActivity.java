package com.upload.yannadams.trailarticles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.upload.yannadams.trailarticles.NetUtil.NetManager;
import com.upload.yannadams.trailarticles.NetUtil.ghdNetHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

public class EditPersonalInfoActivity extends AppCompatActivity {
    private TextView headphoto;
    private ImageView headphotoimage;
    private ghdNetHelper mGhdNetHelper;
    private ghdNetHelper.NetHelperListener mListener;
    private ghdNetHelper mGetGhdNetHelper;
    private ghdNetHelper.NetHelperListener mGetListener;
    private EditText nickname;
    private EditText sex;
    private EditText birthday;
    private EditText erea;
    private EditText signature;
    private TextView email;
    private EditText blog;
    private EditText qq;
    private Button save;
    private String head_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("编辑个人资料");
        toolbar.setNavigationIcon(R.drawable.icon__home_back);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            init();
            GlobalVariable.getInstance().setHeadid(Integer.parseInt(head_id));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void init() {
        headphoto = (TextView) findViewById(R.id.editheadphoto);
        nickname=(EditText)findViewById(R.id.textView1);
        sex=(EditText)findViewById(R.id.textView2);
        birthday=(EditText)findViewById(R.id.textView3);
        erea=(EditText)findViewById(R.id.textView4);
        signature=(EditText)findViewById(R.id.textView5);
        email=(TextView)findViewById(R.id.textView6);
        blog=(EditText)findViewById(R.id.textView7);
        qq=(EditText)findViewById(R.id.textView8);
        save=(Button)findViewById(R.id.edit_save_button);

        headphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(EditPersonalInfoActivity.this, HeadPhotoActivity.class);
                startActivity(intent);
            }
        });
        changeHeadPhoto();
        mGetListener = new ghdNetHelper.NetHelperListener() {
            @Override
            public void on_success(String result_json) {
                try {
                    System.out.println("看这里");
                    JSONObject json = new JSONObject(result_json);;
                    String data=json.optString("data");
                    JSONObject jdata=new JSONObject(data);
                    String fheadid=jdata.optString("avatar");
                    head_id=fheadid;
                    String fsex=jdata.optString("sex");
                    String fbirthday=jdata.optString("birthday");
                    String farea=jdata.optString("area");
                    String fsignature=jdata.optString("signature");
                    String femail=jdata.optString("email");
                    String fblog=jdata.optString("blog");
                    String fqq=jdata.optString("qq");
                    String fnickname=jdata.optString("nickname");
                    GlobalVariable.getInstance().setNickname(fnickname);
                    GlobalVariable.getInstance().setHeadid(Integer.parseInt(fheadid));
                    GlobalVariable.getInstance().setSignature(fsignature);
                    GlobalVariable.getInstance().setBirthday(fbirthday);
                    GlobalVariable.getInstance().setErea(farea);
                    GlobalVariable.getInstance().setSex(fsex);
                    GlobalVariable.getInstance().setBlog(fblog);
                    GlobalVariable.getInstance().setQq(fqq);
                    GlobalVariable.getInstance().setEmail(femail);
                    changeHeadPhoto();
                    nickname.setText(fnickname);
                    signature.setText(fsignature);
                    birthday.setText(fbirthday);
                    erea.setText(farea);
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
        mListener = new ghdNetHelper.NetHelperListener() {
            @Override
            public void on_success(String result_json) {
                try {
                    Toast.makeText(EditPersonalInfoActivity.this, "保存成功！",
                            Toast.LENGTH_LONG).show();
                    JSONObject json = new JSONObject(result_json);
                    String msg = json.optString("msg");
                    System.out.println("成功看这里"+msg);
                    String code = json.optString("code");
                    System.out.println("成功看这里"+code);
                    String data=json.optString("data");
                    System.out.println("成功看这里"+data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void on_fail(String result_json) {
                try {
                    Toast.makeText(EditPersonalInfoActivity.this, "连接出错，请重试！" ,
                            Toast.LENGTH_LONG).show();
                    JSONObject json = new JSONObject(result_json);
                    String msg = json.optString("msg");
                    System.out.println("失败看这里"+msg);
                    String code = json.optString("code");
                    System.out.println("失败看这里"+code);
                    String data=json.optString("data");
                    System.out.println("失败看这里"+data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void on_error() {
                System.out.println("错误啦");

            }
        };
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSaved();
            }
        });
        getInformation();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        changeHeadPhoto();
    }

    private void changeHeadPhoto() {
        headphotoimage = (ImageView) findViewById(R.id.imageView);
        try {
            Field field = R.drawable.class.getField("h" + GlobalVariable.getInstance().getHeadid());
            headphotoimage.setImageResource((Integer) field.getInt(new R.drawable()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void getInformation() {
        mGetGhdNetHelper = new ghdNetHelper();
        mGetGhdNetHelper.setAction("getProfile");
        mGetGhdNetHelper.setListener(mGetListener);
        mGetGhdNetHelper.execute(NetManager.getParam("token", GlobalVariable.getInstance().getToken())
                , NetManager.getParam("user_id", Integer.toString(GlobalVariable.getInstance().getUserid()))
                , NetManager.getParam("view_id", Integer.toString(GlobalVariable.getInstance().getView_id())));
        System.out.println(GlobalVariable.getInstance().getToken()+"+"+Integer.toString(GlobalVariable.getInstance().getUserid())+
                "+"+Integer.toString(GlobalVariable.getInstance().getView_id()));

    }


    private void getSaved(){
        String fnickname=nickname.getText().toString();
        String fsex=sex.getText().toString();
        String fbirthday=birthday.getText().toString();
        String ferea=erea.getText().toString();
        String fsignature=signature.getText().toString();
        String fblog=blog.getText().toString();
        String fqq=qq.getText().toString();
        System.out.println("测试"+fnickname+"+"+fsex+"+"+fbirthday+"+"+ferea+"+"+fsignature+"+"+fblog+"+"+fqq+"+"+GlobalVariable.getInstance().getHeadid());
        GlobalVariable.getInstance().setNickname(fnickname);
        GlobalVariable.getInstance().setSex(fsex);
        GlobalVariable.getInstance().setBirthday(fbirthday);
        GlobalVariable.getInstance().setErea(ferea);
        GlobalVariable.getInstance().setSignature(fsignature);
        GlobalVariable.getInstance().setBlog(fblog);
        GlobalVariable.getInstance().setQq(fqq);
        mGhdNetHelper = new ghdNetHelper();
        mGhdNetHelper.setAction("setProfile");
        mGhdNetHelper.setListener(mListener);
        mGhdNetHelper.execute(NetManager.getParam("nickname", fnickname)
                , NetManager.getParam("sex",fsex)
                , NetManager.getParam("birthday",fbirthday)
                , NetManager.getParam("area",ferea)
                , NetManager.getParam("signature",fsignature)
                , NetManager.getParam("blog",fblog)
                , NetManager.getParam("qq",fqq)
                , NetManager.getParam("token",GlobalVariable.getInstance().getToken())
                , NetManager.getParam("user_id",Integer.toString(GlobalVariable.getInstance().getUserid()))
                , NetManager.getParam("head_id",Integer.toString(GlobalVariable.getInstance().getHeadid())));


    }

}

