package com.scau.easyfarm.bean;


import java.io.Serializable;

/**
 * 通知信息实体类
 */
@SuppressWarnings("serial")
public class Notice implements Serializable {

    public final static String UTF8 = "UTF-8";
    public final static String NODE_ROOT = "easyfarm";

    public final static int TYPE_ATME = 1;//@
    public final static int TYPE_MESSAGE = 2;//消息
    public final static int TYPE_COMMENT = 3;//评论

    private int atmeCount;//@个数

    private int msgCount;//消息个数

    private int commentCount;//评论个数

    public int getAtmeCount() {
	return atmeCount;
    }

    public void setAtmeCount(int atmeCount) {
	    this.atmeCount = atmeCount;
    }

    public int getMsgCount() {
	return msgCount;
    }

    public void setMsgCount(int msgCount) {
	this.msgCount = msgCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

}
