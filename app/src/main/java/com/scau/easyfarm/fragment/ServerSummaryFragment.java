package com.scau.easyfarm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.R;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.bean.VillageService;
import com.scau.easyfarm.util.StringUtils;

import java.io.ByteArrayInputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ServerSummaryFragment extends BaseFragment {

    @InjectView(R.id.et_summary)
    EditText mEtContent;

    private VillageService service;
    private int position;//用于标记listview的item索引，便于结束成功后移除item
    public static final String BUNDLEKEY_POSITION = "bundlekey_position";
    public static final String BUNDLEKEY_SERVICE = "bundlekey_service";
    public static final int REQUESTCODE_SERVERSUMMARY = 114;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_server_summary, null);
        ButterKnife.inject(this, view);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.submit_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.public_menu_send:
            String data = mEtContent.getText().toString();
            if (StringUtils.isEmpty(data)) {
                AppContext.showToast("你忘记写总结咯");
            } else {
                EasyFarmServerApi.sendServerSummary(data, service.getId(), new OperationResponseHandler() {
                    @Override
                    public void onSuccess(int code, ByteArrayInputStream is, Object[] args) {
                        AppContext.showToast("已收到你的总结，谢谢");
                        Intent intent = new Intent();
                        intent.putExtra(BUNDLEKEY_POSITION,position);
                        getActivity().setResult(getActivity().RESULT_OK, intent);
                        getActivity().finish();
                    }

                    @Override
                    public void onFailure(int code, String errorMessage, Object[] args) {
                        AppContext.showToast("网络异常，请稍后重试");
                    }
                });
            }
            break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void initData() {
        Bundle bundle = getArguments();
        if (bundle!=null){
            service = (VillageService) bundle.getSerializable(BUNDLEKEY_SERVICE);
            position = bundle.getInt(BUNDLEKEY_POSITION);
        }
    }
}
