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
        if (selectManualCategory.isParent()){
            UIHelper.showTweetTypeChoose(this,selectManualCategory,MANUAL_COTEGORY_LIST_REQUEST_CODE);
        }else {
            Intent result = new Intent();
            result.putExtra(SELECTED_MANUAL_COTEGORY_ID, selectManualCategory.getId());
//          如果是“全部”
            if (selectManualCategory.getId()==parentManualCategory.getId()){
                result.putExtra(SELECTED_MANUAL_COTEGORY_NAME, parentManualCategory.getCategoryName());
            }else {
                result.putExtra(SELECTED_MANUAL_COTEGORY_NAME, selectManualCategory.getCategoryName());
            }
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
