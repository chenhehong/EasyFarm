package com.scau.easyfarm.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenhehong on 2016/10/25.
 */
public class PerformStatisticsList extends Entity implements ListEntity<PerformanceStatistics> {

    @JSONField(name = "obj")
    private ArrayList<PerformanceStatistics> performanceStatisticsArrayList;

    public ArrayList<PerformanceStatistics> getPerformanceStatisticsArrayList() {
        return performanceStatisticsArrayList;
    }

    public void setPerformanceStatisticsArrayList(ArrayList<PerformanceStatistics> performanceStatisticsArrayList) {
        this.performanceStatisticsArrayList = performanceStatisticsArrayList;
    }

    @Override
    public List<PerformanceStatistics> getList() {
        return performanceStatisticsArrayList;
    }
}
