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
import com.scau.easyfarm.adapter.ActiveAdapter;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseListFragment;
import com.scau.easyfarm.bean.Active;
import com.scau.easyfarm.bean.ActiveList;
import com.scau.easyfarm.bean.Constants;
import com.scau.easyfarm.bean.Notice;
import com.scau.easyfarm.bean.ResultBean;
import com.scau.easyfarm.bean.Tweet;
import com.scau.easyfarm.ui.MainActivity;
import com.scau.easyfarm.ui.empty.EmptyLayout;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.TLog;
import com.scau.easyfarm.util.UIHelper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;

/**
 * Created by chenhehong on 2016/8/26.
 */
public class ActiveFragment extends BaseListFragment<Active> implements
        AdapterView.OnItemLongClickListener{

    public final static int CATALOG_ATME = 2;// @我
    public final static int CATALOG_COMMENT = 3;// 评论

    private static final String CACHE_KEY_PREFIX = "active_list_";
    private String timeStamp = "";

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

//  重新进入如果有新的消息就自动刷新
    @Override
    public void onResume() {
        super.onResume();
        if (AppContext.getInstance().isLogin()){
            Notice notice = MainActivity.mNotice;
            if (notice == null) {
                return;
            }
            if (notice.getAtmeCount() > 0 && mCatalog == CATALOG_ATME) {
                onRefresh();
            } else if (notice.getReviewCount() > 0
                    && mCatalog == CATALOG_COMMENT) {
                onRefresh();
            }
        }
    }

    @Override
//  重载设置子类的列表适配器
    protected ActiveAdapter getListAdapter() {
        return new ActiveAdapter();
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
    protected ActiveList parseList(InputStream is) throws Exception {
        ActiveList list = JsonUtils.toBean(ActiveList.class, is);
        if (list!=null){
            timeStamp = list.getLastReadTime();
        }
        return list;
    }

//  用于从缓存中读出序列化数据
    @Override
    protected ActiveList readList(Serializable seri) {
        return ((ActiveList) seri);
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


    @Override
    protected void sendRequestData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
//            如果需要做搜索功能，可以通过bundle传人参数，进行带参数的请求
        }
        if (mCatalog==CATALOG_ATME){
            EasyFarmServerApi.getNoticeList(Notice.TYPE_ATME, mCurrentPage,mHandler);
        }else if (mCatalog == CATALOG_COMMENT){
            EasyFarmServerApi.getNoticeList(Notice.TYPE_COMMENT, mCurrentPage, mHandler);
        }
    }

//  重载点击事件，自定义子类的点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Active active = mAdapter.getItem(position);
        if (active != null) {
            if (mCatalog==CATALOG_ATME){
                UIHelper.showTweetDetail(getActivity(),new Tweet(),Integer.valueOf(active.getMetaID()+""));
            }else if(mCatalog==CATALOG_COMMENT){
                UIHelper.showTweetDetail(getActivity(),new Tweet(),Integer.valueOf(active.getMetaID()+""));
            }
        }
    }


//  长按监听
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }



    private  OperationResponseHandler mClearNoticeCountHandler = new OperationResponseHandler() {

        @Override
        public void onSuccess(int code, ByteArrayInputStream is, Object[] args) throws Exception {
            try {
                ResultBean rsb = JsonUtils.toBean(ResultBean.class, is);
                if (rsb.getResult().OK()&&rsb.getNotice()!=null){
                    UIHelper.sendNoticeBroadCast(getActivity(), rsb.getNotice());
                }
            }catch (Exception e){
                e.printStackTrace();
                onFailure(code, e.getMessage(), args);
            }
        }

        @Override
        public void onFailure(int code, String errorMessage, Object[] args) {
            TLog.error(code + errorMessage);
        }
    };

//   刷新消息列表成功后，清楚notice数量
    @Override
    protected void onRefreshNetworkSuccess() {
        if (AppContext.getInstance().isLogin()) {
            if (mCatalog==Notice.TYPE_ATME) {
                EasyFarmServerApi.clearNoticeCount(Notice.TYPE_ATME,timeStamp,mClearNoticeCountHandler);
            } else if (mCatalog==Notice.TYPE_COMMENT) {
                EasyFarmServerApi.clearNoticeCount(Notice.TYPE_COMMENT,timeStamp,mClearNoticeCountHandler);
            }
        }
    }
}
