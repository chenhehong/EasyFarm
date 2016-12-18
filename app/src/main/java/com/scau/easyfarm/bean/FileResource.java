package com.scau.easyfarm.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by chenhehong on 2016/10/12.
 */
public class FileResource extends Entity {

    @JSONField(name = "metaDescription")
    private String description="";
    @JSONField(name = "metaPath")
    private String path="";
    private int typeId;
    private String typeString="";

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeString() {
        return typeString;
    }

    public void setTypeString(String typeString) {
        this.typeString = typeString;
    }
}
