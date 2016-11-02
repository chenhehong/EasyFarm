package com.scau.easyfarm.bean;

import java.io.Serializable;

/**
 * 动态实体类
 * 
 */
public class Active extends Entity {

	private Long type;//消息类型
	private String typeName;//类型名
	private Long metaID;//资源id  例如评论和@是问题的id
	private Long personalID;//用户id
	private String personalName;//用户名
	private String content;//内容：已经封装好提示文本
	private Integer isNotice;//是否已读

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Long getMetaID() {
		return metaID;
	}

	public void setMetaID(Long metaID) {
		this.metaID = metaID;
	}

	public Long getPersonalID() {
		return personalID;
	}

	public void setPersonalID(Long personalID) {
		this.personalID = personalID;
	}

	public String getPersonalName() {
		return personalName;
	}

	public void setPersonalName(String personalName) {
		this.personalName = personalName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getIsNotice() {
		return isNotice;
	}

	public void setIsNotice(Integer isNotice) {
		this.isNotice = isNotice;
	}
}
