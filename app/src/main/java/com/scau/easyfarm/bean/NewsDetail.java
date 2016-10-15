package com.scau.easyfarm.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by chenhehong on 2016/9/26.
 */
public class NewsDetail extends Entity {

    @JSONField(name = "obj")
    private News news;

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }
}
