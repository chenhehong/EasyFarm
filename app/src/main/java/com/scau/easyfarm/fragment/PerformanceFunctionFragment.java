package com.scau.easyfarm.fragment;

import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.R;
import com.scau.easyfarm.bean.Module;
import com.scau.easyfarm.bean.SimpleBackPage;
import com.scau.easyfarm.bean.User;

/**
 * Created by ChenHehong on 2016/6/11.
 */
public class PerformanceFunctionFragment extends BaseFunctionFragment{

    @Override
    public void initModuleList() {
        User u = AppContext.getInstance().getLoginUser();
        if (Module.existModule(u.getModuleList(), Module.MODULE_PERFORMANCE_APPLY)){
            Module performanceApply = new Module(SimpleBackPage.PERFORMANCE_APPLY_LIST,"绩效申请",R.drawable.func_performance_apply_icon,null);
            moduleList.add(performanceApply);
        }
        if (Module.existModule(u.getModuleList(), Module.MODULE_PERFORMANCE_STATISTICS_EXPERT)){
            Module performanceStatisticsExpert = new Module(SimpleBackPage.MYPERFORMANCE_MONTH_STATISTICS,"绩效统计",R.drawable.func_performance_statistic_icon,null);
            moduleList.add(performanceStatisticsExpert);
        }
        if (Module.existModule(u.getModuleList(), Module.MODULE_PERFORMANCE_STATISTICS_VERIFIER)){
            Module performanceStatisticsVerifier = new Module(SimpleBackPage.PERFORMANCE_MONTH_STATISTICS,"绩效统计",R.drawable.func_performance_statistic_icon,null);
            moduleList.add(performanceStatisticsVerifier);
        }
        if (Module.existModule(u.getModuleList(), Module.MODULE_PERFORMANCE_VIREFY)){
            Module performanceVerify = new Module(SimpleBackPage.PERFORMANCE_VERIFY_VIEWPAGER,"绩效审核",R.drawable.func_performance_apply_icon,null);
            moduleList.add(performanceVerify);
        }
    }

}
