package com.scau.easyfarm.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenhehong on 2016/9/8.
 */
public class UserList extends Entity implements ListEntity<User>{

    private int userCount;
    @JSONField(name = "obj")
    private List<User> userList= new ArrayList<User>();

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public List<User> getList() {
        return userList;
    }
}
