package com.scau.easyfarm.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenhehong on 2016/9/9.
 */
public class AreaList extends Entity implements ListEntity<Area> {


    @JSONField(name = "obj")
    private ArrayList<Area> areaList = new ArrayList<Area>();
    private int areaAccount;

    public ArrayList<Area> getAreaList() {
        return areaList;
    }

    public void setAreaList(ArrayList<Area> areaList) {
        this.areaList = areaList;
    }

    public int getAreaAccount() {
        return areaAccount;
    }

    public void setAreaAccount(int areaAccount) {
        this.areaAccount = areaAccount;
    }

    @Override
    public List<Area> getList() {
        return areaList;
    }
}
