package com.scau.easyfarm.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.scau.easyfarm.R;
import com.scau.easyfarm.adapter.SimpleTextListAdapter;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.bean.Entity;
import com.scau.easyfarm.bean.SimpleText;
import com.scau.easyfarm.bean.SimpleTextList;
import com.scau.easyfarm.ui.empty.EmptyLayout;
import com.scau.easyfarm.util.JsonUtils;

import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * Created by chenhehong on 2016/9/8.
 * 通用的文本列表选择frame，其他类可以直接继承使用
 */
public abstract class BaseSimpleTextChooseFragment extends BaseFragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener{

    public static final String BUNDLE_SELECT_TEXT_STR = "bundle_select_text_str";
    public static final String BUNDLE_SELECT_TEXT_ID = "bundle_select_text_id";

    private static EmptyLayout mEmptyView;
    private SimpleTextListAdapter simpleTextAdapter;
    private ListView mListView;
    private static int mState = STATE_NONE;
    //  后面以currenPage==0判断为第一次加载数据列表
    private int currenPage;
    private int pageSize=20;

    private OperationResponseHandler mHandler = new OperationResponseHandler() {

        @Override
        public void onSuccess(int code, ByteArrayInputStream is, Object[] args){
            try {
                SimpleTextList list = JsonUtils.toBean(SimpleTextList.class,is);
                executeOnLoadDataSuccess(list.getList());
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
        View view = inflater.inflate(R.layout.fragment_simple_textchoose_list, container,false);
        initView(view);
        return view;
    }

    @Override
    public void initView(View view) {
        mEmptyView = (EmptyLayout) view.findViewById(R.id.error_layout);
        mListView = (ListView) view.findViewById(R.id.lv_type);
        mListView.setOnItemClickListener(itemOnItemClick);
        if (simpleTextAdapter ==null){
            simpleTextAdapter = new SimpleTextListAdapter();
        }
        mListView.setAdapter(simpleTextAdapter);

        mEmptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestData();
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        sendRequestData();
    }

    private void executeOnLoadDataSuccess(List<SimpleText> data) {
        if (data == null) {
            return;
        }
        mEmptyView.setErrorType(EmptyLayout.HIDE_LAYOUT);

        for (int i = 0; i < data.size(); i++) {
            if (compareTo(simpleTextAdapter.getData(), data.get(i))) {
                data.remove(i);
            }
        }
        int adapterState = ListBaseAdapter.STATE_EMPTY_ITEM;
        if (simpleTextAdapter.getCount() == 0 && mState == STATE_NONE) {
            mEmptyView.setErrorType(EmptyLayout.NODATA);
        } else if (data.size() == 0 || (data.size() < 20 && currenPage == 0)) {
            adapterState = ListBaseAdapter.STATE_NO_MORE;
        } else {
            adapterState = ListBaseAdapter.STATE_LOAD_MORE;
        }
        simpleTextAdapter.setState(adapterState);
        simpleTextAdapter.addData(data);
    }

    private boolean compareTo(List<? extends Entity> data, SimpleText enity) {
        int s = data.size();
        if (enity != null) {
            for (int i = 0; i < s; i++) {
                if (enity.getId()==((SimpleText)data.get(i)).getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void sendRequestData() {
        mState = STATE_REFRESH;
        mEmptyView.setErrorType(EmptyLayout.NETWORK_LOADING);
        sendRequest(currenPage,pageSize,mHandler);
    }

    public abstract  void sendRequest(int currenPage,int pageSize,OperationResponseHandler mHandler);

    private AdapterView.OnItemClickListener itemOnItemClick = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            SimpleText selectedText = (SimpleText) simpleTextAdapter.getItem(position);
            if (selectedText==null) return;
            Intent result = new Intent();
            result.putExtra(BUNDLE_SELECT_TEXT_STR, selectedText.getText());
            result.putExtra(BUNDLE_SELECT_TEXT_ID, selectedText.getId());
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
        if (simpleTextAdapter != null
                && simpleTextAdapter.getDataSize() > 0
                && mListView.getLastVisiblePosition() == (mListView
                .getCount() - 1)) {
            if (mState == STATE_NONE
                    && simpleTextAdapter.getState() == ListBaseAdapter.STATE_LOAD_MORE) {
                mState = STATE_LOADMORE;
                currenPage++;
                sendRequestData();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
