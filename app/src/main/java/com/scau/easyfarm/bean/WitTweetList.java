package com.scau.easyfarm.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenhehong on 2016/12/1.
 */
public class WitTweetList extends Entity implements ListEntity<WitTweet> {

    @JSONField(name = "obj")
    private ArrayList<WitTweet> witTweetList = new ArrayList<>();

    public ArrayList<WitTweet> getWitTweetList() {
        return witTweetList;
    }

    public void setWitTweetList(ArrayList<WitTweet> witTweetList) {
        this.witTweetList = witTweetList;
    }

    @Override
    public List<WitTweet> getList() {
        return witTweetList;
    }
}
