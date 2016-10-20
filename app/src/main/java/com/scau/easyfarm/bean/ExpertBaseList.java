package com.scau.easyfarm.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenhehong on 2016/10/20.
 */
public class ExpertBaseList extends Entity implements ListEntity<ExpertBase>{

    @JSONField(name = "obj")
    private ArrayList<ExpertBase> expertBaseArrayList = new ArrayList<ExpertBase>();

    public ArrayList<ExpertBase> getExpertBaseArrayList() {
        return expertBaseArrayList;
    }

    public void setExpertBaseArrayList(ArrayList<ExpertBase> expertBaseArrayList) {
        this.expertBaseArrayList = expertBaseArrayList;
    }

    @Override
    public List<ExpertBase> getList() {
        return null;
    }

}
