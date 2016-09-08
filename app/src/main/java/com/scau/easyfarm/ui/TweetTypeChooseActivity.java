package com.scau.easyfarm.ui;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.scau.easyfarm.R;
import com.scau.easyfarm.adapter.ManualCategoryListAdapter;
import com.scau.easyfarm.base.BaseActivity;
import com.scau.easyfarm.base.ListBaseAdapter;
import com.scau.easyfarm.bean.Entity;
import com.scau.easyfarm.bean.ManualCategory;
import com.scau.easyfarm.bean.ManualCategoryList;
import com.scau.easyfarm.ui.empty.EmptyLayout;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.UIHelper;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by chenhehong on 2016/9/1.
 */
public class TweetTypeChooseActivity extends BaseActivity implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    protected static final int STATE_NONE = 0;
    protected static final int STATE_REFRESH = 1;
    protected static final int STATE_LOADMORE = 2;
    public static final int STATE_NOMORE = 3;

    public static final int TWEET_TYPE_CHOOSE_REQUEST_CODE = 100;

    public static final String MANUALCOTEGORYPARENT = "manual_cotegory_parent";
    public static final String SELECTED_MANUAL_COTEGORY_ID = "selected_manual_cotegory_id";
    public static final String SELECTED_MANUAL_COTEGORY_NAME = "selected_manual_cotegory_name";

    private int parentId=0;

    private static EmptyLayout mEmptyView;
    private ManualCategoryListAdapter manualCategoryListAdapter;
    private ListView manualCategoryListView;
    private static int mState = STATE_NONE;
//  后面以currenPage==0判断为第一次加载数据列表
    private int currenPage;
    private int pageSize=20;


    private AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBytes) {
            try {
                ManualCategoryList list = JsonUtils.toBean(ManualCategoryList.class,
                        new ByteArrayInputStream(responseBytes));
                executeOnLoadDataSuccess(list.getManualCategoryList());
            } catch (Exception e) {
                e.printStackTrace();
                onFailure(statusCode, headers, responseBytes, null);
            }
        }

        @Override
        public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                              Throwable arg3) {
            mEmptyView.setErrorType(EmptyLayout.NETWORK_ERROR);
        }

        public void onFinish() {
            mState = STATE_NONE;
        }
    };

    @Override
    public void initView() {
        mEmptyView = (EmptyLayout) findViewById(R.id.error_layout);
        manualCategoryListView = (ListView) findViewById(R.id.lv_manual_catalog);
        manualCategoryListView.setOnItemClickListener(manualCategoryOnItemClick);

        if (manualCategoryListAdapter==null){
            manualCategoryListAdapter = new ManualCategoryListAdapter();
        }
        manualCategoryListView.setAdapter(manualCategoryListAdapter);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        parentId = intent.getIntExtra(MANUALCOTEGORYPARENT, 0);
        sendRequestManualCatalogData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_manual_cotegory_list;
    }

    private void executeOnLoadDataSuccess(List<ManualCategory> data) {
        if (data == null) {
            return;
        }
        mEmptyView.setErrorType(EmptyLayout.HIDE_LAYOUT);

        for (int i = 0; i < data.size(); i++) {
            if (compareTo(manualCategoryListAdapter.getData(), data.get(i))) {
                data.remove(i);
            }
        }
        int adapterState = ListBaseAdapter.STATE_EMPTY_ITEM;
        if (manualCategoryListAdapter.getCount() == 0 && mState == STATE_NONE) {
            mEmptyView.setErrorType(EmptyLayout.NODATA);
        } else if (data.size() == 0 || (data.size() < 20 && currenPage == 0)) {
            adapterState = ListBaseAdapter.STATE_NO_MORE;
        } else {
            adapterState = ListBaseAdapter.STATE_LOAD_MORE;
        }
        manualCategoryListAdapter.setState(adapterState);
        manualCategoryListAdapter.addData(data);
    }

    private boolean compareTo(List<? extends Entity> data, ManualCategory enity) {
        int s = data.size();
        if (enity != null) {
            for (int i = 0; i < s; i++) {
                if (enity.getId()==((ManualCategory)data.get(i)).getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void sendRequestManualCatalogData() {
        mState = STATE_REFRESH;
        mEmptyView.setErrorType(EmptyLayout.NETWORK_LOADING);
//      EasyFarmServerApi.getManualCatalogList(parentId, currenPage,pageSize,mHandler);
//      测试start
        ManualCategory m1 = new ManualCategory();
        m1.setCategoryName("畜牧");
        m1.setIsParent(false);
        m1.setId(2012);
        ManualCategory m2 = new ManualCategory();
        m2.setCategoryName("水稻");
        m2.setIsParent(true);
        m2.setId(2013);
        List<ManualCategory> mList = new ArrayList<ManualCategory>();
        mList.add(m1);mList.add(m2);
        executeOnLoadDataSuccess(mList);
//      测试end
    }

    private AdapterView.OnItemClickListener manualCategoryOnItemClick = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            ManualCategory selectManualCategory = (ManualCategory)manualCategoryListAdapter.getItem(position);
            if (selectManualCategory==null) return;
            if (selectManualCategory.isParent()){
                UIHelper.showTweetTypeChoose(TweetTypeChooseActivity.this, selectManualCategory.getId(), TWEET_TYPE_CHOOSE_REQUEST_CODE);
            }else {
                Intent result = new Intent();
                result.putExtra(SELECTED_MANUAL_COTEGORY_ID, selectManualCategory.getId());
                result.putExtra(SELECTED_MANUAL_COTEGORY_NAME, selectManualCategory.getCategoryName());
                setResult(RESULT_OK, result);
                finish();
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)
            return;
        if (requestCode==TWEET_TYPE_CHOOSE_REQUEST_CODE){
            int selectedManualCoregoryId = data.getIntExtra(SELECTED_MANUAL_COTEGORY_ID,0);
            String selectedManualcoregoryName = data.getStringExtra(SELECTED_MANUAL_COTEGORY_NAME);
            Intent result = new Intent();
            result.putExtra(SELECTED_MANUAL_COTEGORY_ID, selectedManualCoregoryId);
            result.putExtra(SELECTED_MANUAL_COTEGORY_NAME, selectedManualcoregoryName);
            setResult(RESULT_OK, result);
            finish();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
//  列表滚动事件
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // 数据已经全部加载，或数据为空时，或正在加载，不处理滚动事件
        if (mState == STATE_NOMORE || mState == STATE_LOADMORE
                || mState == STATE_REFRESH) {
            return;
        }
        if (manualCategoryListAdapter != null
                && manualCategoryListAdapter.getDataSize() > 0
                && manualCategoryListView.getLastVisiblePosition() == (manualCategoryListView
                .getCount() - 1)) {
            if (mState == STATE_NONE
                    && manualCategoryListAdapter.getState() == ListBaseAdapter.STATE_LOAD_MORE) {
                mState = STATE_LOADMORE;
                currenPage++;
                sendRequestManualCatalogData();
            }
        }
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.tweet_type_choose;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {

    }
}
