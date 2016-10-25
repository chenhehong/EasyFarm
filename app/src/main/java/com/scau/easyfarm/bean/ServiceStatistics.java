package com.scau.easyfarm.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by chenhehong on 2016/10/25.
 */
public class ServiceStatistics extends Entity {

    @JSONField(name = "queryDate")
    private String month="";
    private String type="";
    private int typeId;
    @JSONField(name = "sum")
    private float sumWorkTime;
    private int organizationId;
    private String organizationName;
    private int userId;
    private String userName;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getSumWorkTime() {
        return sumWorkTime;
    }

    public void setSumWorkTime(float sumWorkTime) {
        this.sumWorkTime = sumWorkTime;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
}
