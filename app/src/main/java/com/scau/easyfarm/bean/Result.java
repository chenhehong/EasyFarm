package com.scau.easyfarm.bean;


import java.io.Serializable;

/**
 * 数据操作结果实体类
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
@SuppressWarnings("serial")
public class Result implements Serializable {

//  errorCode==0的时候表示服务器返回数据正常，其他表示错误
    private int errorCode=-1;
    private boolean success=false;
    private String errorMessage;

    public boolean OK() {
	    return success;
    }

    public int getErrorCode() {
	return errorCode;
    }

    public void setErrorCode(int errorCode) {
	this.errorCode = errorCode;
    }

    public String getErrorMessage() {
	return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
	this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
