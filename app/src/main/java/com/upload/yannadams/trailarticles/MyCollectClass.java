package com.upload.yannadams.trailarticles;

/**
 * Created by YannAdams on 2016/5/2.
 */
public class MyCollectClass {
    protected String[] picture;
    protected String[] title;
    protected String[] id;
    protected int Count = 0;

    public MyCollectClass(int count)
    {
        this.Count = count;
        this.picture = new String[count];
        this.title = new String[count];
        this.id = new String[count];
    }
}
