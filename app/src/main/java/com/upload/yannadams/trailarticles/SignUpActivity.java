package com.upload.yannadams.trailarticles;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.upload.yannadams.trailarticles.NetUtil.NetManager;
import com.upload.yannadams.trailarticles.NetUtil.ghdNetHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignUpActivity extends AppCompatActivity {
    private Button Sign_Up_Button;
    private EditText username,password,email;
    private ghdNetHelper mGhdNetHelper;
    private ghdNetHelper.NetHelperListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();//点击返回上一级
                break;
        }
        return true;
    }
    private void init(){
        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("注册");
        toolbar.setNavigationIcon(R.drawable.icon__home_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        username=(EditText)findViewById(R.id.username_edittext);
        password=(EditText)findViewById(R.id.password_edittext);
        email=(EditText)findViewById(R.id.email_text);
        Sign_Up_Button=(Button)findViewById(R.id.signup_button);

        mListener = new ghdNetHelper.NetHelperListener() {
            @Override
            public void on_success(String result_json) {
                try {
                    JSONObject json = new JSONObject(result_json);
                    String msg = json.optString("msg");
                    System.out.println(msg);
                    String code=json.optString("code");
                    System.out.println(code);
                    String data=json.optString("data");
                    System.out.println(data);
                    if(code.equals("2")){
                        Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"注册失败",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void on_fail(String result_json) {
                try {
                    JSONObject json = new JSONObject(result_json);
                    String msg = json.optString("msg");
                    if (msg.equals("Wrong email address format"))
                        Toast.makeText(getApplicationContext(),"注册失败，邮箱格式错误",Toast.LENGTH_SHORT).show();
                    else if (msg.equals("user exists"))
                        Toast.makeText(getApplicationContext(),"注册失败，用户名已存在",Toast.LENGTH_SHORT).show();

                    System.out.println(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void on_error() {

            }
        };

        Sign_Up_Button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getSignUp();
            }
        });
    }

    public void getSignUp(){
        String user_name=username.getText().toString();
        String pass_word=password.getText().toString();
        String s_email=email.getText().toString();
        String fpswd=encipher(user_name,pass_word);
        mGhdNetHelper =new ghdNetHelper();
        mGhdNetHelper.setAction("sign");
        mGhdNetHelper.setListener(mListener);
        mGhdNetHelper.execute(NetManager.getParam("username",user_name)
        ,NetManager.getParam("email",s_email)
        ,NetManager.getParam("password",fpswd));
    }
    private String encipher(String user_name,String pass_word){
        byte[] iuser= new byte[0];
        try {
            byte[] ipswd=(user_name+pass_word).getBytes("UTF-8");
            MessageDigest md=MessageDigest.getInstance("MD5");
            byte[] pswd=md.digest(ipswd);
            char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符
            int k = 0; // 表示转换结果中对应的字符位置
            for (int i = 0; i < 16; i++) { // 从第一个字节开始，对每一个字节,转换成 16 进制字符的转换
                byte byte0 = pswd[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移
                str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
            }
            String fpswd=new String(str);
            return fpswd;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}
