package com.scau.easyfarm.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by ChenHehong on 2016/9/19.
 */
public class ExpertBaseDetail extends Entity {

    @JSONField(name = "obj")
    private ExpertBase expertBase;

    public ExpertBase getExpertBase() {
        return expertBase;
    }

    public void setExpertBase(ExpertBase expertBase) {
        this.expertBase = expertBase;
    }
}
