package com.upload.yannadams.trailarticles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.upload.yannadams.trailarticles.NetUtil.NetManager;
import com.upload.yannadams.trailarticles.NetUtil.ghdNetHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LogInActivity extends AppCompatActivity {
    private Button login_button;
    private EditText username, password;
    private RelativeLayout signup_bar;
    private ghdNetHelper mGhdNetHelper;
    private ghdNetHelper.NetHelperListener mListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        init();
    }

    private void init(){

        username = (EditText) findViewById(R.id.username_edittext);
        password = (EditText) findViewById(R.id.password_edittext);
        login_button = (Button) findViewById(R.id.login_button);


        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLogin();
            }
        });
        signup_bar = (RelativeLayout) findViewById(R.id.signup_bar);
        signup_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LogInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        mListener = new ghdNetHelper.NetHelperListener() {
            @Override
            public void on_success(String result_json) {
                try {
                    JSONObject json = new JSONObject(result_json);
                    String code=json.optString("code");
                    String data=json.optString("data");
                    JSONObject jdata =new JSONObject(data);
                    String token=jdata.optString("token");
                    String name=jdata.optString("name");
                    String id=jdata.optString("id");
                    String avatar=jdata.optString("avatar");
                    GlobalVariable.getInstance().setHeadid(Integer.parseInt(avatar));
                    GlobalVariable.getInstance().setToken(token);
                    GlobalVariable.getInstance().setUserid(Integer.parseInt(id));
                    GlobalVariable.getInstance().setUsername(name);
                    if(code.equals("1")){
                        Toast.makeText(getApplicationContext(),"登陆成功",Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(LogInActivity.this, HomePage.class);

                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
                        startActivity(i);
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"登陆失败",Toast.LENGTH_SHORT).show();
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
                    System.out.println(msg);
                    Toast.makeText(getApplicationContext(),"登陆失败",Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void on_error() {

            }
        };
    }

    private void getLogin(){
        String user_name = username.getText().toString();
        String pass_word = password.getText().toString();
        String fpswd=encipher(user_name,pass_word);
        mGhdNetHelper = new ghdNetHelper();
        mGhdNetHelper.setAction("login");
        mGhdNetHelper.setListener(mListener);
        mGhdNetHelper.execute(NetManager.getParam("username",user_name)
                ,NetManager.getParam("password",fpswd));
    }

    private String encipher(String user_name,String pass_word){
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
