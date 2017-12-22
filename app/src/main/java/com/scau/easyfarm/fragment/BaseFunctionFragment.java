package com.scau.easyfarm.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scau.easyfarm.R;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.bean.Constants;
import com.scau.easyfarm.bean.Module;
import com.scau.easyfarm.util.UIHelper;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by ChenHehong on 2016/6/11.
 */
public abstract class BaseFunctionFragment extends BaseFragment{

    @InjectView(R.id.row1) View row1;
    @InjectView(R.id.row2) View row2;
    @InjectView(R.id.icon1)  ImageView icon1;
    @InjectView(R.id.icon2)  ImageView icon2;
    @InjectView(R.id.icon3)  ImageView icon3;
    @InjectView(R.id.icon4)  ImageView icon4;
    @InjectView(R.id.icon5)  ImageView icon5;
    @InjectView(R.id.icon6)  ImageView icon6;
    @InjectView(R.id.icon7)  ImageView icon7;
    @InjectView(R.id.icon8)  ImageView icon8;
    @InjectView(R.id.iconText1) TextView iconText1;
    @InjectView(R.id.iconText2) TextView iconText2;
    @InjectView(R.id.iconText3) TextView iconText3;
    @InjectView(R.id.iconText4) TextView iconText4;
    @InjectView(R.id.iconText5) TextView iconText5;
    @InjectView(R.id.iconText6) TextView iconText6;
    @InjectView(R.id.iconText7) TextView iconText7;
    @InjectView(R.id.iconText8) TextView iconText8;

    public static final int MAXMODULE = 8;
    public ArrayList<Module> moduleList = new ArrayList<>();
    public ArrayList<ImageView> iconList;
    public ArrayList<TextView>  iconTextList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initModuleList();
//      监听用户登录状态的广播
        IntentFilter filter = new IntentFilter(
                Constants.INTENT_ACTION_USER_CHANGE);
        filter.addAction(Constants.INTENT_ACTION_LOGOUT);
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    //  用户登录状态广播接收器
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setupContent();
        }
    };

    //  处理用户登录状态广播
    private void setupContent() {
        moduleList.clear();
        initModuleList();
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_function,container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initData();
    }

    @Override
    public void initView(View view) {
    }

    @Override
    public void initData() {
        iconList = new ArrayList<ImageView>(){{add(icon1);add(icon2);add(icon3);add(icon4);add(icon5);add(icon6);add(icon7);add(icon8);}};
        iconTextList = new ArrayList<TextView>(){{add(iconText1);add(iconText2);add(iconText3);add(iconText4);add(iconText5);add(iconText6);add(iconText7);add(iconText8);}};
        for (int i=0;i<moduleList.size();i++){
            final int position = i;
            iconList.get(position).setImageResource(moduleList.get(position).getIcon());
            iconTextList.get(position).setText(moduleList.get(position).getName());
            iconList.get(position).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIHelper.showSimpleBack(getActivity(),moduleList.get(position).getSimpleBackPage(),moduleList.get(position).getArgs());
                }
            });
        }
//      设置显示的行数
        if (moduleList.size()>0)
            row1.setVisibility(View.VISIBLE);
        if (moduleList.size()>4)
            row2.setVisibility(View.VISIBLE);
    }

    public abstract void initModuleList();

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            default:
                break;
        }
    }
}
