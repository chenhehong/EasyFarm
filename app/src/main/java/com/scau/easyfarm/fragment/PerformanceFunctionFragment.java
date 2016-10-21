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
public class PerformanceFunctionFragment extends BaseFragment{

    @InjectView(R.id.ImgPerformanceApply)
    ImageView imgPerformanceApply;
    @InjectView(R.id.ImgPerformanceStatistic)
    ImageView imgPerformanceStatistic;
    @InjectView(R.id.ImgPerformanceVerify)
    ImageView imgPerformanceVerify;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_performance_function,container, false);
        ButterKnife.inject(this, view);
        initView(view);
        return view;
    }

    @Override
    public void initView(View view) {
        imgPerformanceApply.setOnClickListener(this);
        imgPerformanceStatistic.setOnClickListener(this);
        imgPerformanceVerify.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.ImgPerformanceApply:
                showPerformanceApply();
                break;
            case R.id.ImgPerformanceStatistic:
                showgPerformanceStatistic();
                break;
            case R.id.ImgPerformanceVerify:
                showPerformanceVerify();
                break;
            default:
                break;
        }
    }

    private void showPerformanceApply(){
        UIHelper.showSimpleBack(getActivity(), SimpleBackPage.VILLAGE_FUNCTION);
    }

    private void showgPerformanceStatistic(){
        
    }

    private void showPerformanceVerify(){
        UIHelper.showManualCategory(this, 0, ManualCategory.VARIETY);
    }

}
