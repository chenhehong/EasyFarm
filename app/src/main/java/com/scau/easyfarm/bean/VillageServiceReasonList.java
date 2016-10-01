package com.scau.easyfarm.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenHehong on 2016/10/1.
 */
public class VillageServiceReasonList  extends Entity implements ListEntity<VillageServiceReason> {

    @JSONField(name = "obj")
    private List<VillageServiceReason> villageServiceReasonsList = new ArrayList<VillageServiceReason>();

    public List<VillageServiceReason> getVillageServiceReasonsList() {
        return villageServiceReasonsList;
    }

    public void setVillageServiceReasonsList(List<VillageServiceReason> villageServiceReasonsList) {
        this.villageServiceReasonsList = villageServiceReasonsList;
    }

    @Override
    public List<VillageServiceReason> getList() {
        return villageServiceReasonsList;
    }
}
