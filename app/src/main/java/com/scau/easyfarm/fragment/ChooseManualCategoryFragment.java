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
//      设置actionbar
        ManualCategory nextLevelManualCategory = new ManualCategory(selectManualCategory.getId(),selectManualCategory.getParentId(),selectManualCategory.isParent(),
                selectManualCategory.getCategoryName(),selectManualCategory.getCategoryCode(),selectManualCategory.getCotegoryDescription());
        if (nextLevelManualCategory.getId()==parentManualCategory.getId()){
            nextLevelManualCategory.setCategoryName(parentManualCategory.getCategoryName());
        }else {
            nextLevelManualCategory.setCategoryName(parentManualCategory.getCategoryName()+"/"+nextLevelManualCategory.getCategoryName());
        }
        if (nextLevelManualCategory.isParent()){
            UIHelper.showManualCategory(this,nextLevelManualCategory);
        }else {
            UIHelper.showManualList(this,nextLevelManualCategory);
        }
    }
}
