package com.scau.easyfarm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.R;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.bean.ExpertBase;
import com.scau.easyfarm.bean.ExpertBaseDetail;
import com.scau.easyfarm.ui.empty.EmptyLayout;
import com.scau.easyfarm.util.JsonUtils;

import java.io.ByteArrayInputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by chenhehong on 2016/9/9.
 */
public class ExpertBaseDetailFragment extends BaseFragment {

    @InjectView(R.id.tv_realname)
    TextView tvRealName;
    @InjectView(R.id.tv_gender)
    TextView tvGender;
    @InjectView(R.id.tv_organization)
    TextView tvOrganization;
    @InjectView(R.id.tv_techtype)
    TextView tvTechType;
    @InjectView(R.id.tv_phonenumber)
    TextView tvPhoneNumber;
    @InjectView(R.id.tv_email)
    TextView tvEmail;
    @InjectView(R.id.tv_description)
    TextView tvDescrioption;
    @InjectView(R.id.error_layout)
    EmptyLayout mErrorLayout;

    public static final String BUNDLEKEY_EXPERTBASE_ID = "bundlekey_expertbase_id";

    private int mExpertBaseId;
    private ExpertBase mExpertBase;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expertbase_detail,container,false);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        ButterKnife.inject(this, view);
        setHasOptionsMenu(true);
        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                sendRequiredData();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        Bundle bundle = getArguments();
        mExpertBaseId = bundle.getInt(BUNDLEKEY_EXPERTBASE_ID);
        sendRequiredData();
    }

    public void sendRequiredData() {
        mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        EasyFarmServerApi.getExpertBaseDetail(mExpertBaseId,
                mHandler);
    }

    private final OperationResponseHandler mHandler = new OperationResponseHandler() {

        @Override
        public void onSuccess(int code, ByteArrayInputStream is, Object[] args) {
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            ExpertBase expertBase = JsonUtils.toBean(ExpertBaseDetail.class,
                   is).getExpertBase();
            if (expertBase!=null) {
                mExpertBase = expertBase;
                fillUI();
            } else {
                this.onFailure(code,null,args);
            }
        }

        @Override
        public void onFailure(int code, String errorMessage, Object[] args) {
            AppContext.showToast(errorMessage + code);
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
        }

    };

    public void fillUI() {
        tvRealName.setText(mExpertBase.getRealName());
        tvGender.setText(ExpertBase.genderIntMap.get(mExpertBase.getSex()));
        tvOrganization.setText(mExpertBase.getOrganization());
        tvTechType.setText(mExpertBase.getTechType());
        tvPhoneNumber.setText(mExpertBase.getPhoneNumber());
        tvEmail.setText(mExpertBase.getEmail());
        tvDescrioption.setText(mExpertBase.getDescription());
    }
}
