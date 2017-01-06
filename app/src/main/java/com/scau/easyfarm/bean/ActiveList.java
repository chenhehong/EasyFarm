package com.scau.easyfarm.bean;


import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态实体列表
 *
 */
@SuppressWarnings("serial")
public class ActiveList extends Entity implements ListEntity<Active> {

    @JSONField(name = "obj")
    private List<Active> activeList = new ArrayList<Active>();
    @JSONField(name = "ts")
    private String lastReadTime;

    public List<Active> getActiveList() {
        return activeList;
    }

    public void setActiveList(List<Active> activeList) {
        this.activeList = activeList;
    }

    public String getLastReadTime() {
        return lastReadTime;
    }

    public void setLastReadTime(String lastReadTime) {
        this.lastReadTime = lastReadTime;
    }

    @Override
    public List<Active> getList() {
	return activeList;
    }

}
