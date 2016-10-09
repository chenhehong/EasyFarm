package com.scau.easyfarm.bean;


import com.alibaba.fastjson.annotation.JSONField;

import java.util.HashMap;

/**
 * 登录专家实体类
 */
@SuppressWarnings("serial")
public class User extends Entity {

    public static final HashMap<Integer,String> genderIntMap = new HashMap<Integer,String>(){
        { put(1,"男");  put(0,"女"); }
    };

    private String loginName="";
    private String password="";
    private String realName="";
    private String organization="";
    private String phoneNumber="";
    private String techType="";
    private String description="";
    private int sex;
    private int age=0;
    private String email="";
    private String address="";
    @JSONField(name = "roleNames")
    private String roleName="";
    private boolean isRememberMe=false;
    private boolean isServerLeader=false;//是否是服务事件的领队
    @JSONField(name = "auditor")
    private boolean canAuditServer=false;//是否有审核服务申请的权限

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public boolean isRememberMe() {
         return isRememberMe;
     }

    public void setRememberMe(boolean isRememberMe) {
        this.isRememberMe = isRememberMe;
    }

    public boolean isServerLeader() {
        return isServerLeader;
    }

    public void setIsServerLeader(boolean isServerLeader) {
        this.isServerLeader = isServerLeader;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public boolean isCanAuditServer() {
        return canAuditServer;
    }

    public void setCanAuditServer(boolean canAuditServer) {
        this.canAuditServer = canAuditServer;
    }
}
