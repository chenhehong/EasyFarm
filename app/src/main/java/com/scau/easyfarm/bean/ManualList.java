package com.scau.easyfarm.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenHehong on 2016/9/2.
 */
public class ManualList extends Entity implements ListEntity<ManualContent> {

    public final static String CATALOG_VARIETY = "Variety";
    public final static String CATALOG_INDUSTRY = "Industry";
    public final static String CATALOG_TECHNOLOGY = "Technology";
    public final static String CATALOG_ACHIEVEMENT = "Achievement";

    private int manualCount;
    private int pagesize;
    @JSONField(name = "obj")
    private List<ManualContent> manualList = new ArrayList<ManualContent>();

    public int getManualCount() {
        return manualCount;
    }

    public void setManualCount(int manualCount) {
        this.manualCount = manualCount;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public List<ManualContent> getManualList() {
        return manualList;
    }

    public void setManualList(List<ManualContent> manualList) {
        this.manualList = manualList;
    }

    @Override
    public List<ManualContent> getList() {
        return manualList;
    }
}
