package com.scau.easyfarm.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.R;
import com.scau.easyfarm.adapter.SelectedUserAdapter;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.bean.Entity;
import com.scau.easyfarm.bean.ResultBean;
import com.scau.easyfarm.bean.User;
import com.scau.easyfarm.bean.VillageService;
import com.scau.easyfarm.util.DialogHelp;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.SimpleTextWatcher;
import com.scau.easyfarm.util.StringUtils;
import com.scau.easyfarm.util.TDevice;
import com.scau.easyfarm.util.UIHelper;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by ChenHehong on 2016/9/2.
 */
public class RegisterFragment extends BaseFragment{

    @InjectView(R.id.et_loginname)
    EditText loginName;
    @InjectView(R.id.et_password)
    EditText password;
    @InjectView(R.id.et_affirm_password)
    EditText affirmPassword;
    @InjectView(R.id.et_realname)
    EditText realName;
    @InjectView(R.id.et_age)
    EditText age;
    @InjectView(R.id.et_phone)
    EditText phoneNumber;
    @InjectView(R.id.et_email)
    EditText email;
    @InjectView(R.id.et_area)
    EditText etArea;
    @InjectView(R.id.et_address)
    EditText address;
    @InjectView(R.id.sp_gender)
    Spinner spGender;
    @InjectView(R.id.btn_submit)
    Button btnSubmit;

    private ArrayAdapter<String> spinnerAdapter;
    public int gender;

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
            hideWaitDialog();
            AppContext.showToast(errorMessage + code);
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
            AppContext.showToastShort("注册成功！");
            getActivity().finish();
        }else {
            hideWaitDialog();
            AppContext.showToast(resultBean.getResult().getErrorMessage());
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register,container,false);
        initView(view);
        return view;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        ButterKnife.inject(this, view);
        etArea.setOnClickListener(this);
        spinnerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.my_spinner_item, User.genderArray);
//      simple_spinner_dropdown_item.xml设置的是下拉看到的效果
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// 设置下拉风格
        spGender.setAdapter(spinnerAdapter);
        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = User.genderStrMap.get(User.genderArray[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initData();
    }

    @Override
    public void initData(){
        spGender.setSelection(0);
    }

    @Override
    public boolean onBackPressed() {
        DialogHelp.getConfirmDialog(getActivity(), "确定离开注册页面?", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }
        }, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
        return true;
    }

    private void handleSubmit() {
        if (!TDevice.hasInternet()) {
            AppContext.showToastShort(R.string.tip_network_error);
            return;
        }
        String loginName = this.loginName.getText().toString();
        String password = this.password.getText().toString();
        String affirmPassword = this.affirmPassword.getText().toString();
        String realName = this.realName.getText().toString();
        String mobile = this.phoneNumber.getText().toString();
        String email = this.email.getText().toString();
        String age = this.age.getText().toString();
        String area = this.etArea.getText().toString();
        String address = this.address.getText().toString();
        if (loginName==null||loginName.length()==0){
            AppContext.showToast("登录名不能为空！");
            return;
        }
        if (!StringUtils.isCorrectLoginNameFormat(loginName)){
            AppContext.showToast("登录名格式不对！由字母数字或下划线组成，5-16个字符");
            return;
        }
        if (password==null||password.length()==0){
            AppContext.showToast("密码不能为空");
            return;
        }
        if (!password.equals(affirmPassword)){
            AppContext.showToast("两次密码输入不一致，请检查");
            return;
        }
        if (realName==null||realName.length()==0){
            AppContext.showToast("真实姓名不能为空");
            return;
        }
        if (email==null||email.length()==0){
            AppContext.showToast("邮箱不能为空");
            return;
        }
        if (!StringUtils.isEmail(email)){
            AppContext.showToast("邮箱格式不对");
            return;
        }
        if (mobile==null||mobile.length()==0){
            AppContext.showToast("手机号码不能为空");
            return;
        }
        if (!StringUtils.isMobileNumber(mobile)){
            AppContext.showToast("手机号码格式错误");
            return;
        }
        if (area==null||area.length()==0){
            AppContext.showToast("请选择所在区域");
            return;
        }
        showWaitDialog("注册中，请稍后");
        EasyFarmServerApi.register(loginName, password, realName, age, gender, email, mobile,area+address,mHandler);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if(id==R.id.btn_submit){
            handleSubmit();
        }else if (id==R.id.et_area){
            handleSelectArea();
        }
    }

    private void handleSelectArea(){
        UIHelper.showAreaChoose(this, AreaCatalogListFragment.AREA_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent returnIntent) {
        if (resultCode != Activity.RESULT_OK)
            return;
        if (requestCode==AreaCatalogListFragment.AREA_REQUEST_CODE){
            String selectedArea = returnIntent.getStringExtra(AreaCatalogListFragment.AREA_SELECTED_CODE);
            etArea.setText(selectedArea);
            return;
        }
    }
}
