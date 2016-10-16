package com.scau.easyfarm.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.R;
import com.scau.easyfarm.adapter.SelectedUserAdapter;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.bean.Entity;
import com.scau.easyfarm.bean.ResultBean;
import com.scau.easyfarm.bean.User;
import com.scau.easyfarm.bean.VillageService;
import com.scau.easyfarm.util.DialogHelp;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.SimpleTextWatcher;
import com.scau.easyfarm.util.TDevice;
import com.scau.easyfarm.util.UIHelper;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cz.msebera.android.httpclient.Header;

/**
 * Created by ChenHehong on 2016/9/2.
 */
public class VillageServiceAddFragment extends BaseFragment{

    @InjectView(R.id.btn_add_person)
    Button btnAddPerson;
    @InjectView(R.id.btn_add_reason)
    Button btnAddReason;
    @InjectView(R.id.et_area)
    EditText etArea;
    @InjectView(R.id.et_address)
    EditText etAddress;
    @InjectView(R.id.et_reason)
    EditText etReason;
    @InjectView(R.id.et_business_date)
    EditText etBusinessDate;
    @InjectView(R.id.et_return_date)
    EditText etReturnDate;
    @InjectView(R.id.lv_person)
    ListView selectedUserListView;
    @InjectView(R.id.sp_server_type)
    Spinner spServerType;
    @InjectView(R.id.et_applyman)
    EditText applyMan;
    @InjectView(R.id.btn_submit)
    Button btnSubmit;

    private ArrayAdapter<String> spinnerAdapter;

    private MenuItem mSendMenu;
    private ArrayList<User> personArray = new ArrayList<User>();
    private SelectedUserAdapter selectedUserAdapter;

    private AlertDialog.Builder datePickBuilder;
    private DatePicker datePicker;

    public int reasonId;
    private String serverType;

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
        etArea.setOnClickListener(this);
        etBusinessDate.setOnClickListener(this);
        etReturnDate.setOnClickListener(this);
        etArea.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                updateMenuState();
            }
        });
        etAddress.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                updateMenuState();
            }
        });
        etReason.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                updateMenuState();
            }
        });
        if (selectedUserAdapter==null){
            selectedUserAdapter = new SelectedUserAdapter(this);
        }
        selectedUserListView.setAdapter(selectedUserAdapter);
        selectedUserAdapter.setData(personArray);
        setListViewHeight();

        spinnerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.my_spinner_item, VillageService.serverTypeArray);
//      simple_spinner_dropdown_item.xml设置的是下拉看到的效果
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// 设置下拉风格
        spServerType.setAdapter(spinnerAdapter);
        spServerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                serverType = VillageService.serverTypeArray[position];
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.submit_menu,menu);
        mSendMenu = menu.findItem(R.id.public_menu_send);
        updateMenuState();
    }

    public void updateMenuState() {
        if (mSendMenu == null) {
            return;
        }
        if (etAddress.getText().length() > 0&&etArea.getText().length()>0&&etReason.getText().length()>0) {
            mSendMenu.setEnabled(true);
            mSendMenu.setIcon(R.drawable.actionbar_send_icon);
        } else {
            mSendMenu.setEnabled(false);
            mSendMenu.setIcon(R.drawable.actionbar_unsend_icon);
        }
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
        if (etBusinessDate.getText().toString().length()==0||etBusinessDate.getText().toString()==null){
            AppContext.showToast("请选择服务时间");
            return;
        }
        if (etReturnDate.getText().toString().length()==0||etReturnDate.getText().toString()==null){
            AppContext.showToast("请选择返回时间");
            return;
        }
        if (etBusinessDate.getText().toString().compareTo(etReturnDate.getText().toString())>0){
            AppContext.showToast("返回时间不能大于服务时间");
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
        int serverTypeId = VillageService.serverTypeStrMap.get(serverType);
        showWaitDialog("发送申请中，请稍后");
        EasyFarmServerApi.addVillageService(etArea.getText().toString(),etAddress.getText().toString(),reasonId,etReason.getText().toString(),
                etBusinessDate.getText().toString(),etReturnDate.getText().toString(),villageServiceUserIds,serverLeaderIds,serverTypeId,mHandler);
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
        }else if(id==R.id.btn_submit){
            handleSubmit();
        }
    }

    public void handleSelectReason(){
        UIHelper.chooseVillageServiceReason(this, VillageServiceReasonChooseFragment.REQUEST_CODE_VSREASON_SELECT);
    }

    private void handleSelectArea(){
        UIHelper.showAreaChoose(this, AreaCatalogListFragment.AREA_REQUEST_CODE);
    }

    private void handleSelectBusinessDate(){
        datePickBuilder = new AlertDialog.Builder(getActivity());
        View dateAlertView = View.inflate(getActivity(),R.layout.date_time_dialog,null);
        datePicker = (DatePicker)dateAlertView.findViewById(R.id.date_picker);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);
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
                etBusinessDate.setText(sb);
                etReturnDate.requestFocus();
                dialog.dismiss();
            }
        });
        Dialog dialog = datePickBuilder.create();
        dialog.show();
    }

    private void handleSelectReturnDate(){
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
                etReturnDate.setText(sb);
                dialog.dismiss();
            }
        });
        Dialog dialog = datePickBuilder.create();
        dialog.show();
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
