package com.scau.easyfarm.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.R;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.bean.FileResource;
import com.scau.easyfarm.bean.Performance;
import com.scau.easyfarm.bean.ResultBean;
import com.scau.easyfarm.util.DialogHelp;
import com.scau.easyfarm.util.ImageUtils;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.SimpleTextWatcher;
import com.scau.easyfarm.util.StringUtils;
import com.scau.easyfarm.util.TDevice;
import com.scau.easyfarm.util.UIHelper;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by ChenHehong on 2016/9/2.
 */
public class PerformanceAddFragment extends BaseFragment{

    @InjectView(R.id.btn_add_file)
    Button btnAddFile;
    @InjectView(R.id.btn_select_type)
    Button btnSelectType;
    @InjectView(R.id.et_applyman)
    EditText etApplyMan;
    @InjectView(R.id.et_type)
    EditText etType;
    @InjectView(R.id.et_server_date)
    EditText etServerDate;
    @InjectView(R.id.lay_worktime)
    View layWorkTime;
    @InjectView(R.id.et_apply_worktime)
    EditText etApplyWorkTime;
    @InjectView(R.id.tv_worktime_unit)
    TextView workTimeUnit;
    @InjectView(R.id.btn_submit)
    Button btnSubmit;

    @InjectView(R.id.rl_img1) View mlyImage1;
    @InjectView(R.id.rl_img2) View mlyImage2;
    @InjectView(R.id.rl_img3) View mlyImage3;
    @InjectView(R.id.rl_img4) View mlyImage4;
    @InjectView(R.id.rl_img5) View mlyImage5;
    @InjectView(R.id.rl_img6) View mlyImage6;
    @InjectView(R.id.rl_img7) View mlyImage7;
    @InjectView(R.id.rl_img8) View mlyImage8;
    @InjectView(R.id.rl_img9) View mlyImage9;
    @InjectView(R.id.iv_img1) ImageView mIvImage1;
    @InjectView(R.id.iv_img2) ImageView mIvImage2;
    @InjectView(R.id.iv_img3) ImageView mIvImage3;
    @InjectView(R.id.iv_img4) ImageView mIvImage4;
    @InjectView(R.id.iv_img5) ImageView mIvImage5;
    @InjectView(R.id.iv_img6) ImageView mIvImage6;
    @InjectView(R.id.iv_img7) ImageView mIvImage7;
    @InjectView(R.id.iv_img8) ImageView mIvImage8;
    @InjectView(R.id.iv_img9) ImageView mIvImage9;
    @InjectView(R.id.iv_clear_img1) ImageView mIvClearImage1;
    @InjectView(R.id.iv_clear_img2) ImageView mIvClearImage2;
    @InjectView(R.id.iv_clear_img3) ImageView mIvClearImage3;
    @InjectView(R.id.iv_clear_img4) ImageView mIvClearImage4;
    @InjectView(R.id.iv_clear_img5) ImageView mIvClearImage5;
    @InjectView(R.id.iv_clear_img6) ImageView mIvClearImage6;
    @InjectView(R.id.iv_clear_img7) ImageView mIvClearImage7;
    @InjectView(R.id.iv_clear_img8) ImageView mIvClearImage8;
    @InjectView(R.id.iv_clear_img9) ImageView mIvClearImage9;
    private ArrayList<View> mlyImageList;
    private ArrayList<ImageView> mIvImageList;
    private ArrayList<ImageView> mIvClearImageList;
    private ArrayList<String> imagePathList = new ArrayList<String>();
    private ArrayList<Bitmap> imageBitmapList = new ArrayList<Bitmap>();

    private AlertDialog.Builder datePickBuilder;
    private DatePicker datePicker;
    private Dialog dateTimeDialog;

    private String performanceTypeStr;
    private int performanceTypeId;

    public static int REQUESTCODE_PERFORMANCE_ADD = 131;

    public static final int MAXPICTURENUM = 9;

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

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                refreshImages();
            }
        }
    };


    private void handleResultBean(ResultBean resultBean){
        if (resultBean.getResult().OK()){
            hideWaitDialog();
            AppContext.showToastShort("发布成功！");
            getActivity().setResult(getActivity().RESULT_OK);
            getActivity().finish();
        }else {
            hideWaitDialog();
            AppContext.showToast(resultBean.getResult().getErrorMessage());
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_performance_add,container,false);
        initView(view);
        return view;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        ButterKnife.inject(this, view);
        setHasOptionsMenu(true);
        btnAddFile.setOnClickListener(this);
        btnSelectType.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        etServerDate.setOnClickListener(this);
        mlyImageList = new ArrayList<View>(){{add(mlyImage1);add(mlyImage2);add(mlyImage3);add(mlyImage4);;add(mlyImage5);add(mlyImage6);add(mlyImage7);;add(mlyImage8);add(mlyImage9);}} ;
        mIvImageList = new ArrayList<ImageView>(){{add(mIvImage1);add(mIvImage2);add(mIvImage3);add(mIvImage4);add(mIvImage5);add(mIvImage6);add(mIvImage7);add(mIvImage8);add(mIvImage9);}};
        mIvClearImageList = new ArrayList<ImageView>(){{add(mIvClearImage1);add(mIvClearImage2);add(mIvClearImage3);add(mIvClearImage4);add(mIvClearImage5);add(mIvClearImage6);add(mIvClearImage7);add(mIvClearImage8);add(mIvClearImage9);}};
        for (int i=0;i<mIvClearImageList.size();i++){
            mIvClearImageList.get(i).setOnClickListener(this);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initData();
    }

    @Override
    public void initData(){
//      申请人默认为成员并设置为领导
        etApplyMan.setText(AppContext.getInstance().getLoginUser().getRealName());
    }

    @Override
    public boolean onBackPressed() {
        DialogHelp.getConfirmDialog(getActivity(), "确定离开?", new DialogInterface.OnClickListener() {

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
        if (etType.getText().toString().length()==0||etType.getText().toString()==null){
            AppContext.showToast("请选择业绩类型");
            return;
        }
        if (etServerDate.getText().toString().length()==0|| etServerDate.getText().toString()==null){
            AppContext.showToast("请选择服务时间");
            return;
        }
        if (etApplyWorkTime.getText().toString().length()==0||etApplyWorkTime.getText().toString()==null){
            AppContext.showToast("请填写申报的工作量");
            return;
        }
        if (!StringUtils.isDouble(etApplyWorkTime.getText().toString())&&!StringUtils.isInteger(etApplyWorkTime.getText().toString())){
            AppContext.showToast("申报的工作量格式填写错误");
            return;
        }
        if (Float.valueOf(etApplyWorkTime.getText().toString())<0){
            AppContext.showToast("申报的工作量不能小于0");
            return;
        }
        Performance performance = new Performance();
        if (imagePathList!=null&&imagePathList.size()>0){
            ArrayList<FileResource> fileResourceArrayList = new ArrayList<FileResource>();
            for (int i=0;i<imagePathList.size();i++){
                FileResource fileResource = new FileResource();
                fileResource.setPath(imagePathList.get(i));
                fileResourceArrayList.add(fileResource);
            }
            performance.setFileList(fileResourceArrayList);
        }
        performance.setApplyWorkTime(Float.valueOf(etApplyWorkTime.getText().toString()));
        performance.setPerformanceServerDate(etServerDate.getText().toString());
        performance.setPerformanceTypeId(performanceTypeId);
        performance.setPerformanceTypeStr(etType.getText().toString());
        showWaitDialog("发送申请中，请稍后");
        EasyFarmServerApi.addPerformanceApply(mHandler,performance);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id==R.id.btn_select_type){
            handleSelectType();
        }else if (id==R.id.et_server_date){
            handleSelectStartDate();
        }else if (id==R.id.btn_add_file){
            handleAddFiles();
        }else if(id==R.id.btn_submit){
            handleSubmit();
        }else if (mIvClearImageList.contains(v)){
            int i = mIvClearImageList.indexOf(v);
            imagePathList.remove(i);
            imageBitmapList.remove(i);
            refreshImages();
        }
    }

    public void refreshImages(){
//      先隐藏掉所有的图片
        for (int i=0;i<mlyImageList.size();i++){
            mlyImageList.get(i).setVisibility(View.GONE);
        }
//      重新加载图片
        for (int i=0;i<imageBitmapList.size();i++){
            mIvImageList.get(i).setImageBitmap(imageBitmapList.get(i));
            mlyImageList.get(i).setVisibility(View.VISIBLE);
        }
    }

    public void handleSelectType(){
        UIHelper.choosePerformanceType(this, PerformanceTypeChooseFragment.REQUEST_CODE_PERFORMANCETYPE_SELECT);
    }

    private void handleAddFiles(){
        MultiImageSelector.create(this.getContext())
                .showCamera(true).count(MAXPICTURENUM).multi().origin(imagePathList).start(this, ImageUtils.REQUEST_CODE_MULTISELECT_PICTURE);
    }

    private void handleSelectStartDate(){
        datePickBuilder = new AlertDialog.Builder(getActivity());
        View dateAlertView = View.inflate(getActivity(),R.layout.date_time_dialog,null);
        datePicker = (DatePicker)dateAlertView.findViewById(R.id.date_picker);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);
        datePickBuilder.setView(dateAlertView);
        datePickBuilder.setTitle("选取服务时间");
        datePickBuilder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                StringBuffer sb = new StringBuffer();
                sb.append(String.format("%d-%02d-%02d",
                        datePicker.getYear(),
                        datePicker.getMonth() + 1,
                        datePicker.getDayOfMonth()));
                etServerDate.setText(sb);
                dialog.dismiss();
            }
        });
        if (dateTimeDialog!=null)
            dateTimeDialog.dismiss();
        dateTimeDialog = datePickBuilder.create();
        dateTimeDialog.show();
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent returnIntent) {
        if (resultCode != Activity.RESULT_OK)
            return;
        if (requestCode==PerformanceTypeChooseFragment.REQUEST_CODE_PERFORMANCETYPE_SELECT){
            performanceTypeStr = returnIntent.getStringExtra(PerformanceTypeChooseFragment.BUNDLE_SELECT_TYPE_STR);
            performanceTypeId = returnIntent.getIntExtra(PerformanceTypeChooseFragment.BUNDLE_SELECT_TYPE_ID, 0);
            etType.setText(performanceTypeStr);
            if (performanceTypeStr.contains("其他")){
                etType.setEnabled(true);
            }else {
                etType.setEnabled(false);
            }
            layWorkTime.setVisibility(View.VISIBLE);
            workTimeUnit.setText(returnIntent.getStringExtra(PerformanceTypeChooseFragment.BUNDLE_SELECT_WORKUNIT));
        }else if (requestCode==ImageUtils.REQUEST_CODE_MULTISELECT_PICTURE){
            imagePathList =  returnIntent.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            new Thread() {
                @Override
                public void run() {
                    //  压缩成bitmap形式的缩略图
                    Bitmap bitmap = null;
                    imageBitmapList.clear();
                    for (int i=0;i<imagePathList.size();i++){
                        bitmap = ImageUtils.loadImgThumbnail(imagePathList.get(i),100,100);
                        imageBitmapList.add(bitmap);
                    }
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            }.start();
        }
    }
}
