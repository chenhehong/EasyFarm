package com.scau.easyfarm.fragment;

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
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.R;
import com.scau.easyfarm.adapter.PerformanceUserWorkTimeAdapter;
import com.scau.easyfarm.adapter.SelectedUserAdapter;
import com.scau.easyfarm.api.ApiHttpClient;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.bean.Performance;
import com.scau.easyfarm.bean.PerformanceDetail;
import com.scau.easyfarm.bean.PerformanceMemberWorkTime;
import com.scau.easyfarm.bean.ResultBean;
import com.scau.easyfarm.bean.User;
import com.scau.easyfarm.bean.VerifyOpinion;
import com.scau.easyfarm.bean.VillageService;
import com.scau.easyfarm.bean.VillageServiceDetail;
import com.scau.easyfarm.ui.ImageGalleryActivity;
import com.scau.easyfarm.ui.empty.EmptyLayout;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.StringUtils;
import com.scau.easyfarm.util.TDevice;
import com.scau.easyfarm.util.UIHelper;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by chenhehong on 2016/9/9.
 */
public class PerformanceVerifyFragment extends BaseFragment {

    @InjectView(R.id.tv_applyman)
    TextView tvApplyMan;
    @InjectView(R.id.tv_type)
    TextView tvType;
    @InjectView(R.id.tv_server_date)
    TextView tvServerDate;
    @InjectView(R.id.tv_statue)
    TextView tvStatue;
    @InjectView(R.id.tv_optionion)
    TextView tvOpinion;
    @InjectView(R.id.tv_serverNumber)
    TextView tvServerNumber;
    @InjectView(R.id.tv_apply_worktime)
    TextView tvApplyWorkTime;
    @InjectView(R.id.tv_description)
    TextView tvDescription;
    @InjectView(R.id.layout_grid)
    GridLayout mLayoutGrid;
    @InjectView(R.id.sp_my_status)
    Spinner myStatusSpinner;
    @InjectView(R.id.et_my_optinion)
    EditText optinionEditText;
    @InjectView(R.id.lv_user_worktime)
    ListView userWorkTimeListView;
    @InjectView(R.id.error_layout)
    EmptyLayout mErrorLayout;

    private ArrayAdapter<String> spinnerAdapter;
    private PerformanceUserWorkTimeAdapter performanceUserWorkTimeAdapter;

    public static final String PERFORMANCE_ID_CODE = "performance_id_code";
    public static final int REQUESTCODE_VERIFY = 121;

    private int mPerformanceId;
    private Performance mPerformance;
    private int selectStatus;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_performance_verify,container,false);
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
        spinnerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.my_spinner_item, Performance.myStatusStrArray);
//      simple_spinner_dropdown_item.xml设置的是下拉看到的效果
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// 设置下拉风格
        myStatusSpinner.setAdapter(spinnerAdapter);
        myStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedStr = Performance.myStatusStrArray[position];
                selectStatus = Performance.myStatusStrMap.get(selectedStr);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        setHasOptionsMenu(true);

        if (performanceUserWorkTimeAdapter==null){
            performanceUserWorkTimeAdapter = new PerformanceUserWorkTimeAdapter(this);
        }
        userWorkTimeListView.setAdapter(performanceUserWorkTimeAdapter);

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
            Performance  performance = JsonUtils.toBean(PerformanceDetail.class,
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
        tvApplyMan.setText(mPerformance.getApplyManName());
        tvServerNumber.setText(mPerformance.getPerformanceCode());
        tvType.setText(mPerformance.getPerformanceTypeStr());
        tvServerDate.setText(mPerformance.getPerformanceServerDateDesc());
        tvApplyWorkTime.setText(mPerformance.getApplyWorkTime()+"");
        tvDescription.setText(mPerformance.getDescription());
        tvStatue.setText(mPerformance.getStatusString());
        if (mPerformance.getFileList() != null && (mPerformance.getFileList().size() > 0)) {
            mLayoutGrid.setVisibility(View.VISIBLE);
            mLayoutGrid.removeAllViews();
            final View.OnClickListener l = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    String description = mPerformance.getFileList().get(position).getTypeString()+":"+mPerformance.getFileList().get(position).getDescription();
                    ImageGalleryActivity.show(getActivity(), ApiHttpClient.getAbsoluteApiUrl(mPerformance.getFileList().get(position).getPath()),description);
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
        myStatusSpinner.setSelection(0);

        performanceUserWorkTimeAdapter.setData((ArrayList)mPerformance.getPerformanceMemberWorkTimeList());
        setListViewHeight();
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
        if (optinionEditText.getText().toString()==null||optinionEditText.getText().toString().length()==0){
            AppContext.showToastShort("请填写审核意见再提交！");
            return;
        }
        String userWorkTime="";
        boolean flag = false;
        for (int i=0;i<performanceUserWorkTimeAdapter.getDataSize();i++){
            if (flag) userWorkTime+=";";
            else flag=true;
            RelativeLayout lay = (RelativeLayout)userWorkTimeListView.getChildAt(i);
            String workTime = ((EditText) lay.findViewById(R.id.et_worktime)).getText().toString();// 从lay中获得控件,根据其id
            String userName = ((TextView)lay.findViewById(R.id.tv_name)).getText().toString();
            if (workTime==null||workTime.length()==0){
                AppContext.showToastShort(userName + "的工作时不能为空！");
            }
            if (!StringUtils.isDouble(workTime)&&!StringUtils.isInteger(workTime)){
                AppContext.showToastShort(userName+"的工作时格式错误！");
                return;
            }
            if (Float.valueOf(workTime)<0){
                AppContext.showToast(userName + "的工作时不能小于0");
                return;
            }
            if (Float.valueOf(workTime)>mPerformance.getApplyWorkTime()){
                AppContext.showToast(userName+"的工作时不能大于申报的工作量，请修改");
                return;
            }
            PerformanceMemberWorkTime p = performanceUserWorkTimeAdapter.getData().get(i);
            p.setRealWorkTime(Float.valueOf(workTime));
            userWorkTime+=p.getUserId()+"^"+p.getRealWorkTime();
        }
        showWaitDialog("提交中，请稍后");
        EasyFarmServerApi.verifyPerformance(mPerformanceId, selectStatus, optinionEditText.getText().toString(), userWorkTime,mSubmitHandler);
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

    public void setListViewHeight(){
        int h = 0;
        for (int i=0;i<performanceUserWorkTimeAdapter.getCount();i++){
            View item = performanceUserWorkTimeAdapter.getView(i,null,userWorkTimeListView);
            item.measure(0,0);
            h+=item.getMeasuredHeight();
        }
        ViewGroup.LayoutParams lp = userWorkTimeListView.getLayoutParams();
        lp.height=h+(userWorkTimeListView.getDividerHeight()*(performanceUserWorkTimeAdapter.getCount()-1));
        userWorkTimeListView.setLayoutParams(lp);
    }
}
