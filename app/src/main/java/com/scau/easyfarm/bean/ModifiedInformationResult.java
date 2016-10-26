package com.scau.easyfarm.bean;


import com.alibaba.fastjson.annotation.JSONField;

/**
 * 操作结果实体类
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年10月14日 下午2:59:27
 * 
 */
@SuppressWarnings("serial")
public class ModifiedInformationResult extends Base {

    private Result result;

    private Notice notice;

    @JSONField(name = "obj")
    private User user;


    public Result getResult() {
	return result;
    }

    public void setResult(Result result) {
	this.result = result;
    }

    public Notice getNotice() {
	return notice;
    }

    public void setNotice(Notice notice) {
	this.notice = notice;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
