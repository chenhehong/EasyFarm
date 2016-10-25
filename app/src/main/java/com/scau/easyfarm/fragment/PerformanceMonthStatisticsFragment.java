package com.scau.easyfarm.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.R;
import com.scau.easyfarm.adapter.PerformanceApplyAdapter;
import com.scau.easyfarm.adapter.PerformanceMonthStatisticsAdapter;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseListFragment;
import com.scau.easyfarm.bean.Constants;
import com.scau.easyfarm.bean.PerformStatisticsList;
import com.scau.easyfarm.bean.Performance;
import com.scau.easyfarm.bean.PerformanceList;
import com.scau.easyfarm.bean.PerformanceStatistics;
import com.scau.easyfarm.ui.empty.EmptyLayout;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.UIHelper;

import java.io.InputStream;
import java.io.Serializable;

/**
 * Created by chenhehong on 2016/8/26.
 */
public class PerformanceMonthStatisticsFragment extends BaseListFragment<PerformanceStatistics> implements
        AdapterView.OnItemLongClickListener{

    private static final String CACHE_KEY_PREFIX = "performance_month_statistics_list_";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//      监听用户登录状态的广播
        IntentFilter filter = new IntentFilter(
                Constants.INTENT_ACTION_USER_CHANGE);
        filter.addAction(Constants.INTENT_ACTION_LOGOUT);
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
//  重载设置子类的列表适配器
    protected PerformanceMonthStatisticsAdapter getListAdapter() {
        return new PerformanceMonthStatisticsAdapter();
    }

//  用户登录状态广播接收器
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setupContent();
        }
    };

    private void setupContent() {
        if (AppContext.getInstance().isLogin()) {
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
            requestData(true);
        } else {
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
            mErrorLayout.setErrorMessage(getString(R.string.unlogin_tip));
        }
    }

    @Override
    protected void requestData(boolean refresh) {
        if (AppContext.getInstance().isLogin()) {
            super.requestData(refresh);
        } else {
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
            mErrorLayout.setErrorMessage(getString(R.string.unlogin_tip));
        }
    }

//  重载该方法，定义子类自己的cachekey
    @Override
    protected String getCacheKeyPrefix() {
        return AppContext.getInstance().getLoginUid()+"_"+CACHE_KEY_PREFIX + mCatalog;
    }

//  重载该方法，对服务器返回的数据进行解析
    @Override
    protected PerformStatisticsList parseList(InputStream is) throws Exception {
        PerformStatisticsList list = JsonUtils.toBean(PerformStatisticsList.class, is);
        return list;
    }

//  用于从缓存中读出序列化数据
    @Override
    protected PerformStatisticsList readList(Serializable seri) {
        return ((PerformStatisticsList) seri);
    }


    @Override
    protected void sendRequestData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
//            如果需要做搜索功能，可以通过bundle传人参数，进行带参数的请求
        }
        EasyFarmServerApi.getMonthStaticsPerformanceList(mCatalog, mCurrentPage, mHandler);
    }

//  重载点击事件，自定义子类的点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        PerformanceStatistics performanceStatistics = mAdapter.getItem(position);
        if (performanceStatistics != null) {
            UIHelper.showPerformanceStatistics(this, performanceStatistics.getMonth());
        }
    }


    @Override
    public void initView(View view) {
        super.initView(view);
        mListView.setOnItemLongClickListener(this);
//      设置状态栏点击事件
        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppContext.getInstance().isLogin()) {
                    mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                    requestData(true);
                } else {
                    UIHelper.showLoginActivity(getActivity());
                }
            }
        });
    }


//  长按监听
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        PerformanceStatistics performanceStatistics = mAdapter.getItem(position);
        if (performanceStatistics != null) {
            handleLongClick(performanceStatistics);
            return true;
        }
        return false;
    }

    private void handleLongClick(final PerformanceStatistics performanceStatistics) {

    }
}
