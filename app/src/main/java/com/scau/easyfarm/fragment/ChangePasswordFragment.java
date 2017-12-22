package com.scau.easyfarm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.R;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.bean.ResultBean;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.StringUtils;
import com.scau.easyfarm.util.TDevice;

import java.io.ByteArrayInputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by chenhehong on 2016/8/22.
 */
public class ChangePasswordFragment extends BaseFragment{

    @InjectView(R.id.et_original_password)
    EditText mOriginalPassword;
    @InjectView(R.id.et_new_password)
    EditText mNewPassword;
    @InjectView(R.id.et_new_password_confirm)
    EditText mNewPasswordConfirm;

    private String originalPassword = "";
    private String newPassword = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflateView(R.layout.fragment_change_password);
        ButterKnife.inject(this, view);
        initView(view);
        initData();
        return view;
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initView(View view) {
        super.initView(view);
    }

    @Override
    @OnClick(R.id.btn_change_password)
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        switch (id){
            case R.id.btn_change_password:
                handleChangePassword();
                break;
            default:
                break;
        }
    }

    private final OperationResponseHandler mHandler = new OperationResponseHandler() {

        @Override
        public void onSuccess(int code, ByteArrayInputStream is, Object[] args) {
            ResultBean resultBean = JsonUtils.toBean(ResultBean.class, is);
            if (resultBean != null) {
                handleResultBean(resultBean);
            }
        }

        @Override
        public void onFailure(int code, String errorMessage, Object[] args) {
            AppContext.showToast("网络出错" + errorMessage);
        }

        @Override
        public void onFinish() {
            super.onFinish();
            hideWaitDialog();
        }
    };

    private void handleResultBean(ResultBean resultBean){
        if (resultBean.getResult().OK()){
            hideWaitDialog();
            AppContext.showToastShort("密码修改成功，请重新登录！");
            AppContext.getInstance().Logout();
            getActivity().finish();
        }else {
            hideWaitDialog();
            AppContext.showToast(resultBean.getResult().getErrorMessage());
        }
    }

    private void handleChangePassword() {

        if (prepareForHandle()) {
            return;
        }

        // if the data has ready
        originalPassword = mOriginalPassword.getText().toString();
        newPassword = mNewPassword.getText().toString();

        showWaitDialog(R.string.progress_update);
        EasyFarmServerApi.change_password(originalPassword, newPassword, mHandler);
    }

    private boolean prepareForHandle(){
        if (!TDevice.hasInternet()){
            AppContext.showToastShort(R.string.tip_no_internet);
            return true;
        }
        if (mOriginalPassword.length()==0){
            mOriginalPassword.setError("请输入原先密码");
            mOriginalPassword.requestFocus();
            return true;
        }
        if (mNewPassword.length()==0){
            mNewPassword.setError("请输入新密码");
            mNewPassword.requestFocus();
            return true;
        }
        if (mNewPasswordConfirm.length()==0){
            mNewPasswordConfirm.setError("请再次输入新的密码");
            mNewPasswordConfirm.requestFocus();
            return true;
        }
        if (!mNewPassword.getText().toString().equals(mNewPasswordConfirm.getText().toString())){
            mNewPasswordConfirm.setError("新密码输入不一致");
            mNewPasswordConfirm.requestFocus();
            return true;
        }
        if (!StringUtils.isCorrectPasswordFormat(mNewPassword.getText().toString())){
            AppContext.showToast("新密码格式不对！至少为6个字符组成");
            return true;
        }
        return false;
    }

}
