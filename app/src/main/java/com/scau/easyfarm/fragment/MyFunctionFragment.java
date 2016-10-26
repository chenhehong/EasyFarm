package com.scau.easyfarm.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.R;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.bean.Constants;
import com.scau.easyfarm.bean.ManualCategory;
import com.scau.easyfarm.bean.Module;
import com.scau.easyfarm.bean.SimpleBackPage;
import com.scau.easyfarm.bean.User;
import com.scau.easyfarm.ui.empty.EmptyLayout;
import com.scau.easyfarm.util.UIHelper;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by ChenHehong on 2016/6/11.
 */
public class MyFunctionFragment extends BaseFunctionFragment{

    @Override
    public void initModuleList() {
        User u = AppContext.getInstance().getLoginUser();
        if (Module.existModule(u.getModuleList(),Module.MODULE_SERVICE_MANAGE)){
            Module serviceMange = new Module(SimpleBackPage.VILLAGE_FUNCTION,"服务管理",R.drawable.func_village_service_apply_icon,null);
            moduleList.add(serviceMange);
        }
        if (Module.existModule(u.getModuleList(),Module.MODULE_PERFORMANCE_MANAGE)){
            Module performanceManage = new Module(SimpleBackPage.SERVICE_PERFORMANCE_FUNCTION,"绩效管理",R.drawable.func_village_service_performance_icon,null);
            moduleList.add(performanceManage);
        }
        Bundle varietyBaseBundle = new Bundle();
        varietyBaseBundle.putString(BaseManualCategoryListFragment.BUNDLEKEY_TYPE, ManualCategory.VARIETY);
        Module varietyBase = new Module(SimpleBackPage.CHOOSE_MANUAL_CATEGORY,"品种库",R.drawable.func_agriculture_knowledge_base_icon,varietyBaseBundle);
        moduleList.add(varietyBase);
        Bundle industryBaseBundle = new Bundle();
        industryBaseBundle.putString(BaseManualCategoryListFragment.BUNDLEKEY_TYPE, ManualCategory.INDUSTRY);
        Module industryBase = new Module(SimpleBackPage.CHOOSE_MANUAL_CATEGORY,"产业库",R.drawable.func_agriculture_knowledge_base_icon,industryBaseBundle);
        moduleList.add(industryBase);
        Bundle skillBaseBundle = new Bundle();
        skillBaseBundle.putString(BaseManualCategoryListFragment.BUNDLEKEY_TYPE, ManualCategory.TECHNOLOGY);
        Module skillBase = new Module(SimpleBackPage.CHOOSE_MANUAL_CATEGORY,"技术库",R.drawable.func_agriculture_knowledge_base_icon,skillBaseBundle);
        moduleList.add(skillBase);
        Bundle achievementBaseBundle = new Bundle();
        achievementBaseBundle.putString(BaseManualCategoryListFragment.BUNDLEKEY_TYPE, ManualCategory.ACHIEVEMENT);
        Module achievementBase = new Module(SimpleBackPage.CHOOSE_MANUAL_CATEGORY,"成果库",R.drawable.func_agriculture_knowledge_base_icon,achievementBaseBundle);
        moduleList.add(achievementBase);
        Bundle expertBaseBundle = new Bundle();
        expertBaseBundle.putString(BaseManualCategoryListFragment.BUNDLEKEY_TYPE, ManualCategory.EXPERT);
        Module expertBase = new Module(SimpleBackPage.CHOOSE_MANUAL_CATEGORY,"专家库",R.drawable.func_agriculture_knowledge_base_icon,expertBaseBundle);
        moduleList.add(expertBase);
    }
}
