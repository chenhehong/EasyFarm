package com.scau.easyfarm.bean;


import com.scau.easyfarm.R;
import com.scau.easyfarm.fragment.AboutAppFragment;
import com.scau.easyfarm.fragment.AreaCatalogListFragment;
import com.scau.easyfarm.fragment.ChangePasswordFragment;
import com.scau.easyfarm.fragment.ChooseManualCategoryFragment;
import com.scau.easyfarm.fragment.ExpertBaseChooseManualCategoryFragment;
import com.scau.easyfarm.fragment.ExpertBaseDetailFragment;
import com.scau.easyfarm.fragment.ExpertBaseListFragment;
import com.scau.easyfarm.fragment.FeedBackFragment;
import com.scau.easyfarm.fragment.FindUserFragment;
import com.scau.easyfarm.fragment.ManualListFragment;
import com.scau.easyfarm.fragment.ModifiedCommonUserInformationFragment;
import com.scau.easyfarm.fragment.ModifiedExpertInformationFragment;
import com.scau.easyfarm.fragment.MyInformationFragmentDetail;
import com.scau.easyfarm.fragment.MyPerformanceMonthStatisticsFragment;
import com.scau.easyfarm.fragment.MyPerformanceStatisticsFragment;
import com.scau.easyfarm.fragment.MyServiceMonthStatisticsFragment;
import com.scau.easyfarm.fragment.MyServiceStatisticsFragment;
import com.scau.easyfarm.fragment.PerformanceAddFragment;
import com.scau.easyfarm.fragment.PerformanceDetailFragment;
import com.scau.easyfarm.fragment.PerformanceFunctionFragment;
import com.scau.easyfarm.fragment.PerformanceMonthStatisticsFragment;
import com.scau.easyfarm.fragment.PerformanceStatisticsFragment;
import com.scau.easyfarm.fragment.PerformanceTypeChooseFragment;
import com.scau.easyfarm.fragment.PerformanceVerifyFragment;
import com.scau.easyfarm.fragment.ProofResourceDescriptionChooseFragment;
import com.scau.easyfarm.fragment.RegisterFragment;
import com.scau.easyfarm.fragment.ServerSummaryFragment;
import com.scau.easyfarm.fragment.ServiceMonthStatisticsFragment;
import com.scau.easyfarm.fragment.ServiceStatisticsFragment;
import com.scau.easyfarm.fragment.SettingsFragment;
import com.scau.easyfarm.fragment.SettingsNotificationFragment;
import com.scau.easyfarm.fragment.TweetChooseManualCategoryFragment;
import com.scau.easyfarm.fragment.TweetExpertChooseFragment;
import com.scau.easyfarm.fragment.TweetPubFragment;
import com.scau.easyfarm.fragment.VillageFunctionFragment;
import com.scau.easyfarm.fragment.VillageServiceDetailFragment;
import com.scau.easyfarm.fragment.UserCenterFragment;
import com.scau.easyfarm.fragment.VillageServiceAddFragment;
import com.scau.easyfarm.fragment.VillageServiceProofChooseFragment;
import com.scau.easyfarm.fragment.VillageServiceProofResourPubFragment;
import com.scau.easyfarm.fragment.VillageServiceProofResourceFragment;
import com.scau.easyfarm.fragment.VillageServiceReasonChooseFragment;
import com.scau.easyfarm.fragment.VillageServiceVerifyFragment;
import com.scau.easyfarm.fragment.WitTweetFragment;
import com.scau.easyfarm.viewpagerfragment.NoticeViewPagerFragment;
import com.scau.easyfarm.viewpagerfragment.PerformanceApplyViewPagerFragment;
import com.scau.easyfarm.viewpagerfragment.PerformanceVerifyViewPagerFragment;
import com.scau.easyfarm.viewpagerfragment.VillageServiceVerifyViewPagerFragment;
import com.scau.easyfarm.viewpagerfragment.VillageServiceApplyViewPagerFragment;
import com.scau.easyfarm.viewpagerfragment.VillageServiceProofViewPagerFragment;

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
//    MANUAL_DETAIL(18,R.string.manualdetail_actionbar_title, ManualContentDetailFragment.class),
    VILLAGE_FUNCTION(19,R.string.village_function, VillageFunctionFragment.class),
    VILLAGE_SERVICE_VERIFY_VIEWPAGER(20, R.string.village_service_verify, VillageServiceVerifyViewPagerFragment.class),
    VILLAGE_SERVICE_VERIFY(21, R.string.village_service_verify, VillageServiceVerifyFragment.class),
    VILLAGE_SERVICE_PROOF(22, R.string.village_service_proof_list, VillageServiceProofViewPagerFragment.class),
    VILLAGE_SERVICE_PROOF_RESOURCE(23, R.string.village_service_proof_resource, VillageServiceProofResourceFragment.class),
    VILLAGE_SERVICE_PROOF_RESOURCE_ADD(24, R.string.village_service_proof_resource_add, VillageServiceProofResourPubFragment.class),
    FIND_USER(26, R.string.actionbar_title_search_user, FindUserFragment.class),
    CHOOSE_VILLAGE_SERVICE_REASON(27, R.string.actionbar_title_choose_village_service_reason, VillageServiceReasonChooseFragment.class),
    CHOOSE_PROOF_RESOURCE_DESCRIPTITON(28, R.string.actionbar_title_choose_proof_resource_description, ProofResourceDescriptionChooseFragment.class),
    CHOOSE_PROOF_VILLAGE_SERVICE(29, R.string.village_service_proof_choose, VillageServiceProofChooseFragment.class),
    SERVER_SUMMARY(30, R.string.village_service_summary, ServerSummaryFragment.class),
    EXPERTBASE_LIST(32,R.string.actionbar_expertbase_list, ExpertBaseListFragment.class),
    EXPERTBASE_DETAIL(33,R.string.actionbar_expertbase_detail, ExpertBaseDetailFragment.class),
    EXPERTBASE_CHOOSE_MANUAL_CATEGORY(34,R.string.actionbar_expertbase_type, ExpertBaseChooseManualCategoryFragment.class),
    PERFORMANCE_ADD(35,R.string.performance_add, PerformanceAddFragment.class),
    CHOOSE_PERFORMANCE_TYPE(36, R.string.actionbar_choose_performance_type, PerformanceTypeChooseFragment.class),
    SERVICE_PERFORMANCE_FUNCTION(37,R.string.service_performance_function, PerformanceFunctionFragment.class),
    PERFORMANCE_APPLY_LIST(38,R.string.actionbar_performance_list, PerformanceApplyViewPagerFragment.class),
    PERFORMANCE_DETAIL(39,R.string.performance_detail, PerformanceDetailFragment.class),
    PERFORMANCE_VERIFY(40, R.string.performance_verify, PerformanceVerifyFragment.class),
    PERFORMANCE_VERIFY_VIEWPAGER(41, R.string.performance_verify, PerformanceVerifyViewPagerFragment.class),
    PERFORMANCE_MONTH_STATISTICS(42, R.string.performance_month_statistics, PerformanceMonthStatisticsFragment.class),
    PERFORMANCE_STATISTICS(43, R.string.performance_statistics, PerformanceStatisticsFragment.class),
    MYPERFORMANCE_MONTH_STATISTICS(44, R.string.myperformance_month_statistics, MyPerformanceMonthStatisticsFragment.class),
    MYPERFORMANCE_STATISTICS(45, R.string.myperformance_statistics, MyPerformanceStatisticsFragment.class),
    SERVICE_MONTH_STATISTICS(46, R.string.service_month_statistics, ServiceMonthStatisticsFragment.class),
    SERVICE_STATISTICS(47, R.string.service_statistics, ServiceStatisticsFragment.class),
    MYSERVICE_MONTH_STATISTICS(48, R.string.myservice_month_statistics, MyServiceMonthStatisticsFragment.class),
    MYSERVICE_STATISTICS(49, R.string.myservice_statistics, MyServiceStatisticsFragment.class),
    REGISTER(50, R.string.register, RegisterFragment.class),
    MODIFIED_COMMONUSERINFORMATION(51, R.string.modified_my_information, ModifiedCommonUserInformationFragment.class),
    MODIFIED_EXPERTINFORMATION(52, R.string.modified_my_information, ModifiedExpertInformationFragment.class),
    WITTWEET(53, R.string.actionbar_wittweet, WitTweetFragment.class);

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
