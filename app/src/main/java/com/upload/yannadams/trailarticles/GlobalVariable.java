package com.upload.yannadams.trailarticles;

import android.app.Application;

/**
 * Created by GaoHengdong on 2016/5/2.
 */
public class GlobalVariable extends Application {

    private static GlobalVariable instance = null;
    private int headid=0;//又名 avatar
    private String token="";
    private int userid=1;//又名 id
    private String username;

    private String birthday;
    private String erea;
    private String sex;
    private String blog;
    private String qq;
    private String email;
    private String nickname;
    private String signature;
    private String host="http://10.4.20.136/tp5/public/index.php/";
    private int[] flag;

    public String getHost() {
        return host;
    }

    private String source;  //判断到动态的来源

    private int view_id=1;
    private int article_id = 1;

    public static synchronized GlobalVariable getInstance() {
        if (null == instance) {
            instance = new GlobalVariable();
        }
        return instance;
    }

    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public int getHeadid() {
        return headid;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setHeadid(int mHeadid) {
        headid = mHeadid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String mToken) {
        token = mToken;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int mUserid) {
        userid = mUserid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String mUsername) {
        username = mUsername;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String mBirthday) {
        birthday = mBirthday;
    }

    public String getErea() {
        return erea;
    }

    public void setErea(String mErea) {
        erea = mErea;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String mSex) {
        sex = mSex;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String mBlog) {
        blog = mBlog;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String mQq) {
        qq = mQq;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String mEmail) {
        email = mEmail;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String mNickname) {
        nickname = mNickname;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String mSignature) {
        signature = mSignature;
    }

    public int getView_id() {
        return view_id;
    }

    public void setView_id(int mView_id) {
        view_id = mView_id;
    }

    public int getArticle_id() {
        return article_id;
    }

    public void setArticle_id(int article_id) {
        this.article_id = article_id;
    }

    public int[] getFlag() {
        return flag;
    }

    public void setFlag(int[] flag) {
        this.flag = flag;
    }

    public void setFlag_single(int i,int value) {
        this.flag[i] = value;
    }

    public int getFlag_single(int i) {
        return this.flag[i];
    }
}
