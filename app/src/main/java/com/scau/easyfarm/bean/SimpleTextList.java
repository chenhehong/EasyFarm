package com.scau.easyfarm.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenHehong on 2016/10/1.
 */
public class SimpleTextList extends Entity implements ListEntity<SimpleText>{

    @JSONField(name = "obj")
    private List<SimpleText> simpleTextList = new ArrayList<SimpleText>();

    public List<SimpleText> getSimpleTextList() {
        return simpleTextList;
    }

    public void setSimpleTextList(List<SimpleText> simpleTextList) {
        this.simpleTextList = simpleTextList;
    }


    @Override
    public List<SimpleText> getList() {
        return simpleTextList;
    }
}
