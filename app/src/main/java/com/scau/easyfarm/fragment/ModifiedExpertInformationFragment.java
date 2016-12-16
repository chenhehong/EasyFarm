package com.scau.easyfarm.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.R;
import com.scau.easyfarm.api.ApiHttpClient;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.bean.ModifiedInformationResult;
import com.scau.easyfarm.bean.User;
import com.scau.easyfarm.util.DialogHelp;
import com.scau.easyfarm.util.ImageUtils;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.StringUtils;
import com.scau.easyfarm.util.TDevice;

import org.kymjs.kjframe.Core;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by ChenHehong on 2016/9/2.
 */
public class ModifiedExpertInformationFragment extends BaseFragment{

    @InjectView(R.id.et_age)
    EditText age;
    @InjectView(R.id.et_phone)
    EditText phoneNumber;
    @InjectView(R.id.et_email)
    EditText email;
    @InjectView(R.id.et_address)
    EditText address;
    @InjectView(R.id.et_jobposition)
    EditText jobpostion;
    @InjectView(R.id.et_techtype)
    EditText techtype;
    @InjectView(R.id.et_techtype2)
    EditText techtype2;
    @InjectView(R.id.et_description)
    EditText description;
    @InjectView(R.id.sp_gender)
    Spinner spGender;
    @InjectView(R.id.portrait)
    ImageView portrait;
    @InjectView(R.id.btn_submit)
    Button btnSubmit;

    private ArrayAdapter<String> spinnerAdapter;
    public int gender;

    private String imagePath="";
    Bitmap imageBitmap;


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
        View view = inflater.inflate(R.layout.fragment_modified_expert_information,container,false);
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
        if (user.getSex()==User.MAN){
            spGender.setSelection(0);
        }else {
            spGender.setSelection(1);
        }
        age.setText(user.getAge()+"");
        phoneNumber.setText(user.getPhoneNumber());
        email.setText(user.getEmail());
        address.setText(user.getAddress());
        if (user.getPortrait()!=null&&user.getPortrait().length()>0){
            new Core.Builder().view(portrait).url(ApiHttpClient.getAbsoluteApiUrl(user.getPortrait())).doTask();
        }
        portrait.setOnClickListener(this);
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
        String mobile = this.phoneNumber.getText().toString();
        String email = this.email.getText().toString();
        String age = this.age.getText().toString();
        String address = this.address.getText().toString();
        String techType = this.techtype.getText().toString();
        String techType2 = this.techtype2.getText().toString();
        String description = this.description.getText().toString();
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
        EasyFarmServerApi.modifiedExpertInformation(age, gender, email, mobile, address,techType,techType2,description,imagePath, mHandler);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if(id==R.id.btn_submit){
            handleSubmit();
        }else if (id==R.id.portrait){
            MultiImageSelector.create(this.getContext())
                    .showCamera(true).single().count(1).start(this, ImageUtils.REQUEST_CODE_SINGLESELECT_PICTURE);
        }
    }

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                portrait.setImageBitmap(imageBitmap);
            }
        }
    };

    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent returnIntent) {
        if (resultCode != Activity.RESULT_OK)
            return;
        if (requestCode==ImageUtils.REQUEST_CODE_SINGLESELECT_PICTURE){
            final ArrayList<String> imagePaths =  returnIntent.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            new Thread() {
                @Override
                public void run() {
                    //  压缩成bitmap形式的缩略图
                    imagePath = ImageUtils.compressPortraitImage(imagePaths.get(0),getActivity());
                    imageBitmap = ImageUtils.loadImgThumbnail(imagePath,100,100);
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            }.start();
        }
    }
}
