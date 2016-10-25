package com.scau.easyfarm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.R;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.bean.ManualCategory;
import com.scau.easyfarm.bean.Module;
import com.scau.easyfarm.bean.SimpleBackPage;
import com.scau.easyfarm.bean.User;
import com.scau.easyfarm.util.UIHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by ChenHehong on 2016/6/11.
 */
public class PerformanceFunctionFragment extends BaseFunctionFragment{

    @Override
    public void initModuleList() {
        User u = AppContext.getInstance().getLoginUser();
        if (Module.existModule(u.getModuleList(), Module.MODULE_PERFORMANCE_APPLY)){
            Module performanceApply = new Module(SimpleBackPage.PERFORMANCE_APPLY_LIST,"业绩申请",R.drawable.func_agriculture_knowledge_base_icon,null);
            moduleList.add(performanceApply);
        }
        if (Module.existModule(u.getModuleList(), Module.MODULE_PERFORMANCE_STATISTICS_EXPERT)){
            Module performanceStatisticsExpert = new Module(SimpleBackPage.MYPERFORMANCE_MONTH_STATISTICS,"个人业绩统计",R.drawable.func_village_service_performance_icon,null);
            moduleList.add(performanceStatisticsExpert);
        }
        if (Module.existModule(u.getModuleList(), Module.MODULE_PERFORMANCE_STATISTICS_VERIFIER)){
            Module performanceStatisticsVerifier = new Module(SimpleBackPage.PERFORMANCE_MONTH_STATISTICS,"部门业绩统计",R.drawable.func_village_service_performance_icon,null);
            moduleList.add(performanceStatisticsVerifier);
        }
        if (Module.existModule(u.getModuleList(), Module.MODULE_PERFORMANCE_VIREFY)){
            Module performanceVerify = new Module(SimpleBackPage.PERFORMANCE_VERIFY_VIEWPAGER,"业绩审核",R.drawable.func_agriculture_knowledge_base_icon,null);
            moduleList.add(performanceVerify);
        }
    }

}
