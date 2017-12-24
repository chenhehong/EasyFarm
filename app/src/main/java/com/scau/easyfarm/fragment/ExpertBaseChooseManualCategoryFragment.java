package com.scau.easyfarm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scau.easyfarm.R;
import com.scau.easyfarm.bean.ManualCategory;
import com.scau.easyfarm.util.UIHelper;

/**
 * Created by chenhehong on 2016/9/9.
 */
public class ExpertBaseChooseManualCategoryFragment extends BaseManualCategoryListFragment {

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
            UIHelper.showExpertBaseManualCategory(this, nextLevelManualCategory);
        }else {
            UIHelper.showExpertBaseList(this,nextLevelManualCategory);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
    }
}
