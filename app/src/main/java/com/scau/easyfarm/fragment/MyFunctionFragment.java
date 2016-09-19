package com.scau.easyfarm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.scau.easyfarm.R;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.bean.SimpleBackPage;
import com.scau.easyfarm.util.UIHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by ChenHehong on 2016/6/11.
 */
public class MyFunctionFragment extends BaseFragment{

    @InjectView(R.id.ImgVillageServiceManage)
    ImageView imgVillageServiceManage;
    @InjectView(R.id.ImgVilageServicePermance)
    ImageView imgVilageServicePermance;
    @InjectView(R.id.ImgKnowledgeBase)
    ImageView imgKnowledgeBase;
    @InjectView(R.id.ImgSkillBase)
    ImageView imgSkillBase;
    @InjectView(R.id.ImgAchievementBase)
    ImageView imgAchievementBase;
    @InjectView(R.id.ImgVarietyBase)
    ImageView imgVarietyBase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_function,container, false);
        ButterKnife.inject(this, view);
        initView(view);
        return view;
    }

    @Override
    public void initView(View view) {
        imgVillageServiceManage.setOnClickListener(this);
        imgVilageServicePermance.setOnClickListener(this);
        imgKnowledgeBase.setOnClickListener(this);
        imgSkillBase.setOnClickListener(this);
        imgAchievementBase.setOnClickListener(this);
        imgVarietyBase.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.ImgVillageServiceManage:
                showVillageServiceManage();
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

    private void showVillageServiceManage(){
        UIHelper.showSimpleBack(getActivity(), SimpleBackPage.VILLAGE_FUNCTION);
    }

    private void showgVilageServicePermance(){

    }

    private void showKnowledgeBase(){
        UIHelper.showManualCategory(this,0);
    }
}
