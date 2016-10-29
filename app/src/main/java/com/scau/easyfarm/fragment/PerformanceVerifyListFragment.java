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
import com.scau.easyfarm.adapter.PerformanceVerifyAdapter;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseListFragment;
import com.scau.easyfarm.bean.Constants;
import com.scau.easyfarm.bean.Performance;
import com.scau.easyfarm.bean.PerformanceList;
import com.scau.easyfarm.bean.Result;
import com.scau.easyfarm.bean.ResultBean;
import com.scau.easyfarm.bean.VillageService;
import com.scau.easyfarm.ui.empty.EmptyLayout;
import com.scau.easyfarm.util.DialogHelp;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.UIHelper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;

/**
 * Created by chenhehong on 2016/8/26.
 */
public class PerformanceVerifyListFragment extends BaseListFragment<Performance> implements
        AdapterView.OnItemLongClickListener{

    private static final String CACHE_KEY_PREFIX = "verifyPerformanceList_";

    public final static int COMPLETED_VERIFY = 1;//已审核
    public final static int WAITING_VERIFY = 2;//带审核

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
    protected PerformanceVerifyAdapter getListAdapter() {
        return new PerformanceVerifyAdapter(this);
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
    protected PerformanceList parseList(InputStream is) throws Exception {
        PerformanceList list = JsonUtils.toBean(PerformanceList.class, is);
        return list;
    }

//  用于从缓存中读出序列化数据
    @Override
    protected PerformanceList readList(Serializable seri) {
        return ((PerformanceList) seri);
    }


    @Override
    protected void sendRequestData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
//          如果需要做搜索功能，可以通过bundle传人参数，进行带参数的请求
        }
        if (mCatalog== WAITING_VERIFY){
            EasyFarmServerApi.getVerifyPerformanceList(1, mCurrentPage,0, mHandler);
        }else if (mCatalog== COMPLETED_VERIFY){
            EasyFarmServerApi.getVerifyPerformanceList(0, mCurrentPage, Performance.PERFORMANCE_ALL, mHandler);
        }
    }

//  重载点击事件，自定义子类的点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Performance performance = mAdapter.getItem(position);
        if (performance != null) {
            UIHelper.showPerformanceDetail(view.getContext(), performance.getId());
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
        Performance performance = mAdapter.getItem(position);
        if (performance != null&&mCatalog== WAITING_VERIFY) {
            handleLongClick(performance);
            return true;
        }
        return false;
    }

    public void handleLongClick(final Performance performance) {
        String[] items = null;
//        items = new String[] {getResources().getString(R.string.delete),"审批" };
//        DialogHelp.getSelectDialog(getActivity(), items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                if (i == 0) {
//                    handleDeleteVillageService(villageService);
//                } else if (i == 1) {
//                    handleVerifyPerformance(villageService);
//                }
//            }
//        }).show();
        items = new String[] {"审批" };
        DialogHelp.getSelectDialog(getActivity(), items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    handleVerifyPerformance(performance);
                }
            }
        }).show();
    }

    private void handleVerifyPerformance(Performance performance){
        if (performance != null) {
            UIHelper.showPerformanceVerify(this, PerformanceVerifyFragment.REQUESTCODE_VERIFY, performance.getId());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode!=getActivity().RESULT_OK)
            return;
        if (requestCode==PerformanceVerifyFragment.REQUESTCODE_VERIFY){
            onRefresh();
        }
    }
}
