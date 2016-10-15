package com.scau.easyfarm.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by ChenHehong on 2016/10/15.
 */
public class ManualContentDetail extends Entity {

    @JSONField(name = "obj")
    private ManualContent manualContent;

    public ManualContent getManualContent() {
        return manualContent;
    }

    public void setManualContent(ManualContent manualContent) {
        this.manualContent = manualContent;
    }
}
