package com.scau.easyfarm.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.scau.easyfarm.R;
import com.scau.easyfarm.adapter.WitTweetAdapter;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.bean.ManualCategory;
import com.scau.easyfarm.bean.WitTweet;
import com.scau.easyfarm.bean.WitTweetList;
import com.scau.easyfarm.ui.empty.EmptyLayout;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.SimpleTextWatcher;
import com.scau.easyfarm.util.StringUtils;
import com.scau.easyfarm.util.UIHelper;

import java.io.ByteArrayInputStream;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by ChenHehong on 2016/10/1.
 */
public class WitTweetFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    @InjectView(R.id.et_tweet_type)
    EditText mEtType;
    @InjectView(R.id.btn_tweet_type)
    ImageView mImvType;
    @InjectView(R.id.et_tweet_title)
    EditText mEtTitle;
    @InjectView(R.id.tv_hint)
    TextView tvHint;
    @InjectView(R.id.lv_list)
    ListView mListView;
    @InjectView(R.id.error_layout)
    EmptyLayout mErrorLayout;
    @InjectView(R.id.btn_newtweet)
    Button btnNewTweet;

    private String selectedTypeName = "";
    private int selectedTypeId = 0;

    private WitTweetAdapter mAdapter;

    private OperationResponseHandler mHandle = new OperationResponseHandler() {

        @Override
        public void onSuccess(int code, ByteArrayInputStream is, Object[] args) {
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            WitTweetList list = JsonUtils.toBean(WitTweetList.class,is);
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
        View view = inflater.inflate(R.layout.fragment_wit_tweet,container,false);
        ButterKnife.inject(this, view);
        initView(view);
        return view;
    }

    @Override
    public void initView(View view) {
        mAdapter = new WitTweetAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(mEtTitle.getText().toString());
            }
        });
        mEtTitle.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(mEtTitle.getText().toString());
            }
        });
        mImvType.setOnClickListener(this);
        btnNewTweet.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
    }

    private void search(String title) {
        if (title == null || StringUtils.isEmpty(title)) {
            return;
        }
        mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        mListView.setVisibility(View.GONE);
        EasyFarmServerApi.findWitTweet(title, selectedTypeId, mHandle);
    }

    private void executeOnLoadDataSuccess(List<WitTweet> data) {
        if (data==null||data.size()==0){
            tvHint.setText("没有搜索到结果，请向专家提问");
        }
        mAdapter.clear();
        mAdapter.addData(data);
        mListView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        WitTweet witTweet = mAdapter.getItem(position);
        if (witTweet != null) {
            if (witTweet.getType()==WitTweet.TYPE_TWEET){
                UIHelper.showTweetDetail(view.getContext(), null, witTweet.getId());
            }else if (witTweet.getType()==WitTweet.TYPE_MANUAL){
                UIHelper.showManualContentDetail(view.getContext(), witTweet.getId());
            }
        }
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id==R.id.btn_tweet_type){
            handlerSelectType();
        }else if (id==R.id.btn_newtweet){
            UIHelper.showTweetPub(getActivity(),selectedTypeId,selectedTypeName,mEtTitle.getText().toString());
            getActivity().finish();
        }
    }

    public void handlerSelectType(){
        ManualCategory industryManualCategory = new ManualCategory(0,0,false,"全部类型/",ManualCategory.EXPERT,"");
        UIHelper.showTweetTypeChoose(this, industryManualCategory, TweetChooseManualCategoryFragment.MANUAL_COTEGORY_LIST_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent returnIntent) {
        if (resultCode != Activity.RESULT_OK)
            return;
//      如果是选择at专家的
        if (requestCode == TweetChooseManualCategoryFragment.MANUAL_COTEGORY_LIST_REQUEST_CODE) {
            selectedTypeName = returnIntent.getStringExtra(TweetChooseManualCategoryFragment.SELECTED_MANUAL_COTEGORY_NAME);
            selectedTypeId = returnIntent.getIntExtra(TweetChooseManualCategoryFragment.SELECTED_MANUAL_COTEGORY_ID, 0);
            mEtType.setText(selectedTypeName);
            return;
        }
    }
}
