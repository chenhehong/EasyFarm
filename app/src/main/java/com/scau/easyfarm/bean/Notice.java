package com.scau.easyfarm.bean;


import java.io.Serializable;

/**
 * 通知信息实体类
 */
@SuppressWarnings("serial")
public class Notice implements Serializable {

    public static final String BUNDLEKEY_NOTICE = "bundlekey_notic";

    public final static int TYPE_ATME = 23;//@
    public final static int TYPE_COMMENT = 24;//评论

    private int atmeCount;//@个数

    private int reviewCount;//评论个数

    public int getAtmeCount() {
	return atmeCount;
    }

    public void setAtmeCount(int atmeCount) {
	    this.atmeCount = atmeCount;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

}
