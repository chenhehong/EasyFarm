package com.scau.easyfarm.bean;


/**
 * 登录专家实体类
 */
@SuppressWarnings("serial")
public class Expert extends UserEntity {

    private String realName;
    private String department;
    private String phoneNumber;
    private String techType;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTechType() {
        return techType;
    }

    public void setTechType(String techType) {
        this.techType = techType;
    }
}
