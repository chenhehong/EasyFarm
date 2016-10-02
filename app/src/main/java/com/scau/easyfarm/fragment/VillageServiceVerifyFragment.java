package com.scau.easyfarm.fragment;

import android.os.Bundle;
import android.util.Log;
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

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.R;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.bean.ResultBean;
import com.scau.easyfarm.bean.VillageService;
import com.scau.easyfarm.bean.VillageServiceDetail;
import com.scau.easyfarm.bean.VillageServiceList;
import com.scau.easyfarm.bean.VillageServiceOpinion;
import com.scau.easyfarm.ui.empty.EmptyLayout;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.TDevice;
import com.scau.easyfarm.util.UIHelper;

import java.io.ByteArrayInputStream;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cz.msebera.android.httpclient.Header;

/**
 * Created by chenhehong on 2016/9/9.
 */
public class VillageServiceVerifyFragment extends BaseFragment {

    @InjectView(R.id.tv_person)
    TextView tvPerson;
    @InjectView(R.id.tv_address)
    TextView tvAddress;
    @InjectView(R.id.tv_reason)
    TextView tvReason;
    @InjectView(R.id.tv_service_date)
    TextView tvServiceDate;
    @InjectView(R.id.sp_status)
    Spinner statusSpinner;
    @InjectView(R.id.et_optinion)
    EditText optinionEditText;
    @InjectView(R.id.error_layout)
    EmptyLayout mErrorLayout;

    private ArrayAdapter<String> spinnerAdapter;
    public static final HashMap<String,Integer> statusMap = new HashMap<String,Integer>(){{ put("待审核",VillageServiceList.VILLAGE_SERVICE_WAITING);  put("通过",10);  put("不通过",11);} };
    public static final String[] statusArray = {"待审核","通过","不通过"};

    public static final String VILLAGE_SERVICE_ID_CODE = "village_service_id_code";

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
        spinnerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.my_spinner_item, statusArray);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// 设置下拉风格
        statusSpinner.setAdapter(spinnerAdapter);
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedStr = statusArray[position];
                selectStatus = statusMap.get(selectedStr);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        setHasOptionsMenu(true);
    }

    @Override
    public void initData() {
        super.initData();
        Bundle bundle = getArguments();
        mVillageServiceId = bundle.getInt(VILLAGE_SERVICE_ID_CODE);
        sendRequiredData();
//      start-模拟数据
//        User u1 = new User();
//        u1.setRealName("陈河宏");
//        User u2 = new User();
//        u2.setRealName("李强");
//        VillageServiceOpinion o1 = new VillageServiceOpinion();
//        o1.setOpinionPerson("刘主任");o1.setOpinion("同意通过");
//        VillageServiceOpinion o2 = new VillageServiceOpinion();
//        o2.setOpinionPerson("马处长");o2.setOpinion("时间不方便");
//        VillageService v = new VillageService();
//        v.getVillageServicePerson().add(u1);
//        v.getVillageServicePerson().add(u2);
//        v.getVillageServiceOpinions().add(o1);
//        v.getVillageServiceOpinions().add(o2);
//        v.setBusinessArea("广东省-广州市-天河区");
//        v.setBusinessAddress("华南农业大学");
//        v.setBusinessReason("考察");
//        v.setBusinessDate("2016-8-23");
//        v.setReturnDate("2016-9-25");
//        v.setStatus(0);
//        mVillageService = v;
//        fillUI();
//      end--模拟数据
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
            AppContext.showToastShort("发布成功！");
            getActivity().finish();
        }else {
            hideWaitDialog();
            AppContext.showToast(resultBean.getResult().getErrorMessage());
        }
    }

    public void fillUI() {
        String servicePerson = "";
        for (int i=0;i<mVillageService.getVillageServicePerson().size();i++){
            servicePerson+=mVillageService.getVillageServicePerson().get(i).getRealName()+"、";
        }
        tvPerson.setText(servicePerson);
        tvAddress.setText(mVillageService.getBusinessArea()+mVillageService.getBusinessAddress());
        tvReason.setText(mVillageService.getBusinessReason());
        tvServiceDate.setText(mVillageService.getBusinessDate()+"至"+mVillageService.getReturnDate());
        for (int i=0;i<statusArray.length;i++){
            String selectedString = statusArray[i];
            if (statusMap.get(selectedString)==mVillageService.getStatus()){
                statusSpinner.setSelection(i);
                break;
            }
        }
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
