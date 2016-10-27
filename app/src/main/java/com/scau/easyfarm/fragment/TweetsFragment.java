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
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseListFragment;
import com.scau.easyfarm.bean.Constants;
import com.scau.easyfarm.bean.MyInformation;
import com.scau.easyfarm.bean.Result;
import com.scau.easyfarm.bean.ResultBean;
import com.scau.easyfarm.bean.Tweet;
import com.scau.easyfarm.bean.TweetsList;
import com.scau.easyfarm.ui.empty.EmptyLayout;
import com.scau.easyfarm.util.DialogHelp;
import com.scau.easyfarm.util.HTMLUtil;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.StringUtils;
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
public class TweetsFragment extends BaseListFragment<Tweet> implements
        AdapterView.OnItemLongClickListener{

    private static final String CACHE_KEY_PREFIX = "tweetslist_";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//      如果是有用户个人的栏目的，要监听用户登录状态的广播
        if (mCatalog ==TweetsList.CATALOG_ME) {
            IntentFilter filter = new IntentFilter(
                    Constants.INTENT_ACTION_USER_CHANGE);
            filter.addAction(Constants.INTENT_ACTION_LOGOUT);
            getActivity().registerReceiver(mReceiver, filter);
        }
    }

    @Override
    public void onDestroy() {
        if (mCatalog ==TweetsList.CATALOG_ME) {
            getActivity().unregisterReceiver(mReceiver);
        }
        super.onDestroy();
    }

    @Override
    protected TweetAdapter getListAdapter() {
        return new TweetAdapter();
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
            mCatalog = TweetsList.CATALOG_ME;
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
            mErrorLayout.setErrorMessage(getString(R.string.unlogin_tip));
        }
    }

    @Override
    protected void requestData(boolean refresh) {
//      如果是用户个人栏目”我的问答“，要判断是否登录了
//      获取数据直接引用父类的requestData方法即可，该方法会进行缓存的保存和读取，没有缓存时调用sendRequestData方法获取数据，子类实现sendRequestData方法即可
        if (mCatalog ==TweetsList.CATALOG_ME) {
            if (AppContext.getInstance().isLogin()) {
                super.requestData(refresh);
            } else {
                mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                mErrorLayout.setErrorMessage(getString(R.string.unlogin_tip));
            }
        } else {
            super.requestData(refresh);
        }
    }

//  重载该方法，定义子类自己的cachekey
    @Override
    protected String getCacheKeyPrefix() {
        if (mCatalog==TweetsList.CATALOG_ME){
            return AppContext.getInstance().getLoginUid()+"_"+CACHE_KEY_PREFIX + mCatalog;
        }
        return CACHE_KEY_PREFIX + mCatalog;
    }

//  重载该方法，对服务器返回的数据进行解析
    @Override
    protected TweetsList parseList(InputStream is) throws Exception {
        TweetsList list = JsonUtils.toBean(TweetsList.class,is);
        return list;
    }

//  用于从缓存中读出序列化数据
    @Override
    protected TweetsList readList(Serializable seri) {
        return ((TweetsList) seri);
    }


    @Override
    protected void sendRequestData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
//            如果需要做搜索功能，可以通过bundle传人参数，进行带参数的请求
        }
        if (mCatalog==TweetsList.CATALOG_ME){
            EasyFarmServerApi.getMyTweetList(mCurrentPage, mHandler);
        }else {
            EasyFarmServerApi.getTweetList(mCurrentPage, mHandler);
        }
//        start-模拟问答数据
//        List<Tweet> data = new ArrayList<Tweet>();
//        Tweet a1 = new Tweet();
//        a1.setAuthor("姚忠良 ");
//        a1.setTitle("苦瓜籽栽培用什么除草剂比较安全可靠，不求全部杂草，只有能解决大部分杂草？");
//        a1.setCreateDate("2016-07-05 11:47:42");
//        a1.setCommentCount("10");
//        a1.setId(2011);
//        data.add(a1);
//        Tweet a2 = new Tweet();
//        a2.setAuthor("萧山区瓜沥镇联络站(凌小丹) ");
//        a2.setTitle("为什么玉米采摘下来瘌头比较多");
//        a2.setCreateDate("2016-07-05 11:47:42");
//        a2.setCommentCount("10");
//        a2.setId(2012);
//        a2.setImgBig("http://imgsrc.baidu.com/forum/w%3D580%3Bcp%3Dtieba%2C10%2C480%3Bap%3D%D1%D6%C2%F3%B0%C9%2C90%2C488/sign=6f74d930d439b6004dce0fbfd96b565a/fbec2201213fb80e0156bb0437d12f2eb8389460.jpg");
//        a2.setImgSmall("http://img2.imgtn.bdimg.com/it/u=3231829510,729497164&fm=21&gp=0.jpg");
//        data.add(a2);
//        Tweet a3 = new Tweet();
//        a3.setAuthor("凌小丹 ");
//        a3.setTitle("您好,我想咨询下猕猴桃树为什么只开花不结果?");
//        a3.setCreateDate("2016-07-05 11:47:42");
//        a3.setCommentCount("10");
//        a3.setId(2013);
//        data.add(a3);
//        executeOnLoadDataSuccess(data);
//        end-模拟问答数据
    }

//  重载点击事件，自定义子类的点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Tweet tweet = mAdapter.getItem(position);
        if (tweet != null) {
            UIHelper.showTweetDetail(view.getContext(), null, tweet.getId());
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
                if (mCatalog ==TweetsList.CATALOG_ME) {
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
        Tweet tweet = mAdapter.getItem(position);
        if (tweet != null) {
            handleLongClick(tweet);
            return true;
        }
        return false;
    }

    private void handleLongClick(final Tweet tweet) {
        String[] items = null;
        if (AppContext.getInstance().getLoginUid() == tweet.getAuthorid()) {
            items = new String[] { getResources().getString(R.string.copy),
                    getResources().getString(R.string.delete) };
        } else {
            items = new String[] { getResources().getString(R.string.copy) };
        }

        DialogHelp.getSelectDialog(getActivity(), items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    TDevice.copyTextToBoard(HTMLUtil.delHTMLTag(tweet.getTitle()));
                } else if (i == 1) {
                    handleDeleteTweet(tweet);
                }
            }
        }).show();
    }

//  删除问答
    private void handleDeleteTweet(final Tweet tweet) {
        DialogHelp.getConfirmDialog(getActivity(), "是否删除该问答?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EasyFarmServerApi.deleteTweet(tweet.getAuthorid(), tweet
                        .getId(), new DeleteTweetResponseHandler(tweet));
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
        public void onFailure(int code, String errorMessage, Object[] args) {
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
