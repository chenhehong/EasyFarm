package com.scau.easyfarm.adapter;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scau.easyfarm.R;
import com.scau.easyfarm.base.ListBaseAdapter;
import com.scau.easyfarm.bean.VillageService;
import com.scau.easyfarm.fragment.VillageServiceVerifyListFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by chenhehong on 2016/8/31.
 * 知识库类别列表适配器
 */
public class VillageServiceVerifyAdapter extends ListBaseAdapter<VillageService>{

    static class ViewHold{
        @InjectView(R.id.tv_villageservice_address)
        TextView address;
        @InjectView(R.id.tv_villageservice_apply_man)
        TextView applyMan;
        @InjectView(R.id.tv_villageservice_apply_time)
        TextView applyTime;
        @InjectView(R.id.tv_villageservice_status)
        TextView status;
        @InjectView(R.id.more)
        ImageView more;

        public ViewHold(View view) {
            ButterKnife.inject(this, view);
        }
    }

    VillageServiceVerifyListFragment fragment;

    public VillageServiceVerifyAdapter(VillageServiceVerifyListFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHold vh = null;

        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(R.layout.list_cell_villageservice_verify, null);
            vh = new ViewHold(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHold)convertView.getTag();
        }

        final VillageService villageService = (VillageService) mDatas.get(position);
        vh.address.setText(villageService.getBusinessArea()+villageService.getBusinessAddress());
        vh.applyTime.setText("申请时间："+villageService.getApplyDate());
        vh.applyMan.setText("申请人："+villageService.getApplyManName());
        vh.status.setText("申请状态：" + villageService.getStatusString());
        if (fragment.getmCatalog()==fragment.WAITING_VILAGE_SERVICE){
            vh.more.setVisibility(View.VISIBLE);
        }
        vh.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.handleLongClick(villageService);
            }
        });
        return convertView;
    }
}
