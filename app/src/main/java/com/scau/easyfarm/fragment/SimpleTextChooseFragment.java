package com.scau.easyfarm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.scau.easyfarm.R;
import com.scau.easyfarm.adapter.PerformanceTypeListAdapter;
import com.scau.easyfarm.adapter.SimpleTextListAdapter;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.base.BaseSimpleTextChooseFragment;
import com.scau.easyfarm.base.ListBaseAdapter;
import com.scau.easyfarm.bean.Entity;
import com.scau.easyfarm.bean.PerformanceType;
import com.scau.easyfarm.bean.PerformanceTypeList;
import com.scau.easyfarm.bean.SimpleText;
import com.scau.easyfarm.bean.SimpleTextList;
import com.scau.easyfarm.ui.empty.EmptyLayout;
import com.scau.easyfarm.util.JsonUtils;

import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * Created by chenhehong on 2016/9/8.
 */
public class SimpleTextChooseFragment extends BaseSimpleTextChooseFragment{

    public static final int REQUEST_CODE_SIMPLETEXT_SELECT = 114;

    @Override
    public void sendRequest(int currenPage, int pageSize, OperationResponseHandler mHandler) {
        EasyFarmServerApi.getSimpleTextList(currenPage, pageSize, mHandler);
    }
}
