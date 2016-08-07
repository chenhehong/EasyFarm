package com.scau.easyfarm.bean;


/**
 * 用户实体类
 */
@SuppressWarnings("serial")
public class UserEntity extends Entity {

    private String loginName;
    private String password;

    private boolean isRememberMe;

    public boolean isRememberMe() {
        return isRememberMe;
    }

    public void setRememberMe(boolean isRememberMe) {
        this.isRememberMe = isRememberMe;
    }

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

}
