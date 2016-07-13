package com.upload.yannadams.trailarticles;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/***
 * Creat By ZhangCheng
 *
 * 全局类
 */

public class MyApplication extends Application {

    public static RequestQueue netQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        netQueue = Volley.newRequestQueue(MyApplication.this);
    }
}