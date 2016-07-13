package com.upload.yannadams.trailarticles;

import android.os.AsyncTask;

import com.upload.yannadams.trailarticles.NetUtil.MyUrl;
import com.upload.yannadams.trailarticles.NetUtil.NetManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Gagun on 2016/4/5.
 */
public class ghdNetHelper extends AsyncTask<NetManager.Param,Void,String> {

    private NetHelperListener listener;

    String action = null;

    public void setListener(NetHelperListener listener) {
        this.listener = listener;
    }

    public void setAction(String mAction) {
        action = mAction;
    }

    @Override
    protected String doInBackground(NetManager.Param... params) {

        String result = "";

        if(action!=null){
            switch (action){
                case "login":
                    try {
                        result = paramMissionUri(MyUrl.loginUrl,params);
                    } catch (IOException e) {
                        result = "Connect Fail!";
                        e.printStackTrace();
                    }
                    break;
                case "sign":
                    try {
                        result = paramMissionUri(MyUrl.signUrl,params);
                    } catch (IOException e) {
                        result = "Connect Fail!";
                        e.printStackTrace();
                    }
                    break;
                case "setProfile":
                    try {
                        result = paramMissionUri(MyUrl.setProfileUrl,params);
                    } catch (IOException e) {
                        result = "Connect Fail!";
                        e.printStackTrace();
                    }
                    break;
                case "getProfile":
                    try {
                        result = paramMissionUri(MyUrl.getProfileUrl,params);
                    } catch (IOException e) {
                        result = "Connect Fail!";
                        e.printStackTrace();
                    }
                    break;
            }
        }
       return result;
    }

    @Override
    protected void onPostExecute(String s) {
        if(s!=null){
            if(!s.equals("Connect Fail!")){
                try {
                    JSONObject json = new JSONObject(s);
                    String code = json.optString("code");
                    if(code.equals("1")||code.equals("2")){
                        listener.on_success(s);
                    }else if(code.equals("0")){
                        System.out.println("你失败了");
                        listener.on_fail(s);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                listener.on_error();
            }
        }
    }

    public interface NetHelperListener{
        void on_success(String result_json);
        void on_fail(String result_json);
        void on_error();
    }

    private String paramMission(NetManager.Param...params)throws IOException {
        return NetManager.postAsyn_String(MyUrl.hostUrl,params);
    }

    private String paramMissionUri(String uri,NetManager.Param...params)throws IOException {
        return NetManager.postAsyn_String(uri,params);
    }
}
