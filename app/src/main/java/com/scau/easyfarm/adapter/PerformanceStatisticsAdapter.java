package com.scau.easyfarm.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scau.easyfarm.R;
import com.scau.easyfarm.base.ListBaseAdapter;
import com.scau.easyfarm.bean.Performance;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by chenhehong on 2016/8/31.
 * 知识库类别列表适配器
 */
public class PerformanceStatisticsAdapter extends ListBaseAdapter<Performance>{

    static class ViewHold{
        @InjectView(R.id.tv_performance_type)
        TextView type;
        @InjectView(R.id.tv_performance_applydate)
        TextView applyDate;
        @InjectView(R.id.tv_performance_applyman)
        TextView applyMan;

        public ViewHold(View view) {
            ButterKnife.inject(this, view);
        }
    }

    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHold vh = null;

        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(R.layout.list_cell_performance_statistics, null);
            vh = new ViewHold(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHold)convertView.getTag();
        }

        Performance performance = (Performance) mDatas.get(position);
        vh.type.setText(performance.getPerformanceTypeStr());
        vh.applyDate.setText("申报时间："+performance.getApplyDate());
        vh.applyMan.setText("申报人："+performance.getApplyManName());
        return convertView;
    }
}
