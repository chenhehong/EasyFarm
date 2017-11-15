package com.scau.easyfarm.fragment;

import android.os.Bundle;

import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.R;
import com.scau.easyfarm.bean.ManualCategory;
import com.scau.easyfarm.bean.Module;
import com.scau.easyfarm.bean.SimpleBackPage;
import com.scau.easyfarm.bean.User;

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
        ManualCategory varietyManualCategory = new ManualCategory(0,0,false,"品种库",ManualCategory.VARIETY,"");
        varietyBaseBundle.putSerializable(BaseManualCategoryListFragment.BUNDLEKEY_PARENT_MANUALCATEGORY, varietyManualCategory);
        Module varietyBase = new Module(SimpleBackPage.CHOOSE_MANUAL_CATEGORY,"品种库",R.drawable.func_variety_base_icon,varietyBaseBundle);
        moduleList.add(varietyBase);
        Bundle industryBaseBundle = new Bundle();
        ManualCategory industryManualCategory = new ManualCategory(0,0,false,"产业库",ManualCategory.INDUSTRY,"");
        industryBaseBundle.putSerializable(BaseManualCategoryListFragment.BUNDLEKEY_PARENT_MANUALCATEGORY, industryManualCategory);
        Module industryBase = new Module(SimpleBackPage.CHOOSE_MANUAL_CATEGORY,"产业库",R.drawable.func_industry_base_icon,industryBaseBundle);
        moduleList.add(industryBase);
        Bundle skillBaseBundle = new Bundle();
        ManualCategory skillManualCategory = new ManualCategory(0,0,false,"技术库",ManualCategory.TECHNOLOGY,"");
        skillBaseBundle.putSerializable(BaseManualCategoryListFragment.BUNDLEKEY_PARENT_MANUALCATEGORY, skillManualCategory);
        Module skillBase = new Module(SimpleBackPage.CHOOSE_MANUAL_CATEGORY,"技术库",R.drawable.func_skill_base_icon,skillBaseBundle);
        moduleList.add(skillBase);
        Bundle achievementBaseBundle = new Bundle();
        ManualCategory achievementManualCategory = new ManualCategory(0,0,false,"成果库",ManualCategory.ACHIEVEMENT,"");
        achievementBaseBundle.putSerializable(BaseManualCategoryListFragment.BUNDLEKEY_PARENT_MANUALCATEGORY, achievementManualCategory);
        Module achievementBase = new Module(SimpleBackPage.CHOOSE_MANUAL_CATEGORY,"成果库",R.drawable.func_achievement_base_icon,achievementBaseBundle);
        moduleList.add(achievementBase);
        Bundle expertBaseBundle = new Bundle();
        ManualCategory expertManualCategory = new ManualCategory(0,0,false,"专家库",ManualCategory.EXPERT,"");
        expertBaseBundle.putSerializable(BaseManualCategoryListFragment.BUNDLEKEY_PARENT_MANUALCATEGORY, expertManualCategory);
        Module expertBase = new Module(SimpleBackPage.EXPERTBASE_CHOOSE_MANUAL_CATEGORY,"专家库",R.drawable.func_expert_base_icon,expertBaseBundle);
        moduleList.add(expertBase);
    }
}
