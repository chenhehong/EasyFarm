package com.scau.easyfarm.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by chenhehong on 2016/9/16.
 */
public class TweetDetail extends Entity {

    @JSONField(name = "obj")
    private Tweet tweet;

    public Tweet getTweet() {
        return tweet;
    }
    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }
}
