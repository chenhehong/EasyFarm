package com.scau.easyfarm.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenhehong on 2016/10/25.
 */
public class ServiceStatisticsList extends Entity implements ListEntity<ServiceStatistics> {

    @JSONField(name = "obj")
    private ArrayList<ServiceStatistics> serviceStatisticsArrayList;

    public ArrayList<ServiceStatistics> getServiceStatisticsArrayList() {
        return serviceStatisticsArrayList;
    }

    public void setServiceStatisticsArrayList(ArrayList<ServiceStatistics> serviceStatisticsArrayList) {
        this.serviceStatisticsArrayList = serviceStatisticsArrayList;
    }

    @Override
    public List<ServiceStatistics> getList() {
        return serviceStatisticsArrayList;
    }
}
