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
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseListFragment;
import com.scau.easyfarm.bean.Constants;
import com.scau.easyfarm.bean.VillageService;
import com.scau.easyfarm.bean.VillageServiceList;
import com.scau.easyfarm.ui.empty.EmptyLayout;
import com.scau.easyfarm.util.DialogHelp;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.UIHelper;

import java.io.InputStream;
import java.io.Serializable;

/**
 * Created by chenhehong on 2016/8/26.
 */
public class VillageServiceProofChooseFragment extends BaseListFragment<VillageService> implements
        AdapterView.OnItemLongClickListener{

    private static final String CACHE_KEY_PREFIX = "villageServiceProofChoose_";

    public static final String SELECTED_VILLAGESERVICE_ID = "selected_village_service_id";
    public static final String SELECTED_VILLAGESERVICE_DEC = "selected_villageservice_dec";

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
        return CACHE_KEY_PREFIX + mCatalog;
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
        EasyFarmServerApi.getMyVillageServiceProofList(0, mCurrentPage, mHandler);
    }

//  重载点击事件，自定义子类的点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        VillageService villageService = mAdapter.getItem(position);
        if (villageService != null) {
            Intent result = new Intent();
            result.putExtra(SELECTED_VILLAGESERVICE_ID,villageService.getId());
            String s = "";
            s+=villageService.getBusinessDate()+"至"+villageService.getReturnDate()+"于"+villageService.getBusinessArea()+villageService.getBusinessAddress()+"。事由:"+villageService.getBusinessReason();
            result.putExtra(SELECTED_VILLAGESERVICE_DEC,s);
            getActivity().setResult(getActivity().RESULT_OK, result);
            getActivity().finish();
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
        items = new String[] {"详情" };
        DialogHelp.getSelectDialog(getActivity(), items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    handleVillageDetail(villageService);
                }
            }
        }).show();
    }

    private void handleVillageDetail(final VillageService villageService) {
        UIHelper.showVillageServiceDetail(getActivity(), villageService.getId());
    }
}
