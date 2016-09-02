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
import com.scau.easyfarm.adapter.TweetAdapter;
import com.scau.easyfarm.adapter.VillageServiceAdapter;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseListFragment;
import com.scau.easyfarm.bean.Constants;
import com.scau.easyfarm.bean.Result;
import com.scau.easyfarm.bean.ResultBean;
import com.scau.easyfarm.bean.Tweet;
import com.scau.easyfarm.bean.TweetsList;
import com.scau.easyfarm.bean.VillageService;
import com.scau.easyfarm.bean.VillageServiceList;
import com.scau.easyfarm.ui.empty.EmptyLayout;
import com.scau.easyfarm.util.DialogHelp;
import com.scau.easyfarm.util.HTMLUtil;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.TDevice;
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
public class VillageServiceFragment extends BaseListFragment<VillageService> implements
        AdapterView.OnItemLongClickListener{

    private static final String CACHE_KEY_PREFIX = "villageServicelist_";

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
            mCatalog = AppContext.getInstance().getLoginUid();
            super.requestData(refresh);
        } else {
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
            mErrorLayout.setErrorMessage(getString(R.string.unlogin_tip));
        }
    }

//  重载该方法，定义子类自己的cachekey
    @Override
    protected String getCacheKeyPrefix() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String str = bundle.getString("topic");
            if (str != null) {
                return str;
            }
        }
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
        Bundle bundle = getArguments();
        if (bundle != null) {
//            如果需要做搜索功能，可以通过bundle传人参数，进行带参数的请求
        }
//        EasyFarmServerApi.getTweetList(mCatalog, mCurrentPage, mHandler);
//        start-模拟问答数据
        List<VillageService> data = new ArrayList<VillageService>();
        VillageService m1 = new VillageService();
        m1.setId(2012);
        m1.setBusinessArea("广东-广州-从化");
        m1.setBusinessAddress("陈家村");
        m1.setApplyDate("2016-09-12");
        m1.setBusinessReason("下乡考察");
        m1.setBusinessDate("2016-9-1");
        m1.setReturnDate("2016-9-18");
        data.add(m1);
        VillageService m2 = new VillageService();
        m2.setId(2013);
        m2.setBusinessArea("广东-广州-从化");
        m2.setBusinessAddress("陈家村");
        m2.setApplyDate("2016-09-12");
        m2.setBusinessReason("下乡考察");
        m2.setBusinessDate("2016-9-1");
        m2.setReturnDate("2016-9-18");
        data.add(m2);
        VillageService m3 = new VillageService();
        m3.setId(2014);
        m3.setBusinessArea("广东-广州-从化");
        m3.setBusinessAddress("陈家村");
        m3.setApplyDate("2016-09-12");
        m3.setBusinessReason("下乡考察");
        m3.setBusinessDate("2016-9-1");
        m3.setReturnDate("2016-9-18");
        data.add(m3);
        executeOnLoadDataSuccess(data);
//        end-模拟问答数据
    }

//  重载点击事件，自定义子类的点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        VillageService villageService = mAdapter.getItem(position);
        if (villageService != null) {
            UIHelper.showVillageServiceDetail(view.getContext(), null, villageService.getId());
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
                if (mCatalog > 0) {
                    if (AppContext.getInstance().isLogin()) {
                        mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                        requestData(true);
                    } else {
                        UIHelper.showLoginActivity(getActivity());
                    }
                } else {
                    mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                    requestData(true);
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
        items = new String[] {getResources().getString(R.string.delete) };

        DialogHelp.getSelectDialog(getActivity(), items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    handleDeleteTweet(villageService);
                }
            }
        }).show();
    }

//  删除申请
    private void handleDeleteTweet(final VillageService villageService) {
        DialogHelp.getConfirmDialog(getActivity(), "是否删除该申请?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                EasyFarmServerApi.deleteTweet(tweet.getAuthorid(), tweet
//                        .getId(), new DeleteTweetResponseHandler(tweet));
            }
        }).show();
    }

//  问答删除句柄
    class DeleteTweetResponseHandler extends OperationResponseHandler {

        DeleteTweetResponseHandler(Object... args) {
            super(args);
        }

        @Override
        public void onSuccess(int code, ByteArrayInputStream is, Object[] args)
                throws Exception {
            try {
                Result res = JsonUtils.toBean(ResultBean.class, is).getResult();
//              更新列表
                if (res != null && res.OK()) {
                    AppContext.showToastShort(R.string.delete_success);
                    Tweet tweet = (Tweet) args[0];
                    mAdapter.removeItem(tweet);
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
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            AppContext.showToastShort(R.string.delete_faile);
        }
    }

    @Override
    protected long getAutoRefreshTime() {
        // 最新问答3分钟刷新一次
        if (mCatalog == TweetsList.CATALOG_LATEST) {
            return 3 * 60;
        }
        return super.getAutoRefreshTime();
    }

}
