package com.upload.yannadams.trailarticles;

/**
 * Created by YannAdams on 2016/3/26.
 */
public class Essay {
    protected String[] ID;
    protected String[] PicUrl;
    protected String[] Title;
    protected int Count = 0;

    public Essay(int count)
    {
        this.Count = count;
        this.ID = new String[count];
        this.Title = new String[count];
        this.PicUrl = new String[count];
    }
}
