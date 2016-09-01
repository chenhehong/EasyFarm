package com.scau.easyfarm.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenhehong on 2016/8/31.
 */
public class ManualCategoryList extends Entity implements ListEntity<ManualCategory>{

    private int manualCategoryCount;
    private List<ManualCategory> manualCategoryList= new ArrayList<ManualCategory>();

    public int getManualCategoryCount() {
        return manualCategoryCount;
    }

    public void setManualCategoryCount(int manualCategoryCount) {
        this.manualCategoryCount = manualCategoryCount;
    }

    public List<ManualCategory> getManualCategoryList() {
        return manualCategoryList;
    }

    public void setManualCategoryList(List<ManualCategory> manualCategoryList) {
        this.manualCategoryList = manualCategoryList;
    }

    @Override
    public List<ManualCategory> getList() {
        return manualCategoryList;
    }
}
