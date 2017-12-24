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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.R;
import com.scau.easyfarm.adapter.SelectedUserAdapter;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.bean.Entity;
import com.scau.easyfarm.bean.ResultBean;
import com.scau.easyfarm.bean.User;
import com.scau.easyfarm.util.DateTimeUtil;
import com.scau.easyfarm.util.DialogHelp;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.SimpleTextWatcher;
import com.scau.easyfarm.util.TDevice;
import com.scau.easyfarm.util.UIHelper;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by ChenHehong on 2016/9/2.
 */
public class VillageServiceAddFragment extends BaseFragment{

    @InjectView(R.id.btn_add_person)
    Button btnAddPerson;
    @InjectView(R.id.btn_add_reason)
    Button btnAddReason;
    @InjectView(R.id.btn_add_type)
    Button btnAddType;
    @InjectView(R.id.et_area)
    EditText etArea;
    @InjectView(R.id.et_address)
    EditText etAddress;
    @InjectView(R.id.et_reason)
    EditText etReason;
    @InjectView(R.id.et_type)
    EditText etType;
    @InjectView(R.id.et_business_date)
    EditText etBusinessDate;
    @InjectView(R.id.et_return_date)
    EditText etReturnDate;
    @InjectView(R.id.lv_person)
    ListView selectedUserListView;
    @InjectView(R.id.et_applyman)
    EditText applyMan;
    @InjectView(R.id.btn_submit)
    Button btnSubmit;

    private ArrayList<User> personArray = new ArrayList<User>();
    private SelectedUserAdapter selectedUserAdapter;

    private AlertDialog.Builder datePickBuilder;
    private DatePicker datePicker;
    private Dialog dateTimeDialog;

    public int reasonId;
    public int typeId;

    public static int REQUESTCODE_SERVICE_ADD = 131;

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
        View view = inflater.inflate(R.layout.fragment_village_service_add,container,false);
        initView(view);
        return view;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        ButterKnife.inject(this, view);
        setHasOptionsMenu(true);
        btnAddPerson.setOnClickListener(this);
        btnAddReason.setOnClickListener(this);
        btnAddType.setOnClickListener(this);
        etArea.setOnClickListener(this);
        etBusinessDate.setOnClickListener(this);
        etReturnDate.setOnClickListener(this);
        if (selectedUserAdapter==null){
            selectedUserAdapter = new SelectedUserAdapter(this);
        }
        selectedUserListView.setAdapter(selectedUserAdapter);
        selectedUserAdapter.setData(personArray);
        setListViewHeight();

        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initData();
    }

    @Override
    public void initData(){
//      申请人默认为成员并设置为领导
        applyMan.setText(AppContext.getInstance().getLoginUser().getRealName());
        User u = AppContext.getInstance().getLoginUser();
        u.setIsServerLeader(true);
        personArray.add(u);
        selectedUserAdapter.notifyDataSetChanged();
        setListViewHeight();
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

    private void handleSubmit() {
        if (!TDevice.hasInternet()) {
            AppContext.showToastShort(R.string.tip_network_error);
            return;
        }
        if (!AppContext.getInstance().isLogin()) {
            UIHelper.showLoginActivity(getActivity());
            return;
        }
        if (personArray.size()==0||personArray==null){
            AppContext.showToast("请添加服务人员后再提交！");
            return;
        }
        ArrayList<Integer> leaderIdArray = new ArrayList<Integer>();
        for (int i=0;i<personArray.size();i++){
            if (personArray.get(i).isServerLeader()){
                leaderIdArray.add(personArray.get(i).getId());
            }
        }
        if (leaderIdArray.size()==0||leaderIdArray==null){
            AppContext.showToast("请至少选择一名领队！");
            return;
        }
        if (etArea.getText().toString().length()==0||etArea.getText().toString()==null){
            AppContext.showToast("请选择下乡区域");
            return;
        }
        if (etAddress.getText().toString().length()==0||etAddress.getText().toString()==null){
            AppContext.showToast("请填写下乡详细地址");
            return;
        }
        if (etReason.getText().toString().length()==0||etReason.getText().toString()==null){
            AppContext.showToast("请选择服务方式");
            return;
        }
        if (etType.getText().toString().length()==0||etType.getText().toString()==null){
            AppContext.showToast("请选择服务类型");
            return;
        }
        if (etBusinessDate.getText().toString().length()==0||etBusinessDate.getText().toString()==null){
            AppContext.showToast("请选择服务时间");
            return;
        }
        if (etReturnDate.getText().toString().length()==0||etReturnDate.getText().toString()==null){
            AppContext.showToast("请选择返回时间");
            return;
        }
        if (etBusinessDate.getText().toString().compareTo(DateTimeUtil.getCurrentDateStr(""))<0){
            AppContext.showToast("服务时间不能小于当前时间");
            return;
        }
        if (etBusinessDate.getText().toString().compareTo(etReturnDate.getText().toString())>0){
            AppContext.showToast("服务时间不能大于返回时间");
            return;
        }
        String villageServiceUserIds = "";
        boolean flag = false;
        for (int i=0;i<personArray.size();i++){
            if (flag) villageServiceUserIds+=",";
            else flag=true;
            villageServiceUserIds+=personArray.get(i).getId();
        }
        String serverLeaderIds = "";
        flag = false;
        for (int i=0;i<leaderIdArray.size();i++){
            if (flag) serverLeaderIds+=",";
            else flag=true;
            serverLeaderIds += leaderIdArray.get(i);
        }
        showWaitDialog("发送申请中，请稍后");
        EasyFarmServerApi.addVillageService(etArea.getText().toString(),etAddress.getText().toString(),reasonId,etReason.getText().toString(),
                etBusinessDate.getText().toString(),etReturnDate.getText().toString(),villageServiceUserIds,serverLeaderIds,typeId,mHandler);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id==R.id.btn_add_person){
            handleAddPerson();
        }else if (id==R.id.et_business_date){
            handleSelectBusinessDate();
        }else if (id==R.id.et_return_date){
            handleSelectReturnDate();
        }else if (id==R.id.et_area){
            handleSelectArea();
        }else if (id==R.id.btn_add_reason){
            handleSelectReason();
        }else if (id==R.id.btn_add_type){
            handleSelectType();
        }else if(id==R.id.btn_submit){
            handleSubmit();
        }
    }

    public void handleSelectReason(){
        UIHelper.chooseVillageServiceReason(this, VillageServiceReasonChooseFragment.REQUEST_CODE_VSREASON_SELECT);
    }

    public void handleSelectType(){
        UIHelper.chooseVillageServiceType(this, VillageServiceTypeChooseFragment.REQUEST_CODE_VSTYPE_SELECT);
    }

    private void handleSelectArea(){
        UIHelper.showAreaChoose(this, AreaCatalogListFragment.AREA_REQUEST_CODE);
    }

    private void handleSelectBusinessDate(){
        datePickBuilder = new AlertDialog.Builder(getActivity());
        View dateAlertView = View.inflate(getActivity(),R.layout.date_time_dialog,null);
        datePicker = (DatePicker)dateAlertView.findViewById(R.id.date_picker);
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
                etBusinessDate.setText(sb);
                etReturnDate.requestFocus();
                dialog.dismiss();
            }
        });
        if (dateTimeDialog!=null)
            dateTimeDialog.dismiss();
        dateTimeDialog = datePickBuilder.create();
        dateTimeDialog.show();
    }

    private void handleSelectReturnDate(){
        datePickBuilder = new AlertDialog.Builder(getActivity());
        View dateAlertView = View.inflate(getActivity(),R.layout.date_time_dialog,null);
        datePicker = (DatePicker)dateAlertView.findViewById(R.id.date_picker);
        datePickBuilder.setView(dateAlertView);
        datePickBuilder.setTitle("选取返回时间");
        datePickBuilder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                StringBuffer sb = new StringBuffer();
                sb.append(String.format("%d-%02d-%02d",
                        datePicker.getYear(),
                        datePicker.getMonth() + 1,
                        datePicker.getDayOfMonth()));
                etReturnDate.setText(sb);
                dialog.dismiss();
            }
        });
        if (dateTimeDialog!=null)
            dateTimeDialog.dismiss();
        dateTimeDialog = datePickBuilder.create();
        dateTimeDialog.show();
    }

    private void handleAddPerson(){
        UIHelper.findUser(this, FindUserFragment.REQUESTCODE_FIND_USER);
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
        }else if (requestCode==FindUserFragment.REQUESTCODE_FIND_USER){
            int selectedUserId = returnIntent.getIntExtra(FindUserFragment.BUNDLE_SELECT_USER_ID,0);
            String selectedUserName = returnIntent.getStringExtra(FindUserFragment.BUNDLE_SELECT_USER_NAME);
            User user = new User();
            user.setId(selectedUserId);
            user.setRealName(selectedUserName);
            if (compareToEntity(personArray,user)) return;
            personArray.add(user);
            selectedUserAdapter.notifyDataSetChanged();
            setListViewHeight();
        }else if (requestCode==VillageServiceReasonChooseFragment.REQUEST_CODE_VSREASON_SELECT){
            String selectedReason = returnIntent.getStringExtra(VillageServiceReasonChooseFragment.BUNDLE_SELECT_REASON_STR);
            reasonId = returnIntent.getIntExtra(VillageServiceReasonChooseFragment.BUNDLE_SELECT_REASON_ID,0);
            if (selectedReason.equals("其他")){
                etReason.setEnabled(true);
                etReason.setText("其他");
            }else {
                etReason.setEnabled(false);
                etReason.setText(selectedReason);
            }
        }else if (requestCode==VillageServiceTypeChooseFragment.REQUEST_CODE_VSTYPE_SELECT){
            String selectedType = returnIntent.getStringExtra(VillageServiceTypeChooseFragment.BUNDLE_SELECT_TEXT_STR);
            typeId = returnIntent.getIntExtra(VillageServiceTypeChooseFragment.BUNDLE_SELECT_TEXT_ID,0);
            etType.setText(selectedType);
        }
    }

    private boolean compareToEntity(List<? extends Entity> data, User enity) {
        int s = data.size();
        if (enity != null) {
            for (int i = 0; i < s; i++) {
                if (enity.getId()==((User)data.get(i)).getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setListViewHeight(){
        int h = 0;
        for (int i=0;i<selectedUserAdapter.getCount();i++){
            View item = selectedUserAdapter.getView(i,null,selectedUserListView);
            item.measure(0,0);
            h+=item.getMeasuredHeight();
        }
        ViewGroup.LayoutParams lp = selectedUserListView.getLayoutParams();
        lp.height=h+(selectedUserListView.getDividerHeight()*(selectedUserAdapter.getCount()-1));
        selectedUserListView.setLayoutParams(lp);
    }
}
