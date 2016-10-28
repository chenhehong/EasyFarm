package com.scau.easyfarm.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.R;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.bean.ModifiedInformationResult;
import com.scau.easyfarm.bean.ResultBean;
import com.scau.easyfarm.bean.User;
import com.scau.easyfarm.util.DialogHelp;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.StringUtils;
import com.scau.easyfarm.util.TDevice;

import java.io.ByteArrayInputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by ChenHehong on 2016/9/2.
 */
public class ModifiedMyInformationFragment extends BaseFragment{

    @InjectView(R.id.et_realname)
    EditText realName;
    @InjectView(R.id.et_age)
    EditText age;
    @InjectView(R.id.et_phone)
    EditText phoneNumber;
    @InjectView(R.id.et_email)
    EditText email;
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
            ModifiedInformationResult resultBean = JsonUtils.toBean(ModifiedInformationResult.class, is);
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

    private void handleResultBean(ModifiedInformationResult resultBean){
        if (resultBean.getResult().OK()){
            hideWaitDialog();
            AppContext.showToastShort("修改成功！");
            User user = resultBean.getUser();
            if (user!=null){
                AppContext.getInstance().updateUserInfo(user);
            }
            getActivity().finish();
        }else {
            hideWaitDialog();
            AppContext.showToast(resultBean.getResult().getErrorMessage());
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modified_myinformation,container,false);
        initView(view);
        return view;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        ButterKnife.inject(this, view);

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
        User user = AppContext.getInstance().getLoginUser();
        realName.setText(user.getRealName());
        if (user.getSex()==User.MAN){
            spGender.setSelection(0);
        }else {
            spGender.setSelection(1);
        }
        age.setText(user.getAge()+"");
        phoneNumber.setText(user.getPhoneNumber());
        email.setText(user.getEmail());
        address.setText(user.getAddress());
    }

    @Override
    public boolean onBackPressed() {
        DialogHelp.getConfirmDialog(getActivity(), "确定离开资料修改页面?", new DialogInterface.OnClickListener() {

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
        String realName = this.realName.getText().toString();
        String mobile = this.phoneNumber.getText().toString();
        String email = this.email.getText().toString();
        String age = this.age.getText().toString();
        String address = this.address.getText().toString();
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
        showWaitDialog("修改中，请稍后");
        EasyFarmServerApi.modifiedMyInformation(realName, age, gender, email, mobile, address, mHandler);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if(id==R.id.btn_submit){
            handleSubmit();
        }
    }
}