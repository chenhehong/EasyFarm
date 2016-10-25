package com.scau.easyfarm.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scau.easyfarm.R;
import com.scau.easyfarm.base.ListBaseAdapter;
import com.scau.easyfarm.bean.PerformanceStatistics;
import com.scau.easyfarm.bean.ServiceStatistics;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by chenhehong on 2016/8/31.
 * 知识库类别列表适配器
 */
public class ServiceMonthStatisticsAdapter extends ListBaseAdapter<ServiceStatistics>{

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
            convertView = getLayoutInflater(parent.getContext()).inflate(R.layout.list_cell_service_month_statistics, null);
            vh = new ViewHold(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHold)convertView.getTag();
        }

        ServiceStatistics serviceStatistics = (ServiceStatistics) mDatas.get(position);
        vh.month.setText("月份："+serviceStatistics.getMonth());
        vh.sumTime.setText("总服务次数："+serviceStatistics.getSumWorkTime());
        return convertView;
    }
}
