package com.scau.easyfarm.bean;


import com.alibaba.fastjson.annotation.JSONField;

/**
 * 实体类
 */
@SuppressWarnings("serial")
public abstract class Entity extends Base {

    @JSONField(name = "autoID")
    protected int id;

    protected String cacheKey;

    public int getId() {
	    return id;
    }

    public void setId(int id) {
	    this.id = id;
    }

    public String getCacheKey() {
	return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
	this.cacheKey = cacheKey;
    }
}
