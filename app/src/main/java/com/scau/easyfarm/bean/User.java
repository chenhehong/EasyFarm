package com.scau.easyfarm.bean;


import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 登录专家实体类
 */
@SuppressWarnings("serial")
public class User extends Entity {

    public static final String NORMALROLE = "注册用户";
    public static final String EXPERTROLE = "农技专家";

    public static final String[] genderArray = {"男","女"};
    public static final HashMap<Integer,String> genderIntMap = new HashMap<Integer,String>(){
        { put(MAN,"男");  put(FEMALE,"女"); }
    };
    public static final HashMap<String,Integer> genderStrMap = new HashMap<String,Integer>(){
        {put("男",MAN);put("女",FEMALE);}
    };

    public final static int MAN = 0;
    public final static int FEMALE=1;

    private String loginName="";
    private String password="";
    @JSONField(name = "imagePath")
    private String portrait="";
    private String realName="";
    @JSONField(name = "organizationName")
    private String organization="";
    private String phoneNumber="";
    private String techTitle="";
    private String techType="";
    private String techType2="";
    private String description="";
    private int sex;
    private int age=0;
    private String email="";
    private String address="";
    @JSONField(name = "roleNames")
    private String roleName="";
    private boolean isRememberMe=false;
    private boolean isServerLeader=false;//是否是服务事件的领队
    @JSONField(name = "resourceList")
    private ArrayList<String> moduleList = new ArrayList<>();

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

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
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

    public static HashMap<Integer, String> getGenderIntMap() {
        return genderIntMap;
    }

    public void setIsRememberMe(boolean isRememberMe) {
        this.isRememberMe = isRememberMe;
    }

    public ArrayList<String> getModuleList() {
        return moduleList;
    }

    public void setModuleList(ArrayList<String> moduleList) {
        this.moduleList = moduleList;
    }

    public String getTechTitle() {
        return techTitle;
    }

    public void setTechTitle(String techTitle) {
        this.techTitle = techTitle;
    }

    public String getTechType2() {
        return techType2;
    }

    public void setTechType2(String techType2) {
        this.techType2 = techType2;
    }
}
