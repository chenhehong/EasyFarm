package com.scau.easyfarm.fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.scau.easyfarm.R;
import com.scau.easyfarm.bean.ManualCategory;
import com.scau.easyfarm.bean.SimpleBackPage;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_icon_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.search_icon:
                UIHelper.showSimpleBack(this, SimpleBackPage.SEARCH_MANUAL);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
