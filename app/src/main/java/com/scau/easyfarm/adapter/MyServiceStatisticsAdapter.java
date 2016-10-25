package com.scau.easyfarm.adapter;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scau.easyfarm.R;
import com.scau.easyfarm.base.ListBaseAdapter;
import com.scau.easyfarm.bean.VillageService;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by chenhehong on 2016/8/31.
 * 知识库类别列表适配器
 */
public class MyServiceStatisticsAdapter extends ListBaseAdapter<VillageService>{

    private Fragment fragment;

    static class ViewHold{
        @InjectView(R.id.tv_villageservice_address)
        TextView address;
        @InjectView(R.id.tv_villageservice_business_date)
        TextView businessDate;
        @InjectView(R.id.tv_villageservice_reason)
        TextView reason;

        public ViewHold(View view) {
            ButterKnife.inject(this, view);
        }
    }

    public MyServiceStatisticsAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected View getRealView(final int position, View convertView, ViewGroup parent) {
        ViewHold vh = null;

        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(R.layout.list_cell_myservice_statistics, null);
            vh = new ViewHold(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHold)convertView.getTag();
        }

        final VillageService villageService = (VillageService) mDatas.get(position);
        vh.address.setText(villageService.getBusinessArea()+villageService.getBusinessAddress());
        vh.businessDate.setText("服务时间："+villageService.getBusinessDate()+"至"+villageService.getReturnDate());
        vh.reason.setText("事由："+villageService.getBusinessReason());
        return convertView;
    }
}
