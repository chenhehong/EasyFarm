package com.scau.easyfarm.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenHehong on 2016/9/2.
 */
public class VillageServiceList extends Entity implements ListEntity<VillageService> {

    public final static int ALL_VILLAGE_SERVICE = 0;
    public final static int PASS_VILAGE_SERVICE = 1;
    public final static int WAITING_VILAGE_SERVICE = 2;


    public final static int VILLAGE_SERVICE_ALL = 0;
    public final static int VILLAGE_SERVICE_PASS = 7;
    public final static int VILLAGE_SERVICE_REJECT = 8;
    public final static int VILLAGE_SERVICE_WAITING = 9;



    private int villageServiceCount;
    private int pagesize;
    @JSONField(name = "obj")
    private List<VillageService> villageServiceList = new ArrayList<VillageService>();

    public int getVillageServiceCount() {
        return villageServiceCount;
    }

    public void setVillageServiceCount(int villageServiceCount) {
        this.villageServiceCount = villageServiceCount;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public List<VillageService> getVillageServiceList() {
        return villageServiceList;
    }

    public void setVillageServiceList(List<VillageService> villageServiceList) {
        this.villageServiceList = villageServiceList;
    }

    @Override
    public List<VillageService> getList() {
        return  villageServiceList;
    }
}
