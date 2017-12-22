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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.scau.easyfarm.R;
import com.scau.easyfarm.adapter.FindUserAdapter;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.bean.User;
import com.scau.easyfarm.bean.UserList;
import com.scau.easyfarm.ui.empty.EmptyLayout;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by ChenHehong on 2016/10/1.
 */
public class FindUserFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    public static final int REQUESTCODE_FIND_USER = 102;
    public static final String BUNDLE_SELECT_USER_ID = "bundle_select_user_id";
    public static final String BUNDLE_SELECT_USER_NAME = "bundle_select_user_name";

    private SearchView mSearchView;

    @InjectView(R.id.lv_list)
    ListView mListView;

    @InjectView(R.id.error_layout)
    EmptyLayout mErrorLayout;

    private FindUserAdapter mAdapter;

    private OperationResponseHandler mHandle = new OperationResponseHandler() {

        @Override
        public void onSuccess(int code, ByteArrayInputStream is, Object[] args) {
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            UserList list = JsonUtils.toBean(UserList.class,is);
            executeOnLoadDataSuccess(list.getList());
        }

        @Override
        public void onFailure(int code, String errorMessage, Object[] args) {
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_find_user,container,false);
        ButterKnife.inject(this, view);
        initView(view);
        return view;
    }

    @Override
    public void initView(View view) {
        setHasOptionsMenu(true);
        mAdapter = new FindUserAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                search(mSearchView.getQuery().toString());
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem search = menu.findItem(R.id.search_content);
        mSearchView = (SearchView) search.getActionView();
        mSearchView.setIconifiedByDefault(false);
        setSearch();
    }

    private void setSearch() {
        mSearchView.setQueryHint("输入用户账号名称");
        TextView textView = (TextView) mSearchView
                .findViewById(R.id.search_src_text);
        textView.setTextColor(Color.WHITE);

        mSearchView
                .setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(String arg0) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String arg0) {
                        search(arg0);
                        return false;
                    }
                });
    }

    private void search(String loginName) {
        if (loginName == null || StringUtils.isEmpty(loginName)) {
            return;
        }
        mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        mListView.setVisibility(View.GONE);
        EasyFarmServerApi.findUser(loginName, mHandle);
    }

    private void executeOnLoadDataSuccess(List<User> data) {
        mAdapter.clear();
        mAdapter.addData(data);
        mListView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        User user = mAdapter.getItem(position);
        if (user != null) {
            Intent result = new Intent();
            result.putExtra(BUNDLE_SELECT_USER_ID, user.getId());
            result.putExtra(BUNDLE_SELECT_USER_NAME,user.getRealName()+"("+StringUtils.formatJobNumber(user.getLoginName())+")");
            getActivity().setResult(getActivity().RESULT_OK, result);
            getActivity().finish();
        }
    }
}
