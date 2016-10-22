package com.scau.easyfarm.bean;

/**
 * Created by ChenHehong on 2016/10/22.
 */
public class PerformanceMemberWorkTime extends Entity{

    private int userId;
    private String userName;
    private float realWorkTime;

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

    public float getRealWorkTime() {
        return realWorkTime;
    }

    public void setRealWorkTime(float realWorkTime) {
        this.realWorkTime = realWorkTime;
    }
}
