package com.scau.easyfarm.bean;


import com.scau.easyfarm.R;
import com.scau.easyfarm.fragment.AboutAppFragment;
import com.scau.easyfarm.fragment.FeedBackFragment;
import com.scau.easyfarm.fragment.MyInformationFragmentDetail;
import com.scau.easyfarm.fragment.SettingsFragment;
import com.scau.easyfarm.fragment.SettingsNotificationFragment;
import com.scau.easyfarm.fragment.UserCenterFragment;
import com.scau.easyfarm.viewpagerfragment.NoticeViewPagerFragment;

public enum SimpleBackPage {


    USER_CENTER(5, R.string.actionbar_title_user_center,
            UserCenterFragment.class),
    MY_MES(9, R.string.actionbar_title_mes, NoticeViewPagerFragment.class),
    SETTING(15, R.string.setting, SettingsFragment.class),
    SETTING_NOTIFICATION(16, R.string.actionbar_title_setting_notification,
            SettingsNotificationFragment.class),
    ABOUT_APP(17, R.string.about, AboutAppFragment.class),
    MY_INFORMATION_DETAIL(28, R.string.actionbar_title_my_information,
            MyInformationFragmentDetail.class),
    FEED_BACK(29, R.string.str_feedback_title, FeedBackFragment.class);

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
