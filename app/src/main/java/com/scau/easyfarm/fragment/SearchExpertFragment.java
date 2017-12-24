package com.scau.easyfarm.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.scau.easyfarm.R;
import com.scau.easyfarm.adapter.ExpertBaseAdapter;
import com.scau.easyfarm.adapter.FindUserAdapter;
import com.scau.easyfarm.adapter.ManualAdapter;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.base.BaseListFragment;
import com.scau.easyfarm.bean.ExpertBase;
import com.scau.easyfarm.bean.ExpertBaseDetail;
import com.scau.easyfarm.bean.ExpertBaseList;
import com.scau.easyfarm.bean.ManualContent;
import com.scau.easyfarm.bean.ManualList;
import com.scau.easyfarm.bean.User;
import com.scau.easyfarm.bean.UserList;
import com.scau.easyfarm.ui.empty.EmptyLayout;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.StringUtils;
import com.scau.easyfarm.util.TDevice;
import com.scau.easyfarm.util.UIHelper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by ChenHehong on 2016/10/1.
 */
public class SearchExpertFragment extends BaseListFragment<ExpertBase> {

    private SearchView mSearchView;

    private static final String CACHE_KEY_PREFIX = "search_manual_list_";
    private String mSearch;
    private boolean mRquestDataIfCreated = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
    protected ExpertBaseAdapter getListAdapter() {
        return new ExpertBaseAdapter();
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mCatalog + mSearch;
    }

    @Override
    protected ExpertBaseList parseList(InputStream is) throws Exception {
        ExpertBaseList list = JsonUtils.toBean(ExpertBaseList.class, is);
        return list;
    }

    @Override
    protected ExpertBaseList readList(Serializable seri) {
        return ((ExpertBaseList) seri);
    }

    @Override
    protected void sendRequestData() {
        EasyFarmServerApi.getExpertSearchList(mSearch, mCurrentPage, mHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        ExpertBase res = mAdapter.getItem(position);
        if (res != null) {
            UIHelper.showExpertBseDetail(this, res.getId());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem search=menu.findItem(R.id.search_content);
        mSearchView=(SearchView) search.getActionView();
        mSearchView.setIconifiedByDefault(false);
        setSearch();
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setSearch() {
        mSearchView.setQueryHint("搜索专家");
        TextView textView = (TextView) mSearchView.findViewById(R.id.search_src_text);
        textView.setTextColor(Color.WHITE);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String arg0) {
                TDevice.hideSoftKeyboard(mSearchView);
                search(arg0);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String arg0) {
                return false;
            }
        });
        mSearchView.requestFocus();
    }
}