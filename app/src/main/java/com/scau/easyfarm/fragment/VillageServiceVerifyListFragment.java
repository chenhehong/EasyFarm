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
import com.scau.easyfarm.adapter.VillageServiceAdapter;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseListFragment;
import com.scau.easyfarm.bean.Constants;
import com.scau.easyfarm.bean.Result;
import com.scau.easyfarm.bean.ResultBean;
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

/**
 * Created by chenhehong on 2016/8/26.
 */
public class VillageServiceVerifyListFragment extends BaseListFragment<VillageService> implements
        AdapterView.OnItemLongClickListener{

    private static final String CACHE_KEY_PREFIX = "verifyVillageServiceList_";

    public final static int ALL_VILLAGE_SERVICE = 0;
    public final static int PASS_VILAGE_SERVICE = 1;
    public final static int WAITING_VILAGE_SERVICE = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//      监听用户登录状态的广播
        IntentFilter filter = new IntentFilter(
                Constants.INTENT_ACTION_USER_CHANGE);
        filter.addAction(Constants.INTENT_ACTION_LOGOUT);
        getActivity().registerReceiver(mReceiver, filter);
//      刷新数据
        requestData(true);
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
//  重载设置子类的列表适配器
    protected VillageServiceAdapter getListAdapter() {
        return new VillageServiceAdapter();
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
    protected VillageServiceList parseList(InputStream is) throws Exception {
        VillageServiceList list = JsonUtils.toBean(VillageServiceList.class, is);
        return list;
    }

//  用于从缓存中读出序列化数据
    @Override
    protected VillageServiceList readList(Serializable seri) {
        return ((VillageServiceList) seri);
    }


    @Override
    protected void sendRequestData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
//          如果需要做搜索功能，可以通过bundle传人参数，进行带参数的请求
        }
        if (mCatalog==WAITING_VILAGE_SERVICE){
            EasyFarmServerApi.getVerifyServiceList(mCatalog, mCurrentPage, VillageService.VILLAGE_SERVICE_WAITING, mHandler);
        }else if (mCatalog==ALL_VILLAGE_SERVICE){
            EasyFarmServerApi.getVerifyServiceList(mCatalog, mCurrentPage, VillageService.VILLAGE_SERVICE_ALL, mHandler);
        }
    }

//  重载点击事件，自定义子类的点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        VillageService villageService = mAdapter.getItem(position);
        if (villageService != null) {
            UIHelper.showVillageServiceDetail(view.getContext(), villageService.getId());
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
        VillageService villageService = mAdapter.getItem(position);
        if (villageService != null) {
            handleLongClick(villageService);
            return true;
        }
        return false;
    }

    private void handleLongClick(final VillageService villageService) {
        String[] items = null;
        items = new String[] {getResources().getString(R.string.delete),"审批" };
        DialogHelp.getSelectDialog(getActivity(), items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    handleDeleteVillageService(villageService);
                } else if (i == 1) {
                    handleVerifyVillageService(villageService);
                }
            }
        }).show();
    }

//  删除申请
    private void handleDeleteVillageService(final VillageService villageService) {
        DialogHelp.getConfirmDialog(getActivity(), "是否删除该申请?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EasyFarmServerApi.deleteVillageService(villageService.getId(), new DeleteVillageServiceResponseHandler(villageService));
            }
        }).show();
    }

//  问答删除句柄
    class DeleteVillageServiceResponseHandler extends OperationResponseHandler {

        DeleteVillageServiceResponseHandler(Object... args) {
            super(args);
        }

    public void onSuccess(int code, ByteArrayInputStream is, Object[] args){
        try {
                Result res = JsonUtils.toBean(ResultBean.class, is).getResult();
//              更新列表
                if (res != null && res.OK()) {
                    AppContext.showToastShort(R.string.delete_success);
                    VillageService villageService = (VillageService) args[0];
                    mAdapter.removeItem(villageService);
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
            AppContext.showToastShort(R.string.delete_faile);
        }
    }

    private void handleVerifyVillageService(VillageService villageService){
        if (villageService != null) {
            UIHelper.showVillageServiceVerify(this, VillageServiceVerifyFragment.REQUESTCODE_VERIFY,villageService.getId());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode!=getActivity().RESULT_OK)
            return;
        if (requestCode==VillageServiceVerifyFragment.REQUESTCODE_VERIFY){
            onRefresh();
        }
    }
}
