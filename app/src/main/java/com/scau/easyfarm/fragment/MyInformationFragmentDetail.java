package com.scau.easyfarm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.R;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.bean.MyInformation;
import com.scau.easyfarm.bean.User;
import com.scau.easyfarm.ui.empty.EmptyLayout;
import com.scau.easyfarm.util.JsonUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * 登录用户信息详情
 *
 */

public class MyInformationFragmentDetail extends BaseFragment {

    public static final int ACTION_TYPE_ALBUM = 0;
    public static final int ACTION_TYPE_PHOTO = 1;

    @InjectView(R.id.iv_avatar)
    ImageView mUserFace;

    @InjectView(R.id.tv_name)
    TextView mName;

    @InjectView(R.id.tv_user_info_address)
    TextView mAddress;

    @InjectView(R.id.tv_user_info_age)
    TextView mAge;

    @InjectView(R.id.tv_user_info_description)
    TextView mDescription;

    @InjectView(R.id.tv_user_info_email)
    TextView mEmail;

    @InjectView(R.id.tv_user_info_phonenumber)
    TextView mPhonenumber;

    @InjectView(R.id.tv_user_info_realname)
    TextView mRealname;

    @InjectView(R.id.tv_user_info_roletype)
    TextView mRoletype;

    @InjectView(R.id.tv_user_info_sex)
    TextView mSex;

    @InjectView(R.id.tv_user_info_techtype)
    TextView mTechtype;

    @InjectView(R.id.error_layout)
    EmptyLayout mErrorLayout;

    private User mUser;

    private final AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            String s = new String(arg2);
            MyInformation user = JsonUtils.toBean(MyInformation.class,
                    new ByteArrayInputStream(arg2));
            if (user != null && user.getUser() != null) {
                mUser = user.getUser();
                fillUI();
            } else {
                this.onFailure(arg0, arg1, arg2, null);
            }
        }

        @Override
        public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                              Throwable arg3) {
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(
                R.layout.fragment_my_information_detail, container, false);
        initView(view);
        initData();
        return view;
    }

    @Override
    @OnClick({R.id.btn_logout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                AppContext.getInstance().Logout();
                AppContext.showToastShort(R.string.tip_logout_success);
                getActivity().finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void initView(View view) {
        ButterKnife.inject(this, view);
        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequiredData();
            }
        });

        mUserFace.setOnClickListener(this);
    }

    @Override
    public void initData() {
        sendRequiredData();
    }

    public void fillUI() {
        mName.setText(mUser.getLoginName());
        mAddress.setText(mUser.getAddress());
        mAge.setText(mUser.getAge()+"");
        mDescription.setText(mUser.getDescription());
        mEmail.setText(mUser.getEmail());
        mPhonenumber.setText(mUser.getPhoneNumber());
        mRealname.setText(mUser.getRealName());
        mRoletype.setText(mUser.getRoleName());
        mSex.setText(mUser.getSex());
        mTechtype.setText(mUser.getTechType());
    }

    public void sendRequiredData() {
        mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        EasyFarmServerApi.getMyInformation(AppContext.getInstance().getLoginUid(),
                mHandler);
    }
}
