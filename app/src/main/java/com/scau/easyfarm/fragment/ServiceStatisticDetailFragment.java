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
import com.scau.easyfarm.bean.VillageService;
import com.scau.easyfarm.bean.VillageServiceDetail;
import com.scau.easyfarm.bean.VillageServiceOpinion;
import com.scau.easyfarm.ui.empty.EmptyLayout;
import com.scau.easyfarm.util.DateTimeUtil;
import com.scau.easyfarm.util.JsonUtils;

import java.io.ByteArrayInputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by chenhehong on 2016/9/9.
 */
public class ServiceStatisticDetailFragment extends BaseFragment {

    @InjectView(R.id.tv_applyman)
    TextView tvApplyMan;
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
    @InjectView(R.id.error_layout)
    EmptyLayout mErrorLayout;

    public static final String VILLAGE_SERVICE_ID_CODE = "village_service_id_code";

    private int mVillageServiceId;
    private VillageService mVillageService;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_village_service_statistic_detail,container,false);
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
            VillageService villageService = JsonUtils.toBean(VillageServiceDetail.class,
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

    public void fillUI() {
        tvApplyMan.setText(mVillageService.getApplyManName());
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
        tvServiceDate.setText(DateTimeUtil.dateTimeToDate(mVillageService.getBusinessDate())+"至"+DateTimeUtil.dateTimeToDate(mVillageService.getReturnDate()));
    }
}
