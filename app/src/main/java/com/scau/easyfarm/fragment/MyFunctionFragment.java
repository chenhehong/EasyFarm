package com.scau.easyfarm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.scau.easyfarm.R;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.util.UIHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by ChenHehong on 2016/6/11.
 */
public class MyFunctionFragment extends BaseFragment{

    @InjectView(R.id.ImgVillageServiceApply)
    ImageView imgVillageServiceApply;
    @InjectView(R.id.ImgVillageServiceProof)
    ImageView imgVillageServiceProof;
    @InjectView(R.id.ImgVilageServicePermance)
    ImageView imgVilageServicePermance;
    @InjectView(R.id.ImgKnowledgeBase)
    ImageView imgKnowledgeBase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_function,container, false);
        ButterKnife.inject(this, view);
        initView(view);
        return view;
    }

    @Override
    public void initView(View view) {
        imgVillageServiceApply.setOnClickListener(this);
        imgVillageServiceProof.setOnClickListener(this);
        imgVilageServicePermance.setOnClickListener(this);
        imgKnowledgeBase.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.ImgVillageServiceApply:
                showVillageServiceApply();
                break;
            case R.id.ImgVillageServiceProof:
                showVillageServiceProof();
                break;
            case R.id.ImgVilageServicePermance:
                showgVilageServicePermance();
                break;
            case R.id.ImgKnowledgeBase:
                showKnowledgeBase();
                break;
            default:
                break;
        }
    }

    private void showVillageServiceApply(){
        UIHelper.showVillageServiceApply(getActivity());
    }

    private void showVillageServiceProof(){

    }

    private void showgVilageServicePermance(){

    }

    private void showKnowledgeBase(){
        UIHelper.showManualCategory(this,0);
    }
}
