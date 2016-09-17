package com.scau.easyfarm.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 我的资料实体类
 * 
 */

@SuppressWarnings("serial")
public class MyInformation extends Base {

	Result result;

	@JSONField(name = "obj")
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}
}
