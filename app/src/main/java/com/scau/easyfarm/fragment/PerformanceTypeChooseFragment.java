package com.scau.easyfarm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.scau.easyfarm.R;
import com.scau.easyfarm.adapter.PerformanceTypeListAdapter;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.base.ListBaseAdapter;
import com.scau.easyfarm.bean.Entity;
import com.scau.easyfarm.bean.PerformanceType;
import com.scau.easyfarm.bean.PerformanceTypeList;
import com.scau.easyfarm.bean.VillageServiceReason;
import com.scau.easyfarm.ui.empty.EmptyLayout;
import com.scau.easyfarm.util.JsonUtils;

import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * Created by chenhehong on 2016/9/8.
 */
public class PerformanceTypeChooseFragment extends BaseFragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener{

    public static final int REQUEST_CODE_PERFORMANCETYPE_SELECT = 112;
    public static final String BUNDLE_SELECT_TYPE_STR = "bundle_select_type_str";
    public static final String BUNDLE_SELECT_TYPE_ID = "bundle_select_type_id";
    public static final String BUNDLE_SELECT_TYPE_FILE_TYPE_LIST = "bundle_select_type_file_type_list";
    public static final String BUNDLE_SELECT_WORKUNIT = "bundle_select_workunit";

    private static EmptyLayout mEmptyView;
    private PerformanceTypeListAdapter performanceTypeAdapter;
    private ListView mListView;
    private static int mState = STATE_NONE;
    //  后面以currenPage==0判断为第一次加载数据列表
    private int currenPage;
    private int pageSize=20;

    private OperationResponseHandler mHandler = new OperationResponseHandler() {

        @Override
        public void onSuccess(int code, ByteArrayInputStream is, Object[] args){
            try {
                PerformanceTypeList list = JsonUtils.toBean(PerformanceTypeList.class,is);
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
        View view = inflater.inflate(R.layout.fragment_performance_type_list, container,false);
        initView(view);
        return view;
    }

    @Override
    public void initView(View view) {
        mEmptyView = (EmptyLayout) view.findViewById(R.id.error_layout);
        mListView = (ListView) view.findViewById(R.id.lv_type);
        mListView.setOnItemClickListener(itemOnItemClick);
        if (performanceTypeAdapter ==null){
            performanceTypeAdapter = new PerformanceTypeListAdapter();
        }
        mListView.setAdapter(performanceTypeAdapter);

        mEmptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestExpertData();
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        sendRequestExpertData();
    }

    private void executeOnLoadDataSuccess(List<PerformanceType> data) {
        if (data == null) {
            return;
        }
        mEmptyView.setErrorType(EmptyLayout.HIDE_LAYOUT);

        for (int i = 0; i < data.size(); i++) {
            if (compareTo(performanceTypeAdapter.getData(), data.get(i))) {
                data.remove(i);
            }
        }
        int adapterState = ListBaseAdapter.STATE_EMPTY_ITEM;
        if (performanceTypeAdapter.getCount() == 0 && mState == STATE_NONE) {
            mEmptyView.setErrorType(EmptyLayout.NODATA);
        } else if (data.size() == 0 || (data.size() < 20 && currenPage == 0)) {
            adapterState = ListBaseAdapter.STATE_NO_MORE;
        } else {
            adapterState = ListBaseAdapter.STATE_LOAD_MORE;
        }
        performanceTypeAdapter.setState(adapterState);
        performanceTypeAdapter.addData(data);
    }

    private boolean compareTo(List<? extends Entity> data, PerformanceType enity) {
        int s = data.size();
        if (enity != null) {
            for (int i = 0; i < s; i++) {
                if (enity.getId()==((PerformanceType)data.get(i)).getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void sendRequestExpertData() {
        mState = STATE_REFRESH;
        mEmptyView.setErrorType(EmptyLayout.NETWORK_LOADING);
        EasyFarmServerApi.getPerformanceTypeList(currenPage, pageSize, mHandler);
    }

    private AdapterView.OnItemClickListener itemOnItemClick = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            PerformanceType selectedReason = (PerformanceType) performanceTypeAdapter.getItem(position);
            if (selectedReason==null) return;
            Intent result = new Intent();
            result.putExtra(BUNDLE_SELECT_TYPE_STR, selectedReason.getName());
            result.putExtra(BUNDLE_SELECT_TYPE_ID,selectedReason.getId());
            result.putExtra(BUNDLE_SELECT_TYPE_FILE_TYPE_LIST,selectedReason.getFileTypeArrayList());
            result.putExtra(BUNDLE_SELECT_WORKUNIT,selectedReason.getUnit());
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
        if (performanceTypeAdapter != null
                && performanceTypeAdapter.getDataSize() > 0
                && mListView.getLastVisiblePosition() == (mListView
                .getCount() - 1)) {
            if (mState == STATE_NONE
                    && performanceTypeAdapter.getState() == ListBaseAdapter.STATE_LOAD_MORE) {
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
