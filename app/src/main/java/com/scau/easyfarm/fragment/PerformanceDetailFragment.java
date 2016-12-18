package com.scau.easyfarm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.R;
import com.scau.easyfarm.api.ApiHttpClient;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.bean.Performance;
import com.scau.easyfarm.bean.PerformanceDetail;
import com.scau.easyfarm.bean.PerformanceMemberWorkTime;
import com.scau.easyfarm.bean.VerifyOpinion;
import com.scau.easyfarm.bean.VillageService;
import com.scau.easyfarm.bean.VillageServiceDetail;
import com.scau.easyfarm.ui.ImageGalleryActivity;
import com.scau.easyfarm.ui.empty.EmptyLayout;
import com.scau.easyfarm.util.DateTimeUtil;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.TDevice;
import com.scau.easyfarm.util.UIHelper;

import java.io.ByteArrayInputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by chenhehong on 2016/9/9.
 */
public class PerformanceDetailFragment extends BaseFragment {

    @InjectView(R.id.tv_applyman)
    TextView tvApplyMan;
    @InjectView(R.id.tv_type)
    TextView tvType;
    @InjectView(R.id.tv_service_detail)
    TextView tvServiceDetail;
    @InjectView(R.id.tv_server_date)
    TextView tvServerDate;
    @InjectView(R.id.tv_user_worktime)
    TextView tvUserWorkTime;
    @InjectView(R.id.worktime_explain)
    View worktime_explain;
    @InjectView(R.id.tv_apply_worktime)
    TextView tvApplyWorktime;
    @InjectView(R.id.tv_description)
    TextView tvDescription;
    @InjectView(R.id.tv_statue)
    TextView tvStatue;
    @InjectView(R.id.tv_optionion)
    TextView tvOpinion;
    @InjectView(R.id.error_layout)
    EmptyLayout mErrorLayout;
    @InjectView(R.id.tv_serverNumber)
    TextView tvServerNumber;
    @InjectView(R.id.layout_grid)
    GridLayout mLayoutGrid;

    public static final String PERFORMANCE_ID_CODE = "performance_id_code";

    private int mPerformanceId;
    private Performance mPerformance;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_performance_detail,container,false);
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
        mPerformanceId = bundle.getInt(PERFORMANCE_ID_CODE);
        sendRequiredData();
    }

    public void sendRequiredData() {
        mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        EasyFarmServerApi.getPerformanceDetail(mPerformanceId,
                mHandler);
    }

    private final OperationResponseHandler mHandler = new OperationResponseHandler() {

        @Override
        public void onSuccess(int code, ByteArrayInputStream is, Object[] args) {
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            Performance performance = JsonUtils.toBean(PerformanceDetail.class,
                   is).getPerformance();
            if (performance!=null) {
                mPerformance = performance;
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
        tvApplyMan.setText(mPerformance.getApplyManName());
        tvServerNumber.setText(mPerformance.getPerformanceCode());
        tvDescription.setText(mPerformance.getDescription());
        tvType.setText(mPerformance.getPerformanceTypeStr());
        if (mPerformance.getVillageService()!=null){
            worktime_explain.setVisibility(View.VISIBLE);
            tvServiceDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIHelper.showVillageServiceDetail(getActivity(), mPerformance.getVillageService().getId());
                }
            });
        }
        tvServerDate.setText(mPerformance.getPerformanceServerDateDesc());
        tvStatue.setText(mPerformance.getStatusString());
        tvApplyWorktime.setText(mPerformance.getApplyWorkTime()+"");
        String memberWorkTimes = "";
        if (mPerformance.getPerformanceMemberWorkTimeList()!=null){
            boolean flage = false;
            for (int i=0;i< mPerformance.getPerformanceMemberWorkTimeList().size();i++){
                if (flage) memberWorkTimes+="\n\n";
                else flage = true;
                PerformanceMemberWorkTime memberWorkTime = mPerformance.getPerformanceMemberWorkTimeList().get(i);
                memberWorkTimes += memberWorkTime.getUserName()+": "+memberWorkTime.getRealWorkTime();
            }
        }
        tvUserWorkTime.setText(memberWorkTimes);
        if (mPerformance.getFileList() != null && (mPerformance.getFileList().size() > 0)) {
            mLayoutGrid.setVisibility(View.VISIBLE);
            mLayoutGrid.removeAllViews();
            final View.OnClickListener l = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    String description = mPerformance.getFileList().get(position).getTypeString()+":"+mPerformance.getFileList().get(position).getDescription();
                    ImageGalleryActivity.show(getActivity(),ApiHttpClient.getAbsoluteApiUrl(mPerformance.getFileList().get(position).getPath()),description);
                }
            };
            for (int i = 0; i < mPerformance.getFileList().size(); i++) {
                ImageView mImage = new ImageView(getActivity());
                GridLayout.LayoutParams mParams = new GridLayout.LayoutParams();
                mParams.setMargins(0, (int) TDevice.dpToPixel(8), (int) TDevice.dpToPixel(8), 0);
                mParams.width = (int) TDevice.dpToPixel(80);
                mParams.height = (int) TDevice.dpToPixel(80);
                mImage.setLayoutParams(mParams);
                mImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mLayoutGrid.addView(mImage);
                getImageLoader()
                        .load(ApiHttpClient.getAbsoluteApiUrl(mPerformance.getFileList().get(i).getPath()))
                        .asBitmap()
                        .placeholder(R.color.gray)
                        .error(R.drawable.ic_default_image)
                        .into(mImage);
                mImage.setTag(i);
                mImage.setOnClickListener(l);
            }
        }
        tvStatue.setText(mPerformance.getStatusString());
        String optionion = "";
        if (mPerformance.getVerifyOpinions()!=null){
            boolean flage = false;
            for (int i=0;i< mPerformance.getVerifyOpinions().size();i++){
                if (flage) optionion+="\n\n";
                else flage = true;
                VerifyOpinion eachOpinion = mPerformance.getVerifyOpinions().get(i);
                optionion += eachOpinion.getOpinionPerson()+":"+eachOpinion.getOpinion();
            }
        }
        tvOpinion.setText(optionion);
    }
}
