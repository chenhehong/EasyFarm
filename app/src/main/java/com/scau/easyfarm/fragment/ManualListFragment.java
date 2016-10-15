package com.scau.easyfarm.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.R;
import com.scau.easyfarm.adapter.ManualAdapter;
import com.scau.easyfarm.adapter.VillageServiceAdapter;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseListFragment;
import com.scau.easyfarm.bean.Constants;
import com.scau.easyfarm.bean.ManualContent;
import com.scau.easyfarm.bean.ManualList;
import com.scau.easyfarm.bean.Result;
import com.scau.easyfarm.bean.ResultBean;
import com.scau.easyfarm.bean.Tweet;
import com.scau.easyfarm.bean.TweetsList;
import com.scau.easyfarm.bean.VillageService;
import com.scau.easyfarm.bean.VillageServiceList;
import com.scau.easyfarm.ui.empty.EmptyLayout;
import com.scau.easyfarm.util.DialogHelp;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.UIHelper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by chenhehong on 2016/8/26.
 */
public class ManualListFragment extends BaseListFragment<ManualContent>{

    private static final String CACHE_KEY_PREFIX = "manuallist_";
    public static final String MANUALCATEGORYCODE = "manualcategorycode";

    private String seletedManualCategoryCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            seletedManualCategoryCode = args.getString(MANUALCATEGORYCODE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
//  重载设置子类的列表适配器
    protected ManualAdapter getListAdapter() {
        return new ManualAdapter();
    }

    @Override
    protected void requestData(boolean refresh) {
        super.requestData(refresh);
    }

//  重载该方法，定义子类自己的cachekey
    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + seletedManualCategoryCode;
    }

//  重载该方法，对服务器返回的数据进行解析
    @Override
    protected ManualList parseList(InputStream is) throws Exception {
        ManualList list = JsonUtils.toBean(ManualList.class, is);
        return list;
    }

//  用于从缓存中读出序列化数据
    @Override
    protected ManualList readList(Serializable seri) {
        return ((ManualList) seri);
    }


    @Override
    protected void sendRequestData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
//            如果需要做搜索功能，可以通过bundle传人参数，进行带参数的请求
        }
        EasyFarmServerApi.getManualList(mCatalog,seletedManualCategoryCode, mCurrentPage, mHandler);
    }

//  重载点击事件，自定义子类的点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        ManualContent manualContent = mAdapter.getItem(position);
        if (manualContent != null) {
            UIHelper.showManualContentDetail(view.getContext(), manualContent.getId());
        }
    }


    @Override
    public void initView(View view) {
        super.initView(view);
//      设置状态栏点击事件
        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                    mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                    requestData(true);
            }
        });
    }

}
