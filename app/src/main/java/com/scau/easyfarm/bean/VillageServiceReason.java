package com.scau.easyfarm.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by ChenHehong on 2016/10/1.
 */
public class VillageServiceReason extends Entity {

    @JSONField(name = "text")
    private String name = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
