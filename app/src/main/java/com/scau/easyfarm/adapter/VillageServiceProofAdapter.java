package com.scau.easyfarm.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scau.easyfarm.R;
import com.scau.easyfarm.base.ListBaseAdapter;
import com.scau.easyfarm.bean.VillageService;
import com.scau.easyfarm.fragment.ServerSummaryFragment;
import com.scau.easyfarm.util.UIHelper;

import org.w3c.dom.Text;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by chenhehong on 2016/8/31.
 * 知识库类别列表适配器
 */
public class VillageServiceProofAdapter extends ListBaseAdapter<VillageService>{

    private Fragment fragment;

    static class ViewHold{
        @InjectView(R.id.tv_villageservice_address)
        TextView address;
        @InjectView(R.id.tv_villageservice_business_date)
        TextView businessDate;
        @InjectView(R.id.tv_villageservice_reason)
        TextView reason;
        @InjectView(R.id.tv_finish)
        TextView finish;

        public ViewHold(View view) {
            ButterKnife.inject(this, view);
        }
    }

    public VillageServiceProofAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected View getRealView(final int position, View convertView, ViewGroup parent) {
        ViewHold vh = null;

        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(R.layout.list_cell_proof_villageservice, null);
            vh = new ViewHold(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHold)convertView.getTag();
        }

        final VillageService villageService = (VillageService) mDatas.get(position);
        vh.address.setText(villageService.getBusinessArea()+villageService.getBusinessAddress());
        vh.businessDate.setText("服务时间："+villageService.getBusinessDate()+"至"+villageService.getReturnDate());
        vh.reason.setText("事由："+villageService.getBusinessReason());
        if (villageService.getStatus()==VillageService.VILLAGE_SERVICE_PASS){
            vh.finish.setVisibility(View.VISIBLE);
            vh.finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIHelper.showServerSummary(fragment,villageService,position, ServerSummaryFragment.REQUESTCODE_SERVERSUMMARY);
                }
            });
        }
        return convertView;
    }
}
