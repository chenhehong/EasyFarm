package com.scau.easyfarm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.R;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.bean.SimpleBackPage;
import com.scau.easyfarm.util.UIHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by ChenHehong on 2016/6/11.
 */
public class VillageFunctionFragment extends BaseFragment{

    @InjectView(R.id.ImgVillageServiceApply)
    ImageView imgVillageServiceApply;
    @InjectView(R.id.ImgVillageServiceProof)
    ImageView imgVillageServiceProof;
    @InjectView(R.id.ImgVilageServiceVerify)
    ImageView imgVilageServiceVerify;
    @InjectView(R.id.ImgVilageServiceStatistic)
    ImageView imgVilageServiceStatistic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_village_function, container, false);
        ButterKnife.inject(this, view);
        initView(view);
        return view;
    }

    @Override
    public void initView(View view) {
        imgVillageServiceApply.setOnClickListener(this);
        imgVillageServiceProof.setOnClickListener(this);
        imgVilageServiceVerify.setOnClickListener(this);
        imgVilageServiceStatistic.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.ImgVillageServiceApply:
                showVillageServiceApply();
                break;
            case R.id.ImgVillageServiceProof:
                showVillageServiceProofList();
                break;
            case R.id.ImgVilageServiceVerify:
                showgVilageServiceVerify();
                break;
            case R.id.ImgVilageServiceStatistic:
                showgVilageServiceStatistic();
                break;
            default:
                break;
        }
    }

    private void showVillageServiceApply(){
        UIHelper.showVillageServiceApply(getActivity());
    }

    private void showVillageServiceProofList(){
        UIHelper.showVillageServicProofList(getActivity());
    }

    private void showgVilageServiceVerify(){
        if ( !AppContext.getInstance().getLoginUser().getRoleName().equals("超级管理员")){
            AppContext.showToast("权限不够");
            return;
        }
        UIHelper.showSimpleBack(getActivity(), SimpleBackPage.VILLAGE_SERVICE_ALL_LIST);
    }

    private void showgVilageServiceStatistic(){
        if ( !AppContext.getInstance().getLoginUser().getRoleName().equals("超级管理员")){
            AppContext.showToast("权限不够");
            return;
        }
        UIHelper.showAllVillageServiceProofList(this);
    }

}
