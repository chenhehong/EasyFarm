package com.scau.easyfarm.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
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
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.R;
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
    @InjectView(R.id.person1)
    LinearLayout layPerson1;
    @InjectView(R.id.person2)
    LinearLayout layPerson2;
    @InjectView(R.id.person3)
    LinearLayout layPerson3;
    @InjectView(R.id.person4)
    LinearLayout layPerson4;
    @InjectView(R.id.person5)
    LinearLayout layPerson5;
    @InjectView(R.id.et_person1)
    EditText etPerson1;
    @InjectView(R.id.et_person2)
    EditText etPerson2;
    @InjectView(R.id.et_person3)
    EditText etPerson3;
    @InjectView(R.id.et_person4)
    EditText etPerson4;
    @InjectView(R.id.et_person5)
    EditText etPerson5;
    @InjectView(R.id.btn_person1)
    ImageView btnPerson1;
    @InjectView(R.id.btn_person2)
    ImageView btnPerson2;
    @InjectView(R.id.btn_person3)
    ImageView btnPerson3;
    @InjectView(R.id.btn_person4)
    ImageView btnPerson4;
    @InjectView(R.id.btn_person5)
    ImageView btnPerson5;

    private MenuItem mSendMenu;
    private ArrayList<ImageView> btnPersonArray;
    private ArrayList<TextView> tvPersonArray;
    private ArrayList<EditText> etPersonArray;
    private ArrayList<LinearLayout> layPersonArray;
    private ArrayList<User> personArray = new ArrayList<User>();
    private int personCount = 0;
    private final int ARRAYLENGTH = 5;

    private AlertDialog.Builder datePickBuilder;
    private DatePicker datePicker;

    private final AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
            ResultBean resultBean = JsonUtils.toBean(ResultBean.class, arg2);
            if (resultBean != null) {
                handleResultBean(resultBean);
            }
        }

        @Override
        public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                              Throwable arg3) {
            AppContext.showToast("网络出错" + arg0);
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
        btnPerson1.setOnClickListener(this);
        btnPerson2.setOnClickListener(this);
        btnPerson3.setOnClickListener(this);
        btnPerson4.setOnClickListener(this);
        btnPerson5.setOnClickListener(this);
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

        btnPersonArray = new ArrayList<ImageView>(){{add(btnPerson1);add(btnPerson2);add(btnPerson3);add(btnPerson4);add(btnPerson5);}};
        etPersonArray = new ArrayList<EditText>(){{add(etPerson1);add(etPerson2);add(etPerson3);add(etPerson4);add(etPerson5);}};
        layPersonArray = new ArrayList<LinearLayout>(){{add(layPerson1);add(layPerson2);add(layPerson3);add(layPerson4);add(layPerson5);}};

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
        String villageServiceUserIds = "";
        for (int i=0;i<personArray.size();i++){
            if (i==(personArray.size()-1)){
                villageServiceUserIds+=personArray.get(i).getId();
            }else {
                villageServiceUserIds+=personArray.get(i).getId()+",";
            }
        }
        showWaitDialog("发送申请中，请稍后");
        EasyFarmServerApi.addVillageService(etArea.getText().toString(),etAddress.getText().toString(),etReason.getText().toString(),
                etBusinessDate.getText().toString(),etReturnDate.getText().toString(),villageServiceUserIds,mHandler);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id==R.id.btn_add_person){
            handleAddPerson();
        }else if (compareToView(btnPersonArray,id)){
            int index = btnPersonArray.indexOf((ImageView)v);
            personArray.remove(index);
            refreshPersonArray();
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
        datePickBuilder.setTitle("选取下乡时间");
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
        if (personArray.size()>=ARRAYLENGTH) return;
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
            refreshPersonArray();
        }else if (requestCode==VillageServiceReasonChooseFragment.REQUEST_CODE_VSREASON_SELECT){
            String selectedReason = returnIntent.getStringExtra(VillageServiceReasonChooseFragment.BUNDLE_SELECT_REASON_STR);
            if (selectedReason.equals("其他")){
                etReason.setEnabled(true);
                etReason.setText("");
                etReason.setHint("请填写其他事由");
            }else {
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

    private boolean compareToView(List<? extends View> data, int viewId) {
        int s = data.size();
        for (int i = 0; i < s; i++) {
            if (viewId==((View)data.get(i)).getId()) {
                return true;
            }
        }
        return false;
    }

    private void refreshPersonArray(){
        for (int i=0;i<ARRAYLENGTH;i++){
            layPersonArray.get(i).setVisibility(View.GONE);
        }
        for (int i=0;i<personArray.size();i++){
            layPersonArray.get(i).setVisibility(View.VISIBLE);
            etPersonArray.get(i).setText(personArray.get(i).getRealName());
        }
    }

    private View generatePersonLayout(){
        LinearLayout linearLayout = new LinearLayout(getActivity());

        return  linearLayout;
    }
}
