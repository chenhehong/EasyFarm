package com.scau.easyfarm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.scau.easyfarm.R;
import com.scau.easyfarm.adapter.ExpertListAdapter;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.base.ListBaseAdapter;
import com.scau.easyfarm.bean.Entity;
import com.scau.easyfarm.bean.UserList;
import com.scau.easyfarm.bean.User;
import com.scau.easyfarm.bean.UserList;
import com.scau.easyfarm.ui.empty.EmptyLayout;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.UIHelper;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by chenhehong on 2016/9/8.
 */
public class TweetExpertChooseFragment extends BaseFragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener{

    public static final int TWEET_EXPERT_CHOOSE_REQUEST_CODE = 101;
    public static final String TWEET_EXPERT_MANUAL_TYPE = "tweet_expert_manual_type";
    public static final String SELECT_TWEET_EXPERT_ID = "select_tweet_expert_id";
    public static final String SELECT_TWEET_EXPERT_NAME = "select_tweet_expert_name";

    private int typeId;

    private static EmptyLayout mEmptyView;
    private ExpertListAdapter expertListAdapter;
    private ListView expertListView;
    private static int mState = STATE_NONE;
    //  后面以currenPage==0判断为第一次加载数据列表
    private int currenPage;
    private int pageSize=20;

    private OperationResponseHandler mHandler = new OperationResponseHandler() {

        @Override
        public void onSuccess(int code, ByteArrayInputStream is, Object[] args){
            try {
                UserList list = JsonUtils.toBean(UserList.class,is);
                executeOnLoadDataSuccess(list.getUserList());
            } catch (Exception e) {
                e.printStackTrace();
                onFailure(code, e.getMessage(),args);
            }
        }

        @Override
        public void onFailure(int code, String errorMessage, Object[] args) {
            mEmptyView.setErrorType(EmptyLayout.NETWORK_ERROR);
        }

        public void onFinish() {
            mState = STATE_NONE;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expert_list, container,false);
        initView(view);
        return view;
    }

    @Override
    public void initView(View view) {
        mEmptyView = (EmptyLayout) view.findViewById(R.id.error_layout);
        expertListView = (ListView) view.findViewById(R.id.lv_expert);
        expertListView.setOnItemClickListener(expertOnItemClick);
        if (expertListAdapter==null){
            expertListAdapter = new ExpertListAdapter();
        }
        expertListView.setAdapter(expertListAdapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle!=null){
            typeId = bundle.getInt(TWEET_EXPERT_MANUAL_TYPE);
        }
        sendRequestExpertData();
    }

    private void executeOnLoadDataSuccess(List<User> data) {
        if (data == null) {
            return;
        }
        mEmptyView.setErrorType(EmptyLayout.HIDE_LAYOUT);

        for (int i = 0; i < data.size(); i++) {
            if (compareTo(expertListAdapter.getData(), data.get(i))) {
                data.remove(i);
            }
        }
        int adapterState = ListBaseAdapter.STATE_EMPTY_ITEM;
        if (expertListAdapter.getCount() == 0 && mState == STATE_NONE) {
            mEmptyView.setErrorType(EmptyLayout.NODATA);
        } else if (data.size() == 0 || (data.size() < 20 && currenPage == 0)) {
            adapterState = ListBaseAdapter.STATE_NO_MORE;
        } else {
            adapterState = ListBaseAdapter.STATE_LOAD_MORE;
        }
        expertListAdapter.setState(adapterState);
        expertListAdapter.addData(data);
    }

    private boolean compareTo(List<? extends Entity> data, User enity) {
        int s = data.size();
        if (enity != null) {
            for (int i = 0; i < s; i++) {
                if (enity.getId()==((User)data.get(i)).getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void sendRequestExpertData() {
        mState = STATE_REFRESH;
        mEmptyView.setErrorType(EmptyLayout.NETWORK_LOADING);
        EasyFarmServerApi.getExpertList(typeId, currenPage, pageSize, mHandler);
//      测试start
//        User m1 = new User();
//        m1.setRealName("陈河宏");
//        m1.setId(2012);
//        User m2 = new User();
//        m2.setRealName("李林");
//        m2.setId(2013);
//        User m3 = new User();
//        m3.setRealName("李刚");
//        m3.setId(2014);
//        User m4 = new User();
//        m4.setRealName("刘备");
//        m4.setId(2015);
//        User m5 = new User();
//        m5.setRealName("项羽");
//        m5.setId(2016);
//        User m6 = new User();
//        m6.setRealName("孙权");
//        m6.setId(2017);
//        List<User> mList = new ArrayList<User>();
//        mList.add(m1);mList.add(m2);mList.add(m3);mList.add(m4);mList.add(m5);mList.add(m6);
//        executeOnLoadDataSuccess(mList);
//      测试end
    }

    private AdapterView.OnItemClickListener expertOnItemClick = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            User selectedUser = (User)expertListAdapter.getItem(position);
            if (selectedUser==null) return;
            Intent result = new Intent();
            result.putExtra(SELECT_TWEET_EXPERT_ID, selectedUser.getId());
            result.putExtra(SELECT_TWEET_EXPERT_NAME, selectedUser.getRealName());
            getActivity().setResult(getActivity().RESULT_OK, result);
            getActivity().finish();
        }
    };

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
        if (expertListAdapter != null
                && expertListAdapter.getDataSize() > 0
                && expertListView.getLastVisiblePosition() == (expertListView
                .getCount() - 1)) {
            if (mState == STATE_NONE
                    && expertListAdapter.getState() == ListBaseAdapter.STATE_LOAD_MORE) {
                mState = STATE_LOADMORE;
                currenPage++;
                sendRequestExpertData();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
