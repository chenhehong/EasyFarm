package com.scau.easyfarm.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenHehong on 2016/10/1.
 */
public class PerformanceTypeList extends Entity implements ListEntity<PerformanceType> {

    @JSONField(name = "obj")
    private List<PerformanceType> performanceTypeList = new ArrayList<PerformanceType>();

    public List<PerformanceType> getPerformanceTypeList() {
        return performanceTypeList;
    }

    public void setPerformanceTypeList(List<PerformanceType> performanceTypeList) {
        this.performanceTypeList = performanceTypeList;
    }

    @Override
    public List<PerformanceType> getList() {
        return performanceTypeList;
    }
}
