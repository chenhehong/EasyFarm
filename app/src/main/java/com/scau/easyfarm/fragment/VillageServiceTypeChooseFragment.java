package com.scau.easyfarm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.scau.easyfarm.R;
import com.scau.easyfarm.adapter.SimpleTextListAdapter;
import com.scau.easyfarm.adapter.VillageServiceReasonListAdapter;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.base.BaseSimpleTextChooseFragment;
import com.scau.easyfarm.base.ListBaseAdapter;
import com.scau.easyfarm.bean.Entity;
import com.scau.easyfarm.bean.SimpleText;
import com.scau.easyfarm.bean.SimpleTextList;
import com.scau.easyfarm.bean.VillageServiceReason;
import com.scau.easyfarm.bean.VillageServiceReasonList;
import com.scau.easyfarm.ui.empty.EmptyLayout;
import com.scau.easyfarm.util.JsonUtils;

import java.io.ByteArrayInputStream;
import java.util.List;

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

