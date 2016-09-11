package com.scau.easyfarm.fragment;

import android.app.Activity;
import android.content.Intent;

import com.scau.easyfarm.bean.ManualCategory;
import com.scau.easyfarm.util.UIHelper;

/**
 * Created by chenhehong on 2016/9/9.
 */
public class ChooseManualCategoryFragment extends BaseManualCategoryListFragment {

    @Override
    void handleSelectManualCategory(ManualCategory selectManualCategory) {
        if (selectManualCategory.isParent()){
            UIHelper.showManualCategory(this,selectManualCategory.getId());
        }else {
            UIHelper.showManualList(this,selectManualCategory.getId());
        }
    }
}
