package com.upload.yannadams.trailarticles;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/***
 * Creat By ZhangCheng
 *
 *网络通信类
 *
 */


public class NetHelper {

    private static RequestQueue queue = MyApplication.netQueue;
    private ResultListener listener;

    private String url;
    private HashMap<String, String> map;

    public NetHelper(String url) {
        this.url = url;
        map = new HashMap<>();
    }

    public void setParams(String key, String value) {
        map.put(key, value);
    }

    public void doPost() {
   //     System.out.println(map);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                listener.getResult(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                listener.getError();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                return map;
            }
        };

        queue.add(request);
    }

    public interface ResultListener {
        void getResult(String result);

        void getError();
    }

    public void setResultListener(ResultListener listener) {
        this.listener = listener;
    }
}
