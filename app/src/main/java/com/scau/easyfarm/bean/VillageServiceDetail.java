package com.scau.easyfarm.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by ChenHehong on 2016/9/19.
 */
public class VillageServiceDetail extends Entity {

    @JSONField(name = "obj")
    private VillageService villageService = new VillageService();

    public VillageService getVillageService() {
        return villageService;
    }

    public void setVillageService(VillageService villageService) {
        this.villageService = villageService;
    }
}
