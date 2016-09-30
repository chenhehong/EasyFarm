package com.scau.easyfarm.base;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;
import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.R;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.bean.Result;
import com.scau.easyfarm.bean.ResultBean;
import com.scau.easyfarm.cache.CacheManager;
import com.scau.easyfarm.emoji.OnSendClickListener;
import com.scau.easyfarm.ui.DetailActivity;
import com.scau.easyfarm.ui.empty.EmptyLayout;
import com.scau.easyfarm.util.DialogHelp;
import com.scau.easyfarm.util.FontSizeUtils;
import com.scau.easyfarm.util.HTMLUtil;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.TDevice;
import com.scau.easyfarm.util.UIHelper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 通用的详情fragment
 * 加载数据并初步处理数据样式，缓存添加与读取。后期可以根据需求添加收藏、提示登录，举报，分享，评论之类的业务相关流程
 */
public abstract class CommonDetailFragment<T extends Serializable> extends BaseFragment{

    protected int mId;

    protected EmptyLayout mEmptyLayout;

    protected WebView mWebView;

    protected T mDetail;

    private AsyncTask<String, Void, T> mCacheTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news_detail;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container,
                false);
        mId = getActivity().getIntent().getIntExtra("id", 0);
        ButterKnife.inject(this, view);
        initView(view);
        initData();
        requestData(false);
        return view;
    }

    @Override
    public void initView(View view) {
        mEmptyLayout = (EmptyLayout) view.findViewById(R.id.error_layout);
        mWebView = (WebView) view.findViewById(R.id.webview);
        UIHelper.initWebView(mWebView);
    }

    private void requestData(boolean refresh) {
        String key = getCacheKey();
        if (TDevice.hasInternet()
                && (!CacheManager.isExistDataCache(getActivity(), key) || refresh)) {
            sendRequestDataForNet();
        } else {
            readCacheData(key);
        }
    }

    @Override
    public void onDestroyView() {
        recycleWebView();
        super.onDestroyView();
    }

    private void recycleWebView() {
        if (mWebView != null) {
            mWebView.setVisibility(View.GONE);
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
    }

    private void readCacheData(String cacheKey) {
        cancelReadCache();
        mCacheTask = new CacheTask(getActivity()).execute(cacheKey);
    }

    private void cancelReadCache() {
        if (mCacheTask != null) {
            mCacheTask.cancel(true);
            mCacheTask = null;
        }
    }

    protected AsyncHttpResponseHandler mDetailHeandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
            try {
                T detail = parseData(new ByteArrayInputStream(arg2));
                if (detail != null) {
                    mEmptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                    executeOnLoadDataSuccess(detail);
                    saveCache(detail);
                } else {
                    executeOnLoadDataError();
                }
            } catch (Exception e) {
                e.printStackTrace();
                executeOnLoadDataError();
            }
        }

        @Override
        public void onStart() {
            super.onStart();
            mEmptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        }

        @Override
        public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                              Throwable arg3) {
            readCacheData(getCacheKey());
        }
    };

    private class CacheTask extends AsyncTask<String, Void, T> {
        private final WeakReference<Context> mContext;

        private CacheTask(Context context) {
            mContext = new WeakReference<Context>(context);
        }

        @Override
        protected T doInBackground(String... params) {
            if (mContext.get() != null) {
                Serializable seri = CacheManager.readObject(mContext.get(),
                        params[0]);
                if (seri == null) {
                    return null;
                } else {
                    return (T)seri;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(T detail) {
            super.onPostExecute(detail);
            mEmptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            if (detail != null) {
                executeOnLoadDataSuccess(detail);
            } else {
                executeOnLoadDataError();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mEmptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        }
    }

    protected void executeOnLoadDataSuccess(T detail) {
        this.mDetail = detail;
        if (this.mDetail == null || TextUtils.isEmpty(this.getWebViewBody(detail))) {
            executeOnLoadDataError();
            return;
        }

        mWebView.loadDataWithBaseURL("", this.getWebViewBody(detail), "text/html", "UTF-8", "");
        // 显示存储的字体大小
        mWebView.loadUrl(FontSizeUtils.getSaveFontSize());
        boolean favoriteState = getFavoriteState() == 1;
        setFavoriteState(favoriteState);

    }

    protected void executeOnLoadDataError() {
        mEmptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
        mEmptyLayout.setOnLayoutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mState = STATE_REFRESH;
                mEmptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                requestData(true);
            }
        });
    }

    protected void saveCache(T detail) {
        new SaveCacheTask(getActivity(), detail, getCacheKey()).execute();
    }

    private class SaveCacheTask extends AsyncTask<Void, Void, Void> {
        private final WeakReference<Context> mContext;
        private final Serializable seri;
        private final String key;

        private SaveCacheTask(Context context, Serializable seri, String key) {
            mContext = new WeakReference<Context>(context);
            this.seri = seri;
            this.key = key;
        }

        @Override
        protected Void doInBackground(Void... params) {
            CacheManager.saveObject(mContext.get(), seri, key);
            return null;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.common_detail_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    int i = 0;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                sendRequestDataForNet();
                return false;
            case R.id.font_size:
                showChangeFontSize();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    AlertDialog fontSizeChange;

    private void showChangeFontSize() {

        final String[] items = getResources().getStringArray(
                R.array.font_size);
        fontSizeChange = DialogHelp.getSingleChoiceDialog(getActivity(), items, FontSizeUtils.getSaveFontSizeIndex(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 更改字体大小
                FontSizeUtils.saveFontSize(i);
                mWebView.loadUrl(FontSizeUtils.getFontSize(i));
                fontSizeChange.dismiss();
            }
        }).show();
    }

    // 收藏或者取消收藏
    public void handleFavoriteOrNot() {
        if (mDetail == null) {
            return;
        }
        if (!TDevice.hasInternet()) {
            AppContext.showToastShort(R.string.tip_no_internet);
            return;
        }
        if (!AppContext.getInstance().isLogin()) {
            UIHelper.showLoginActivity(getActivity());
            return;
        }
        int uid = AppContext.getInstance().getLoginUid();
        final boolean isFavorited = getFavoriteState() == 1 ? true : false;
        AsyncHttpResponseHandler mFavoriteHandler = new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                try {
                    Result res = JsonUtils.toBean(ResultBean.class,
                            new ByteArrayInputStream(arg2)).getResult();
                    if (res.OK()) {
                        AppContext.showToast(res.getErrorMessage());
                        boolean newFavorited = !isFavorited;
                        setFavoriteState(newFavorited);
                        // 更新收藏的状态
                        updateFavoriteChanged(!newFavorited ? 0 : 1);
                    } else {
                        onFailure(arg0, arg1, arg2, null);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    onFailure(arg0, arg1, arg2, e);
                }
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                AppContext.showToastShort("收藏失败");
            }

            @Override
            public void onStart() {
                super.onStart();
                showWaitDialog("请稍候...");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideWaitDialog();
            }
        };

//      收藏状态改变的网络接口，需要时可以添加
        if (isFavorited) {
//            EasyFarmServerApi.delFavorite(uid, mId,
//                    getFavoriteTargetType(), mFavoriteHandler);
        } else {
//            EasyFarmServerApi.addFavorite(uid, mId,
//                    getFavoriteTargetType(), mFavoriteHandler);
        }
    }

//  ui上改变收藏的状态，需要时可以实现
    private void setFavoriteState(boolean isFavorited) {

    }
    /***
     * 获取去除html标签的body
     *
     * @param body
     * @return
     */
    protected String getFilterHtmlBody(String body) {
        if (body == null)
            return "";
        return HTMLUtil.delHTMLTag(body.trim());
    }

    // 获取缓存的key
    protected abstract String getCacheKey();
    // 从网络中读取数据
    protected abstract void sendRequestDataForNet();
    // 解析数据
    protected abstract T parseData(InputStream is);
    // 返回填充到webview中的内容
    protected abstract String getWebViewBody(T detail);
    // 显示评论列表

//    需要用收藏功能的时候可以实现这两个方法
    protected abstract int getFavoriteState();
    protected abstract void updateFavoriteChanged(int newFavoritedState);
}
