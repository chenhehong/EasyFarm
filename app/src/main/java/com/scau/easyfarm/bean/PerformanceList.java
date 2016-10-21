package com.scau.easyfarm.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenhehong on 2016/10/21.
 */
public class PerformanceList extends Entity implements ListEntity<Performance> {

    @JSONField(name = "obj")
    private ArrayList<Performance> performanceArrayList = new ArrayList<Performance>();

    public ArrayList<Performance> getPerformanceArrayList() {
        return performanceArrayList;
    }

    public void setPerformanceArrayList(ArrayList<Performance> performanceArrayList) {
        this.performanceArrayList = performanceArrayList;
    }

    @Override
    public List<Performance> getList() {
        return performanceArrayList;
    }
}
