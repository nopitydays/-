package com.upload.yannadams.trailarticles;

/**
 * Created by YannAdams on 2016/5/1.
 */
public class DynamicClass {
    protected String[] avatar;
    protected String[] username;
    protected String[] S_content;
    protected String[] A_id;
    protected String[] A_title;
    protected String[] A_picture;
    protected String[] S_dayart;
    protected String[] id;
    protected int[] flag;
    protected int Count = 0;

    public DynamicClass(int count)
    {
        this.Count = count;
        this.avatar = new String[count];
        this.username = new String[count];
        this.S_content = new String[count];
        this.A_id = new String[count];
        this.A_title = new String[count];
        this.A_picture = new String[count];
        this.S_dayart = new String[count];
        this.id = new String[count];
    }
}
