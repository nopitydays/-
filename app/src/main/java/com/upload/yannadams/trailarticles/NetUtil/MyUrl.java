package com.upload.yannadams.trailarticles.NetUtil;

import com.upload.yannadams.trailarticles.GlobalVariable;

/**
 * Created by Gagun on 2016/4/5.
 */
public class MyUrl {
    public final static String hostUrl = GlobalVariable.getInstance().getHost()+"sign";
    public final static String loginUrl = GlobalVariable.getInstance().getHost()+"login";
    public final static String signUrl = GlobalVariable.getInstance().getHost()+"sign";
    public final static String setProfileUrl=GlobalVariable.getInstance().getHost()+"setProfile";
    public final static String getProfileUrl=GlobalVariable.getInstance().getHost()+"getProfile";
}
