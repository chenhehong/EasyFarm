package com.scau.easyfarm.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;


import com.scau.easyfarm.adapter.ManualAdapter;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseListFragment;
import com.scau.easyfarm.bean.ManualContent;
import com.scau.easyfarm.bean.ManualList;
import com.scau.easyfarm.ui.empty.EmptyLayout;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.UIHelper;

import java.io.InputStream;
import java.io.Serializable;

public class SearchManulFragment extends BaseListFragment<ManualContent> {
    protected static final String TAG = SearchManulFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "search_manual_list_";
    private String mCatalog;
    private String mSearch;
    private boolean mRquestDataIfCreated = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mCatalog = args.getString(BUNDLE_KEY_CATALOG);
        }
        int mode = WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
        getActivity().getWindow().setSoftInputMode(mode);
    }

    public void search(String search) {
        mSearch = search;
        if (mErrorLayout != null) {
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
            mState = STATE_REFRESH;
            requestData(true);
        } else {
            mRquestDataIfCreated = true;
        }
    }

    @Override
    protected boolean requestDataIfViewCreated() {
        return mRquestDataIfCreated;
    }

    @Override
    protected ManualAdapter getListAdapter() {
        return new ManualAdapter();
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mCatalog + mSearch;
    }

    @Override
    protected ManualList parseList(InputStream is) throws Exception {
        ManualList list = JsonUtils.toBean(ManualList.class, is);
        return list;
    }

    @Override
    protected ManualList readList(Serializable seri) {
        return ((ManualList) seri);
    }

    @Override
    protected void sendRequestData() {
        EasyFarmServerApi.getManualSearchList(mCatalog, mSearch, mCurrentPage, mHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        ManualContent res = mAdapter.getItem(position);
        if (res != null) {
            UIHelper.showManualContentDetail(view.getContext(), res.getId());
        }
    }
}
