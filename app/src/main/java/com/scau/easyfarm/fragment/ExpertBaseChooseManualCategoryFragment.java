package com.scau.easyfarm.fragment;

import com.scau.easyfarm.bean.ManualCategory;
import com.scau.easyfarm.util.UIHelper;

/**
 * Created by chenhehong on 2016/9/9.
 */
public class ExpertBaseChooseManualCategoryFragment extends BaseManualCategoryListFragment {

    @Override
    void handleSelectManualCategory(ManualCategory selectManualCategory) {
        if (selectManualCategory.isParent()){
            UIHelper.showExpertBaseManualCategory(this,selectManualCategory.getId(),"");
        }else {
            UIHelper.showExpertBaseList(this, selectManualCategory.getId());
        }
    }
}
