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
import com.scau.easyfarm.adapter.PerformanceApplyAdapter;
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
public class PerformanceApplyFragment extends BaseListFragment<Performance> implements
        AdapterView.OnItemLongClickListener{

    private static final String CACHE_KEY_PREFIX = "performancelist_";

    public final static int ALL_PERFORMANCE = 0;
    public final static int PASS_PERFORMANCE = 1;
    public final static int WAITING_PERFORMANCE = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//      监听用户登录状态的广播
        IntentFilter filter = new IntentFilter(
                Constants.INTENT_ACTION_USER_CHANGE);
        filter.addAction(Constants.INTENT_ACTION_LOGOUT);
        getActivity().registerReceiver(mReceiver, filter);

        requestData(true);
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
//  重载设置子类的列表适配器
    protected PerformanceApplyAdapter getListAdapter() {
        return new PerformanceApplyAdapter();
    }

//  用户登录状态广播接收器
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setupContent();
        }
    };

//  处理用户登录状态广播，登录了的话”我的问答“栏目可以加载数据，否则显示未登录
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
//            如果需要做搜索功能，可以通过bundle传人参数，进行带参数的请求
        }
        if (mCatalog== PASS_PERFORMANCE){
            EasyFarmServerApi.getApplyPerformanceList(mCatalog, mCurrentPage, Performance.PERFORMANCE_PASS, mHandler);
        }else if (mCatalog == ALL_PERFORMANCE){
            EasyFarmServerApi.getApplyPerformanceList(mCatalog, mCurrentPage, Performance.PERFORMANCE_ALL, mHandler);
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
        if (performance != null) {
            handleLongClick(performance);
            return true;
        }
        return false;
    }

    private void handleLongClick(final Performance performance) {
        String[] items = null;
        if (performance.getStatus()==Performance.PERFORMANCE_WAITING){
            items = new String[] {getResources().getString(R.string.delete) };
            DialogHelp.getSelectDialog(getActivity(), items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (i == 0) {
                        handleDeletePerformance(performance);
                    }
                }
            }).show();
        }
    }

//  删除申请
    private void handleDeletePerformance(final Performance performance) {
        DialogHelp.getConfirmDialog(getActivity(), "是否删除该申请?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showWaitDialog("删除中...");
                EasyFarmServerApi.deletePerformance(performance.getId(), new DeleteResponseHandler(performance));
            }
        }).show();
    }

//  问答删除句柄
    class DeleteResponseHandler extends OperationResponseHandler {

        DeleteResponseHandler(Object... args) {
            super(args);
        }

        @Override
        public void onSuccess(int code, ByteArrayInputStream is, Object[] args)
                throws Exception {
            try {
                Result res = JsonUtils.toBean(ResultBean.class, is).getResult();
//              更新列表
                if (res != null && res.OK()) {
                    hideWaitDialog();
                    AppContext.showToastShort(R.string.delete_success);
                    Performance performance = (Performance) args[0];
                    mAdapter.removeItem(performance);
                    mAdapter.notifyDataSetChanged();
                } else {
                    onFailure(code, res.getErrorMessage(), args);
                }
            } catch (Exception e) {
                e.printStackTrace();
                onFailure(code, e.getMessage(), args);
            }
        }

        @Override
        public void onFailure(int code, String errorMessage, Object[] args) {
            AppContext.showToastShort(R.string.delete_faile+errorMessage);
        }
    }

}
