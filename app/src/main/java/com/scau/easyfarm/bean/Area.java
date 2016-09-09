package com.scau.easyfarm.bean;

/**
 * Created by chenhehong on 2016/9/9.
 */
public class Area extends Entity {

    private int jsonId;
    private String name;
    private boolean isParent;

    public int getJsonId() {
        return jsonId;
    }

    public void setJsonId(int jsonId) {
        this.jsonId = jsonId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isParent() {
        return isParent;
    }

    public void setIsParent(boolean isParent) {
        this.isParent = isParent;
    }
}
