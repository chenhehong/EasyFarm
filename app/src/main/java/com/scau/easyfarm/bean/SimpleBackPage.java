package com.scau.easyfarm.bean;


import com.scau.easyfarm.R;
import com.scau.easyfarm.fragment.AboutAppFragment;
import com.scau.easyfarm.fragment.AreaCatalogListFragment;
import com.scau.easyfarm.fragment.ChangePasswordFragment;
import com.scau.easyfarm.fragment.ChooseManualCategoryFragment;
import com.scau.easyfarm.fragment.FeedBackFragment;
import com.scau.easyfarm.fragment.ManualContentDetailFragment;
import com.scau.easyfarm.fragment.ManualListFragment;
import com.scau.easyfarm.fragment.MyInformationFragmentDetail;
import com.scau.easyfarm.fragment.SettingsFragment;
import com.scau.easyfarm.fragment.SettingsNotificationFragment;
import com.scau.easyfarm.fragment.TweetChooseManualCategoryFragment;
import com.scau.easyfarm.fragment.TweetExpertChooseFragment;
import com.scau.easyfarm.fragment.TweetPubFragment;
import com.scau.easyfarm.fragment.VillageServiceDetailFragment;
import com.scau.easyfarm.fragment.UserCenterFragment;
import com.scau.easyfarm.fragment.VillageServiceAddFragment;
import com.scau.easyfarm.viewpagerfragment.NoticeViewPagerFragment;
import com.scau.easyfarm.viewpagerfragment.VillageServiceApplyViewPagerFragment;

public enum SimpleBackPage {


    USER_CENTER(1, R.string.actionbar_title_user_center,
            UserCenterFragment.class),
    MY_MES(2, R.string.actionbar_title_mes, NoticeViewPagerFragment.class),
    SETTING(3, R.string.setting, SettingsFragment.class),
    SETTING_NOTIFICATION(4, R.string.actionbar_title_setting_notification,
            SettingsNotificationFragment.class),
    ABOUT_APP(5, R.string.about, AboutAppFragment.class),
    MY_INFORMATION_DETAIL(6, R.string.actionbar_title_my_information,
            MyInformationFragmentDetail.class),
    FEED_BACK(7, R.string.str_feedback_title, FeedBackFragment.class),
    CHANGE_PASSWORD(8,R.string.password_setting, ChangePasswordFragment.class),
    TWEET_PUB(9, R.string.actionbar_title_tweetpub, TweetPubFragment.class),
    VILLAGE_SERVICE_APPLY(10, R.string.village_service_apply, VillageServiceApplyViewPagerFragment.class),
    VILLAGE_SERVICE_ADD(11,R.string.village_service_apply_add, VillageServiceAddFragment.class),
    TWEET_CHOOSE_EXPERT(12,R.string.tweet_expert_choose, TweetExpertChooseFragment.class),
    CHOOSE_AREA(13,R.string.area_choose, AreaCatalogListFragment.class),
    VILLAGE_SERVICE_DETAIL(14,R.string.village_service_detail, VillageServiceDetailFragment.class),
    TWEET_CHOOSE_TYPE(15,R.string.tweet_type_choose, TweetChooseManualCategoryFragment.class),
    CHOOSE_MANUAL_CATEGORY(16,R.string.manualcategory_actionbar_title, ChooseManualCategoryFragment.class),
    MANUAL_LIST(17,R.string.manuallist_actionbar_title, ManualListFragment.class),
    MANUAL_DETAIL(18,R.string.manualdetail_actionbar_title, ManualContentDetailFragment.class);

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
