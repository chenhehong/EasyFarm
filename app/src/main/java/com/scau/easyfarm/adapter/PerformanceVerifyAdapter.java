package com.scau.easyfarm.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scau.easyfarm.R;
import com.scau.easyfarm.base.ListBaseAdapter;
import com.scau.easyfarm.bean.Performance;
import com.scau.easyfarm.fragment.PerformanceVerifyListFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by chenhehong on 2016/8/31.
 * 知识库类别列表适配器
 */
public class PerformanceVerifyAdapter extends ListBaseAdapter<Performance>{

    static class ViewHold{
        @InjectView(R.id.tv_performance_type)
        TextView type;
        @InjectView(R.id.tv_performance_applydate)
        TextView applyDate;
        @InjectView(R.id.tv_performance_applyman)
        TextView applyMan;
        @InjectView(R.id.tv_performance_status)
        TextView status;
        @InjectView(R.id.more)
        ImageView more;

        public ViewHold(View view) {
            ButterKnife.inject(this, view);
        }
    }

    PerformanceVerifyListFragment fragment;

    public PerformanceVerifyAdapter(PerformanceVerifyListFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHold vh = null;

        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(R.layout.list_cell_performance_verify, null);
            vh = new ViewHold(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHold)convertView.getTag();
        }

        final Performance performance = (Performance) mDatas.get(position);
        vh.type.setText(performance.getPerformanceTypeStr());
        vh.applyDate.setText("申报时间："+performance.getApplyDate());
        vh.applyMan.setText("申报人："+performance.getApplyManName());
        vh.status.setText("审核状态："+performance.getStatusString());
        if (fragment.getmCatalog()==fragment.WAITING_PERFORMANCE){
            vh.more.setVisibility(View.VISIBLE);
        }
        vh.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.handleLongClick(performance);
            }
        });
        return convertView;
    }
}
