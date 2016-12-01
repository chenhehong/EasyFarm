package com.scau.easyfarm.bean;

import java.util.HashMap;

/**
 * Created by chenhehong on 2016/12/1.
 */
public class WitTweet extends Entity {

    public static int TYPE_TWEET = 1;
    public static int TYPE_MANUAL = 2;
    public static final HashMap<Integer,String> typeIntHashMap = new HashMap<Integer,String>(){
        { put(TYPE_TWEET,"在线交流");  put(TYPE_MANUAL,"知识库");}};

    private int type;
    private String title;
    private int commentCount;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
