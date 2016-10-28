package com.scau.easyfarm.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by chenhehong on 2016/9/26.
 */
public class News extends Entity {

    private String title="";
    private String url="";
    @JSONField(name = "pageContent")
    private String content="";
    private int authorId;
    private String author="";
    @JSONField(name = "publishDate")
    private String pubDate="";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
}
