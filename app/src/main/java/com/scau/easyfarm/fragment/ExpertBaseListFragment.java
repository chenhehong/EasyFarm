package com.scau.easyfarm.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.scau.easyfarm.adapter.ExpertBaseAdapter;
import com.scau.easyfarm.adapter.ManualAdapter;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseListFragment;
import com.scau.easyfarm.bean.ExpertBase;
import com.scau.easyfarm.bean.ExpertBaseList;
import com.scau.easyfarm.bean.ManualContent;
import com.scau.easyfarm.bean.ManualList;
import com.scau.easyfarm.ui.empty.EmptyLayout;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.UIHelper;

import java.io.InputStream;
import java.io.Serializable;

/**
 * Created by chenhehong on 2016/8/26.
 */
public class ExpertBaseListFragment extends BaseListFragment<ExpertBase>{

    private static final String CACHE_KEY_PREFIX = "expertbaselist_";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
//  重载设置子类的列表适配器
    protected ExpertBaseAdapter getListAdapter() {
        return new ExpertBaseAdapter();
    }

//  重载该方法，定义子类自己的cachekey
    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX;
    }

//  重载该方法，对服务器返回的数据进行解析
    @Override
    protected ExpertBaseList parseList(InputStream is) throws Exception {
        ExpertBaseList list = JsonUtils.toBean(ExpertBaseList.class, is);
        return list;
    }

//  用于从缓存中读出序列化数据
    @Override
    protected ExpertBaseList readList(Serializable seri) {
        return ((ExpertBaseList) seri);
    }


    @Override
    protected void sendRequestData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
//            如果需要做搜索功能，可以通过bundle传人参数，进行带参数的请求
        }
        EasyFarmServerApi.getManualList(mCatalog,"", mCurrentPage, mHandler);
    }

//  重载点击事件，自定义子类的点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        ExpertBase expertBase= mAdapter.getItem(position);
        if (expertBase != null) {
            UIHelper.showExpertBseDetail(this, expertBase.getId());
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
