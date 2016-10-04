package com.scau.easyfarm.fragment;

import android.os.Bundle;
import android.support.v4.util.TimeUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.R;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.bean.User;
import com.scau.easyfarm.bean.VillageService;
import com.scau.easyfarm.bean.VillageServiceDetail;
import com.scau.easyfarm.bean.VillageServiceList;
import com.scau.easyfarm.bean.VillageServiceOpinion;
import com.scau.easyfarm.ui.empty.EmptyLayout;
import com.scau.easyfarm.util.DateTimeUtil;
import com.scau.easyfarm.util.JsonUtils;

import java.io.ByteArrayInputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cz.msebera.android.httpclient.Header;

/**
 * Created by chenhehong on 2016/9/9.
 */
public class VillageServiceDetailFragment extends BaseFragment {

    @InjectView(R.id.tv_applyman)
    TextView tvApplyMan;
    @InjectView(R.id.tv_person)
    TextView tvPerson;
    @InjectView(R.id.tv_address)
    TextView tvAddress;
    @InjectView(R.id.tv_reason)
    TextView tvReason;
    @InjectView(R.id.tv_service_date)
    TextView tvServiceDate;
    @InjectView(R.id.tv_statue)
    TextView tvStatue;
    @InjectView(R.id.tv_optionion)
    TextView tvOpinion;
    @InjectView(R.id.error_layout)
    EmptyLayout mErrorLayout;

    public static final String VILLAGE_SERVICE_ID_CODE = "village_service_id_code";

    private int mVillageServiceId;
    private VillageService mVillageService;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_village_service_detail,container,false);
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
        for (int i=0;i<mVillageService.getVillageServicePerson().size();i++){
            servicePerson+=mVillageService.getVillageServicePerson().get(i).getRealName()+"、";
        }
        tvPerson.setText(servicePerson);
        tvAddress.setText(mVillageService.getBusinessArea()+mVillageService.getBusinessAddress());
        tvReason.setText(mVillageService.getBusinessReason());
        tvServiceDate.setText(DateTimeUtil.DateTimeToDate(mVillageService.getBusinessDate())+"至"+DateTimeUtil.DateTimeToDate(mVillageService.getReturnDate()));
        if (mVillageService.getStatus()== VillageService.VILLAGE_SERVICE_WAITING){
            tvStatue.setText("待审核");
        }else if (mVillageService.getStatus()==VillageService.VILLAGE_SERVICE_PASS){
            tvStatue.setText("通过");
        }
        String optionion = "";
        if (mVillageService.getVillageServiceOpinions()!=null){
            boolean flage = false;
            for (int i=0;i<mVillageService.getVillageServiceOpinions().size();i++){
                if (flage) optionion+="\n\n";
                else flage = true;
                VillageServiceOpinion eachOpinion = mVillageService.getVillageServiceOpinions().get(i);
                optionion += eachOpinion.getOpinionPerson()+":"+eachOpinion.getOpinion();
            }
        }
        tvOpinion.setText(optionion);
    }
}
