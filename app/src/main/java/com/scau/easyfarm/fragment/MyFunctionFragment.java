package com.scau.easyfarm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.scau.easyfarm.R;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.bean.ManualCategory;
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
    @InjectView(R.id.ImgIndustryBase)
    ImageView imgIndustryBase;
    @InjectView(R.id.ImgSkillBase)
    ImageView imgSkillBase;
    @InjectView(R.id.ImgAchievementBase)
    ImageView imgAchievementBase;
    @InjectView(R.id.ImgVarietyBase)
    ImageView imgVarietyBase;
    @InjectView(R.id.ImgExpertBase)
    ImageView imgExpertBase;

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
        imgIndustryBase.setOnClickListener(this);
        imgSkillBase.setOnClickListener(this);
        imgAchievementBase.setOnClickListener(this);
        imgVarietyBase.setOnClickListener(this);
        imgExpertBase.setOnClickListener(this);
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
            case R.id.ImgIndustryBase:
                showIndustryBase();
                break;
            case R.id.ImgSkillBase:
                showSkillBase();
                break;
            case R.id.ImgAchievementBase:
                showAchievementBase();
                break;
            case R.id.ImgVarietyBase:
                showVarietyBase();
                break;
            case R.id.ImgExpertBase:
                showExpertBase();
                break;
            default:
                break;
        }
    }

    private void showVillageServiceManage(){
        UIHelper.showSimpleBack(getActivity(), SimpleBackPage.VILLAGE_FUNCTION);
    }

    private void showgVilageServicePermance(){
        UIHelper.showSimpleBack(getActivity(),SimpleBackPage.SERVICE_PERFORMANCE_FUNCTION);
    }

    private void showVarietyBase(){
        UIHelper.showManualCategory(this,0, ManualCategory.VARIETY);
    }

    private void showIndustryBase(){
        UIHelper.showManualCategory(this,0,ManualCategory.INDUSTRY);
    }

    private void showSkillBase(){
        UIHelper.showManualCategory(this,0,ManualCategory.TECHNOLOGY);
    }

    private void showAchievementBase(){
        UIHelper.showManualCategory(this,0,ManualCategory.ACHIEVEMENT);
    }

    private void showExpertBase(){
        UIHelper.showExpertBaseManualCategory(this,0,ManualCategory.EXPERT);
    }
}
