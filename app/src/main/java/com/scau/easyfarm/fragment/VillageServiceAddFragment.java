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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

    private MenuItem mSendMenu;
    private ArrayList<User> personArray = new ArrayList<User>();
    private SelectedUserAdapter selectedUserAdapter;

    private AlertDialog.Builder datePickBuilder;
    private DatePicker datePicker;

    public int reasonId;

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
        //      测试start
//        User m1 = new User();
//        m1.setRealName("陈河宏");
//        m1.setId(2012);
//        User m2 = new User();
//        m2.setRealName("李林");
//        m2.setId(2013);
//        User m3 = new User();
//        m3.setRealName("李刚");
//        m3.setId(2014);
//        User m4 = new User();
//        m4.setRealName("刘备");
//        m4.setId(2015);
//        User m5 = new User();
//        m5.setRealName("项羽");
//        m5.setId(2016);
//        User m6 = new User();
//        m6.setRealName("孙权");
//        m6.setId(2017);
//        ArrayList<User> mList = new ArrayList<User>();
//        mList.add(m1);mList.add(m2);mList.add(m3);mList.add(m4);mList.add(m5);mList.add(m6);
//        personArray = mList;
//      测试end
        if (selectedUserAdapter==null){
            selectedUserAdapter = new SelectedUserAdapter(this);
        }
        selectedUserListView.setAdapter(selectedUserAdapter);
        selectedUserAdapter.setData(personArray);
        setListViewHeight();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        if (etBusinessDate.getText().toString().compareTo(etReturnDate.getText().toString())>0){
            AppContext.showToast("返回时间不能大于服务时间");
            return;
        }
        String villageServiceUserIds = "";
        for (int i=0;i<personArray.size();i++){
            if (i==(personArray.size()-1)){
                villageServiceUserIds+=personArray.get(i).getId();
            }else {
                villageServiceUserIds+=personArray.get(i).getId()+",";
            }
        }
        showWaitDialog("发送申请中，请稍后");
        EasyFarmServerApi.addVillageService(etArea.getText().toString(),etAddress.getText().toString(),reasonId,etReason.getText().toString(),
                etBusinessDate.getText().toString(),etReturnDate.getText().toString(),villageServiceUserIds,mHandler);
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
                etReason.setText("");
                etReason.setHint("请填写其他事由");
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
