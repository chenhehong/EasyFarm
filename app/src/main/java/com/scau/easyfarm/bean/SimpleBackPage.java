package com.scau.easyfarm.bean;


import com.scau.easyfarm.R;
import com.scau.easyfarm.fragment.MyInformationFragmentDetail;
import com.scau.easyfarm.fragment.UserCenterFragment;
import com.scau.easyfarm.viewpagerfragment.NoticeViewPagerFragment;

public enum SimpleBackPage {


    USER_CENTER(5, R.string.actionbar_title_user_center,
            UserCenterFragment.class),
    MY_MES(9, R.string.actionbar_title_mes, NoticeViewPagerFragment.class),
    MY_INFORMATION_DETAIL(28, R.string.actionbar_title_my_information,
            MyInformationFragmentDetail.class);

    private int title;
    private Class<?> clz;
    private int value;

    private SimpleBackPage(int value, int title, Class<?> clz) {
        this.value = value;
        this.title = title;
        this.clz = clz;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static SimpleBackPage getPageByValue(int val) {
        for (SimpleBackPage p : values()) {
            if (p.getValue() == val)
                return p;
        }
        return null;
    }
}
