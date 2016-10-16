package com.scau.easyfarm.fragment;

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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.R;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.bean.ResultBean;
import com.scau.easyfarm.bean.VillageService;
import com.scau.easyfarm.bean.VillageServiceDetail;
import com.scau.easyfarm.ui.empty.EmptyLayout;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.TDevice;
import com.scau.easyfarm.util.UIHelper;

import java.io.ByteArrayInputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by chenhehong on 2016/9/9.
 */
public class VillageServiceVerifyFragment extends BaseFragment {

    @InjectView(R.id.tv_person)
    TextView tvPerson;
    @InjectView(R.id.tv_leader)
    TextView tvLeader;
    @InjectView(R.id.tv_address)
    TextView tvAddress;
    @InjectView(R.id.tv_reason)
    TextView tvReason;
    @InjectView(R.id.tv_service_date)
    TextView tvServiceDate;
    @InjectView(R.id.tv_status)
    TextView tvStatus;
    @InjectView(R.id.sp_my_status)
    Spinner myStatusSpinner;
    @InjectView(R.id.et_optinion)
    EditText optinionEditText;
    @InjectView(R.id.error_layout)
    EmptyLayout mErrorLayout;

    private ArrayAdapter<String> spinnerAdapter;

    public static final String VILLAGE_SERVICE_ID_CODE = "village_service_id_code";
    public static final int REQUESTCODE_VERIFY = 121;

    private int mVillageServiceId;
    private VillageService mVillageService;
    private int selectStatus;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_village_service_verify,container,false);
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
//      my_spinner_item.xml设置的是选中后看到的效果
        spinnerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.my_spinner_item, VillageService.myStatusStrArray);
//      simple_spinner_dropdown_item.xml设置的是下拉看到的效果
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// 设置下拉风格
        myStatusSpinner.setAdapter(spinnerAdapter);
        myStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedStr = VillageService.myStatusStrArray[position];
                selectStatus = VillageService.myStatusStrMap.get(selectedStr);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        setHasOptionsMenu(true);

        mErrorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequiredData();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        Bundle bundle = getArguments();
        mVillageServiceId = bundle.getInt(VILLAGE_SERVICE_ID_CODE);
        sendRequiredData();
    }

    public void sendRequiredData() {
        mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        EasyFarmServerApi.getVillageServiceDetail(mVillageServiceId,
                mHandler);
    }

    private final OperationResponseHandler mHandler = new OperationResponseHandler() {

        @Override
        public void onSuccess(int code, ByteArrayInputStream is, Object[] args) {
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            VillageService  villageService = JsonUtils.toBean(VillageServiceDetail.class,
                    is).getVillageService();
            if (villageService!=null) {
                mVillageService = villageService;
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

    private void handleResultBean(ResultBean resultBean){
        if (resultBean.getResult().OK()){
            hideWaitDialog();
            AppContext.showToastShort("审核成功！");
            getActivity().setResult(getActivity().RESULT_OK);
            getActivity().finish();
        }else {
            hideWaitDialog();
            AppContext.showToast(resultBean.getResult().getErrorMessage());
        }
    }

    public void fillUI() {
        String servicePerson = "";
        boolean flag=false;
        for (int i=0;i<mVillageService.getVillageServicePerson().size();i++){
            if (flag) servicePerson+="、";
            else flag=true;
            servicePerson+=mVillageService.getVillageServicePerson().get(i).getRealName();
        }
        String leaders = "";
        flag=false;
        for (int i=0;i<mVillageService.getLeaders().size();i++){
            if (flag) leaders+="、";
            else flag=true;
            leaders+=mVillageService.getVillageServicePerson().get(i).getRealName();
        }
        tvPerson.setText(servicePerson);
        tvLeader.setText(leaders);
        tvAddress.setText(mVillageService.getBusinessArea()+mVillageService.getBusinessAddress());
        tvReason.setText(mVillageService.getBusinessReason());
        tvServiceDate.setText(mVillageService.getBusinessDate()+"至"+mVillageService.getReturnDate());
        tvStatus.setText(VillageService.statusIntMap.get(mVillageService.getStatus()));
        myStatusSpinner.setSelection(0);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.submit_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.public_menu_send:
                handleSubmit();
                break;
            default:
                break;
        }
        return true;
    }

    private void handleSubmit() {
        if (!TDevice.hasInternet()) {
            AppContext.showToastShort(R.string.tip_network_error);
            return;
        }
        if (!AppContext.getInstance().isLogin()) {
            UIHelper.showLoginActivity(getActivity());
            return;
        }
        showWaitDialog("提交中，请稍后");
        EasyFarmServerApi.verifyVillageService(mVillageServiceId, selectStatus, optinionEditText.getText().toString(), mSubmitHandler);
    }

    private final OperationResponseHandler mSubmitHandler = new OperationResponseHandler() {

        @Override
        public void onSuccess(int code, ByteArrayInputStream is, Object[] args) {
            ResultBean resultBean = JsonUtils.toBean(ResultBean.class, is);
            if (resultBean != null) {
                handleResultBean(resultBean);
            }
        }

        @Override
        public void onFailure(int code, String errorMessage, Object[] args){
            AppContext.showToast("网络出错" + code);
        }

        @Override
        public void onFinish() {
            super.onFinish();
            hideWaitDialog();
        }
    };
}
