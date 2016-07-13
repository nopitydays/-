package com.upload.yannadams.trailarticles;

/**
 * Created by YannAdams on 2016/5/1.
 */
public class CommentClass {
    protected String[] avatar;
    protected String[] name;
    protected String[] CM_content;
    protected String[] CM_id;
    protected String[] CM_dayart;
    protected String[] CM_user_id;
    protected String[] CM_upvote_num;
    protected int Count = 0;

    public CommentClass(int count)
    {
        this.Count = count;
        String[] avatar = new String[count];
        String[] name = new String[count];
        String[] CM_content = new String[count];
        String[] CM_id = new String[count];
        String[] CM_dayart = new String[count];
        String[] CM_user_id = new String[count];
        String[] CM_upvote_num= new String[count];
    }
}
