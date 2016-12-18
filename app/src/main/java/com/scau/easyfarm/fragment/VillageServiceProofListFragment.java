package com.scau.easyfarm.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.R;
import com.scau.easyfarm.adapter.VillageServiceAdapter;
import com.scau.easyfarm.adapter.VillageServiceProofAdapter;
import com.scau.easyfarm.api.ApiHttpClient;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseListFragment;
import com.scau.easyfarm.bean.Constants;
import com.scau.easyfarm.bean.SimpleBackPage;
import com.scau.easyfarm.bean.TweetsList;
import com.scau.easyfarm.bean.VillageService;
import com.scau.easyfarm.bean.VillageServiceList;
import com.scau.easyfarm.ui.ImageGalleryActivity;
import com.scau.easyfarm.ui.empty.EmptyLayout;
import com.scau.easyfarm.util.DialogHelp;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.UIHelper;

import java.io.InputStream;
import java.io.Serializable;

/**
 * Created by chenhehong on 2016/8/26.
 */
public class VillageServiceProofListFragment extends BaseListFragment<VillageService> implements
        AdapterView.OnItemLongClickListener{

    public static final int UNDERWAY_SERVICE = 1;
    public static final int COMPLETED_SERVICE = 2;

    private static final String CACHE_KEY_PREFIX = "villageServiceProofList_";

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
    protected VillageServiceProofAdapter getListAdapter() {
        return new VillageServiceProofAdapter(this);
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
        if (mCatalog==UNDERWAY_SERVICE) {
            EasyFarmServerApi.getMyVillageServiceProofList(0, mCurrentPage,mHandler);
        }else if (mCatalog==COMPLETED_SERVICE){
            EasyFarmServerApi.getMyVillageServiceProofList(1, mCurrentPage,mHandler);
        }
    }

//  重载点击事件，自定义子类的点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        VillageService villageService = mAdapter.getItem(position);
        if (villageService != null) {
                UIHelper.showVillageServiceProofResource(this, villageService, mCatalog);
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
        items = new String[] {"详情","查看二维码" };
        DialogHelp.getSelectDialog(getActivity(), items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    handleVillageDetail(villageService);
                }else if (i==1){
                    showCommentQrCode(villageService);
                }
            }
        }).show();
    }

//  删除申请
    private void handleVillageDetail(final VillageService villageService) {
        UIHelper.showVillageServiceDetail(getActivity(), villageService.getId());
    }

    private void showCommentQrCode(VillageService villageService){
        ImageGalleryActivity.show(getActivity(), ApiHttpClient.getAbsoluteApiUrl(villageService.getCommentQrcodePath()),"");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;
        if (requestCode==ServerSummaryFragment.REQUESTCODE_SERVERSUMMARY){
            int positin = data.getIntExtra(ServerSummaryFragment.BUNDLEKEY_POSITION,0);
            VillageService v = mAdapter.getItem(positin);
            mAdapter.removeItem(v);
            mAdapter.notifyDataSetChanged();
        }
    }
}
