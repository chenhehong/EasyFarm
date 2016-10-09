package com.scau.easyfarm.bean;

/**
 * Created by chenhehong on 2016/8/31.
 * 系统知识库 问答类别实体
 */
public class ManualCategory extends Entity{

//  知识库条目
    public static final String VARIETY = "Variety";
    public static final String INDUSTRY = "Industry";
    public static final String ACHIEVEMENT = "Achievement";
    public static final String TECHNOLOGY = "Technology";

    private int parentId;
    private boolean isParent;
    private String categoryName;
    private String categoryCode;
    private String cotegoryDescription;

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public boolean isParent() {
        return isParent;
    }

    public void setIsParent(boolean isParent) {
        this.isParent = isParent;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCotegoryDescription() {
        return cotegoryDescription;
    }

    public void setCotegoryDescription(String cotegoryDescription) {
        this.cotegoryDescription = cotegoryDescription;
    }
}
