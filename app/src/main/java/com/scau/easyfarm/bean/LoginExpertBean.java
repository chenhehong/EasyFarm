package com.scau.easyfarm.bean;


/** 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年9月27日 下午2:45:57 
 * 
 */

@SuppressWarnings("serial")
public class LoginExpertBean extends Entity {
	
	private Result result;

	private Expert expert;

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public Expert getExpert() {
		return expert;
	}

	public void setExpert(Expert expert) {
		this.expert = expert;
	}

}