package com.scau.easyfarm.bean;

/** 
 * 我的资料实体类
 * 
 */

@SuppressWarnings("serial")
public class MyInformation extends Base {
	
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
