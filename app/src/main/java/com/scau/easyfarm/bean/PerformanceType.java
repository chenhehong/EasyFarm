package com.scau.easyfarm.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;

/**
 * Created by ChenHehong on 2016/10/1.
 */
public class PerformanceType extends Entity {

    @JSONField(name = "description")
    private String name = "";
    private String unit = "";
    @JSONField(name = "fileType")
    private ArrayList<PerformanceFileType> fileTypeArrayList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public ArrayList<PerformanceFileType> getFileTypeArrayList() {
        return fileTypeArrayList;
    }

    public void setFileTypeArrayList(ArrayList<PerformanceFileType> fileTypeArrayList) {
        this.fileTypeArrayList = fileTypeArrayList;
    }
}
