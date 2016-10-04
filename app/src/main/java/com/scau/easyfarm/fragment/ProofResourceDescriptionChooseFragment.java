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
import com.scau.easyfarm.adapter.ProofResourceDescriptionAdapter;
import com.scau.easyfarm.adapter.VillageServiceReasonListAdapter;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.base.ListBaseAdapter;
import com.scau.easyfarm.bean.Entity;
import com.scau.easyfarm.bean.ProofResourceDescription;
import com.scau.easyfarm.bean.ProofResourceDescriptionList;
import com.scau.easyfarm.bean.VillageServiceReason;
import com.scau.easyfarm.ui.empty.EmptyLayout;
import com.scau.easyfarm.util.JsonUtils;

import java.io.ByteArrayInputStream;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by chenhehong on 2016/9/8.
 */
public class ProofResourceDescriptionChooseFragment extends BaseFragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener{

    public static final int REQUEST_CODE_DESCRIPTION_SELECT = 113;
    public static final String BUNDLE_SELECT_DESCRIPTION_STR = "bundle_select_description_str";
    public static final String BUNDLE_SELECT_DESCRIPTION_ID = "bundle_select_description_id";

    private static EmptyLayout mEmptyView;
    private ProofResourceDescriptionAdapter proofResourceDescriptionListAdapter;
    private ListView proofResourceDescriptionListView;
    private static int mState = STATE_NONE;
    //  后面以currenPage==0判断为第一次加载数据列表
    private int currenPage;
    private int pageSize=20;

    private OperationResponseHandler mHandler = new OperationResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBytes) {
            try {
                ProofResourceDescriptionList list = JsonUtils.toBean(ProofResourceDescriptionList.class,
                        new ByteArrayInputStream(responseBytes));
                executeOnLoadDataSuccess(list.getProofResourceDescriptionList());
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_proof_resource_description_list, container,false);
        initView(view);
        return view;
    }

    @Override
    public void initView(View view) {
        mEmptyView = (EmptyLayout) view.findViewById(R.id.error_layout);
        proofResourceDescriptionListView = (ListView) view.findViewById(R.id.lv_description);
        proofResourceDescriptionListView.setOnItemClickListener(descriptionOnItemClick);
        if (proofResourceDescriptionListAdapter ==null){
            proofResourceDescriptionListAdapter = new ProofResourceDescriptionAdapter();
        }
        proofResourceDescriptionListView.setAdapter(proofResourceDescriptionListAdapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        sendRequestExpertData();
    }

    private void executeOnLoadDataSuccess(List<ProofResourceDescription> data) {
        if (data == null) {
            return;
        }
        mEmptyView.setErrorType(EmptyLayout.HIDE_LAYOUT);

        for (int i = 0; i < data.size(); i++) {
            if (compareTo(proofResourceDescriptionListAdapter.getData(), data.get(i))) {
                data.remove(i);
            }
        }
        int adapterState = ListBaseAdapter.STATE_EMPTY_ITEM;
        if (proofResourceDescriptionListAdapter.getCount() == 0 && mState == STATE_NONE) {
            mEmptyView.setErrorType(EmptyLayout.NODATA);
        } else if (data.size() == 0 || (data.size() < 20 && currenPage == 0)) {
            adapterState = ListBaseAdapter.STATE_NO_MORE;
        } else {
            adapterState = ListBaseAdapter.STATE_LOAD_MORE;
        }
        proofResourceDescriptionListAdapter.setState(adapterState);
        proofResourceDescriptionListAdapter.addData(data);
    }

    private boolean compareTo(List<? extends Entity> data, ProofResourceDescription enity) {
        int s = data.size();
        if (enity != null) {
            for (int i = 0; i < s; i++) {
                if (enity.getId()==((ProofResourceDescription)data.get(i)).getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void sendRequestExpertData() {
        mState = STATE_REFRESH;
        mEmptyView.setErrorType(EmptyLayout.NETWORK_LOADING);
        EasyFarmServerApi.getProofResourceDescriptionList(currenPage, pageSize, mHandler);
    }

    private AdapterView.OnItemClickListener descriptionOnItemClick = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            ProofResourceDescription selectedDescription = (ProofResourceDescription) proofResourceDescriptionListAdapter.getItem(position);
            if (selectedDescription==null) return;
            Intent result = new Intent();
            result.putExtra(BUNDLE_SELECT_DESCRIPTION_STR, selectedDescription.getName());
            result.putExtra(BUNDLE_SELECT_DESCRIPTION_ID, selectedDescription.getId());
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
        if (proofResourceDescriptionListAdapter != null
                && proofResourceDescriptionListAdapter.getDataSize() > 0
                && proofResourceDescriptionListView.getLastVisiblePosition() == (proofResourceDescriptionListView
                .getCount() - 1)) {
            if (mState == STATE_NONE
                    && proofResourceDescriptionListAdapter.getState() == ListBaseAdapter.STATE_LOAD_MORE) {
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
