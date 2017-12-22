package com.scau.easyfarm.adapter;

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
public class VillageServiceAdapter extends ListBaseAdapter<VillageService>{

    static class ViewHold{
        @InjectView(R.id.tv_villageservice_address)
        TextView address;
        @InjectView(R.id.tv_villageservice_apply_man)
        TextView applyMan;
        @InjectView(R.id.tv_villageservice_apply_time)
        TextView applyTime;
        @InjectView(R.id.tv_villageservice_status)
        TextView status;

        public ViewHold(View view) {
            ButterKnife.inject(this, view);
        }
    }

    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHold vh = null;

        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(R.layout.list_cell_villageservice, null);
            vh = new ViewHold(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHold)convertView.getTag();
        }

        VillageService villageService = (VillageService) mDatas.get(position);
        vh.address.setText(villageService.getBusinessArea()+villageService.getBusinessAddress());
        vh.applyTime.setText("申请时间："+villageService.getApplyDate());
        vh.applyMan.setText("申请人："+villageService.getApplyManName());
        vh.status.setText("申请状态："+villageService.getStatusString());
        return convertView;
    }
}
