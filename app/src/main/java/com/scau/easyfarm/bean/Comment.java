package com.scau.easyfarm.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by chenhehong on 2016/9/7.
 */
public class Comment extends Entity{

//  所评论的评论的id，如果直接评论问题为0
    @JSONField(name = "commentID")
    private int commentedId;
    private String commentedName;
    @JSONField(name="personalID")
    private int commenterId;
    @JSONField(name = "commentName")
    private String comenterName;
    private String content;
    @JSONField(name = "createDate")
    private String commentDate;

    public int getCommentedId() {
        return commentedId;
    }

    public void setCommentedId(int commentedId) {
        this.commentedId = commentedId;
    }

    public String getCommentedName() {
        return commentedName;
    }

    public void setCommentedName(String commentedName) {
        this.commentedName = commentedName;
    }

    public int getCommenterId() {
        return commenterId;
    }

    public void setCommenterId(int commenterId) {
        this.commenterId = commenterId;
    }

    public String getComenterName() {
        return comenterName;
    }

    public void setComenterName(String comenterName) {
        this.comenterName = comenterName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }
}
