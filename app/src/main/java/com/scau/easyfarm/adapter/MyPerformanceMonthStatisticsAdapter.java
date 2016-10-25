package com.scau.easyfarm.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scau.easyfarm.R;
import com.scau.easyfarm.base.ListBaseAdapter;
import com.scau.easyfarm.bean.PerformanceStatistics;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by chenhehong on 2016/8/31.
 * 知识库类别列表适配器
 */
public class MyPerformanceMonthStatisticsAdapter extends ListBaseAdapter<PerformanceStatistics>{

    static class ViewHold{
        @InjectView(R.id.tv_month)
        TextView month;
        @InjectView(R.id.tv_sumtime)
        TextView sumTime;

        public ViewHold(View view) {
            ButterKnife.inject(this, view);
        }
    }

    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHold vh = null;

        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(R.layout.list_cell_myperformance_month_statistics, null);
            vh = new ViewHold(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHold)convertView.getTag();
        }

        PerformanceStatistics performanceStatistics = (PerformanceStatistics) mDatas.get(position);
        vh.month.setText("月份："+performanceStatistics.getMonth());
        vh.sumTime.setText("总工时："+performanceStatistics.getSumWorkTime());
        return convertView;
    }
}
