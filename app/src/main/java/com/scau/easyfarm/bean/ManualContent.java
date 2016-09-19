package com.scau.easyfarm.bean;

/**
 * Created by ChenHehong on 2016/9/19.
 */
public class ManualContent extends Entity {

    private Long manualCategoryID;
    private String categoryCode;
    private String content;
    private String title;

    public Long getManualCategoryID() {
        return manualCategoryID;
    }

    public void setManualCategoryID(Long manualCategoryID) {
        this.manualCategoryID = manualCategoryID;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
