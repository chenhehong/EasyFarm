package com.scau.easyfarm.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenHehong on 2016/9/2.
 */
public class VillageProofResourceList extends Entity implements ListEntity<VillageProofResource> {

    private int pagesize;
    @JSONField(name = "obj")
    private List<VillageProofResource> villageProofResourceList = new ArrayList<VillageProofResource>();

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public List<VillageProofResource> getVillageProofResourceList() {
        return villageProofResourceList;
    }

    public void setVillageProofResourceList(List<VillageProofResource> villageProofResourceList) {
        this.villageProofResourceList = villageProofResourceList;
    }

    @Override
    public List<VillageProofResource> getList() {
        return  villageProofResourceList;
    }
}
