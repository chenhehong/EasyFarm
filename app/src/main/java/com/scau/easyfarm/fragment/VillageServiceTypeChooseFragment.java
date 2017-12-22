package com.scau.easyfarm.fragment;

import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseSimpleTextChooseFragment;

/**
 * Created by chenhehong on 2016/9/8.
 */
public class VillageServiceTypeChooseFragment extends BaseSimpleTextChooseFragment {

    public static final int  REQUEST_CODE_VSTYPE_SELECT = 113;

    @Override
    public void sendRequest(int currenPage, int pageSize, OperationResponseHandler mHandler) {
        EasyFarmServerApi.getServiceTypeList(currenPage, pageSize, mHandler);
    }
}

