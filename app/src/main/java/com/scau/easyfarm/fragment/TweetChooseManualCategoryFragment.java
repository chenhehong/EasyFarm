package com.scau.easyfarm.fragment;

import android.app.Activity;
import android.content.Intent;

import com.scau.easyfarm.bean.ManualCategory;
import com.scau.easyfarm.util.UIHelper;

/**
 * Created by chenhehong on 2016/9/9.
 */
public class TweetChooseManualCategoryFragment extends BaseManualCategoryListFragment {

    public static final String SELECTED_MANUAL_COTEGORY_ID = "selected_manual_cotegory_id";
    public static final String SELECTED_MANUAL_COTEGORY_NAME = "selected_manual_cotegory_name";

    @Override
    void handleSelectManualCategory(ManualCategory selectManualCategory) {
//      设置actionbar
        ManualCategory nextLevelManualCategory = new ManualCategory(selectManualCategory.getId(),selectManualCategory.getParentId(),selectManualCategory.isParent(),
                selectManualCategory.getCategoryName(),selectManualCategory.getCategoryCode(),selectManualCategory.getCotegoryDescription());
        if (nextLevelManualCategory.getId()==parentManualCategory.getId()){
            nextLevelManualCategory.setCategoryName(parentManualCategory.getCategoryName());
        }else {
            nextLevelManualCategory.setCategoryName(parentManualCategory.getCategoryName()+nextLevelManualCategory.getCategoryName()+"/");
        }
        if (nextLevelManualCategory.isParent()){
            UIHelper.showTweetTypeChoose(this,nextLevelManualCategory,MANUAL_COTEGORY_LIST_REQUEST_CODE);
        }else {
            Intent result = new Intent();
            result.putExtra(SELECTED_MANUAL_COTEGORY_ID, nextLevelManualCategory.getId());
            result.putExtra(SELECTED_MANUAL_COTEGORY_NAME, nextLevelManualCategory.getCategoryName());
            getActivity().setResult(getActivity().RESULT_OK, result);
            getActivity().finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)
            return;
        if (requestCode==MANUAL_COTEGORY_LIST_REQUEST_CODE){
            int selectedManualCoregoryId = data.getIntExtra(SELECTED_MANUAL_COTEGORY_ID,0);
            String selectedManualcoregoryName = data.getStringExtra(SELECTED_MANUAL_COTEGORY_NAME);
            Intent result = new Intent();
            result.putExtra(SELECTED_MANUAL_COTEGORY_ID, selectedManualCoregoryId);
            result.putExtra(SELECTED_MANUAL_COTEGORY_NAME, selectedManualcoregoryName);
            getActivity().setResult(getActivity().RESULT_OK, result);
            getActivity().finish();
        }
    }
}
