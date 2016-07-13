package com.upload.yannadams.trailarticles.NetUtil;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;

/**
 * Created by Gagun on 2016/4/4.
 */
public class NetManager {
    private static NetManager myNetManager;
    private OkHttpClient okHttpClient;

    private NetManager(){
        okHttpClient = new OkHttpClient();
    }

    public static NetManager getInstance(){
        if(myNetManager==null){
            synchronized (NetManager.class){
                if(myNetManager==null){
                    myNetManager = new NetManager();
                }
            }
        }
        return myNetManager;
    }

    public static Response getAsyn(String url) throws IOException {
        return getInstance().getResponse(url);
    }

    public static String getAsyn_String(String url) throws IOException {
        return getInstance().getResponseString(url);
    }

    public static Response postAsyn(String url,Param...params) throws IOException {
        return getInstance().postResponse(url, params);
    }

    public static String postAsyn_String(String url,Param...params) throws IOException {
        return getInstance().postResponseString(url, params);
    }

    public static Response mutiltypeAsyn(String url,File[] files,String[] filekeys,Param...params) throws IOException {
        return getInstance().mutilTypePostResponse(url, files, filekeys, params);
    }

    public static String mutiltypeAsyn_String(String url,File[] files,String[] filekeys,Param...params) throws IOException {
        return getInstance().mutilTypePostResponseString(url,files,filekeys,params);
    }


    /**
     * 同步get请求
     * @param url
     * @return
     * @throws IOException
     */
    private Response getResponse(String url)throws IOException {
        final Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        return response;
    }

    private String getResponseString(String url)throws IOException {
        return getResponse(url).body().toString();
    }

    /**
     * 同步post请求
     * @param url
     * @param params 请求体
     * @return
     * @throws IOException
     */
    private Response postResponse(String url,Param...params)throws IOException {
        Request request = buildPostRequest(url, params);
        Response response = okHttpClient.newCall(request).execute();
        return response;
    }

    private String postResponseString(String url,Param...params)throws IOException {
        return postResponse(url,params).body().string();
    }

    /**
     * 同步文件上传
     * @param url
     * @param files
     * @param fileKeys
     * @param params
     * @return
     * @throws IOException
     */
    private Response mutilTypePostResponse(String url,File[] files,String[] fileKeys,Param...params)throws IOException {
        Request request = bulidMutiltypePostRequset(url,files,fileKeys,params);
        Response response = okHttpClient.newCall(request).execute();
        return response;
    }

    private String mutilTypePostResponseString(String url,File[] files,String[] fileKeys,Param...params)throws IOException {
        return mutilTypePostResponse(url,files,fileKeys,params).body().string();
    }

    private Request buildPostRequest(String url,Param[] params){
        if(params==null){
            params = new Param[0];
        }
        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
        formEncodingBuilder.add("method","post");
        for(Param param:params){
            formEncodingBuilder.add(param.key,param.value);
        }
        RequestBody requestBody = formEncodingBuilder.build();
        return new Request.Builder().url(url).post(requestBody).build();
    }

    private Request bulidMutiltypePostRequset(String url,File[] file,String[] filekeys,Param[] params){
        if(params==null){
            params = new Param[0];
        }
        MultipartBuilder multipartBuilder = new MultipartBuilder().type(MultipartBuilder.FORM);
        for(Param param:params){
            multipartBuilder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.key + "\""), RequestBody.create(null, param.value));
        }
        if(file!=null){
            RequestBody fileBody = null;
            for(int i=0;i<file.length;i++){
                File file1 = file[i];
                String filename = file1.getName();
                fileBody = RequestBody.create(MediaType.parse(filecontentType(filename)),file1);
                multipartBuilder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + filekeys[i] + "\"; filename=\""+filename+"\""),fileBody);
            }
        }
        RequestBody requestBody = multipartBuilder.build();
        return new Request.Builder().url(url).post(requestBody).build();
    }

    /**
     * 判断文件类型
     * @param path
     * @return
     */
    private String filecontentType(String path){
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if(contentTypeFor==null){
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    public static class Param{
        String key,value;
        Param(String key,String value){
            this.key = key;
            this.value = value;
        }
    }

    public static Param getParam(String key,String value){
        return new Param(key,value);
    }

}
