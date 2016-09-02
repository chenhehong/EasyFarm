package com.scau.easyfarm.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenHehong on 2016/9/2.
 */
public class VillageServiceList extends Entity implements ListEntity<VillageService> {

    public final static int ALL_VILLAGE_SERVICE = 0;
    public final static int PASS_VILAGE_SERVICE = 1;

    private int villageServiceCount;
    private int pagesize;
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
