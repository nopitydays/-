package com.upload.yannadams.trailarticles;

/**
 * Created by YannAdams on 2016/5/1.
 */
public class MyFriendsClass {
    protected String[] avatar;
    protected String[] username;
    protected String[] id;
    protected int Count = 0;

    public MyFriendsClass(int count)
    {
        this.Count = count;
        this.avatar = new String[count];
        this.username = new String[count];
        this.id = new String[count];
    }
}
