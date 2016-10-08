package com.scau.easyfarm.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scau.easyfarm.R;
import com.scau.easyfarm.api.ApiHttpClient;
import com.scau.easyfarm.base.ListBaseAdapter;
import com.scau.easyfarm.bean.VillageProofResource;
import com.scau.easyfarm.bean.VillageService;

import org.kymjs.kjframe.Core;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by chenhehong on 2016/8/31.
 * 知识库类别列表适配器
 */
public class VillageServiceProofResourceAdapter extends ListBaseAdapter<VillageProofResource>{

    static class ViewHold{
        @InjectView(R.id.iv_resource_img)
        ImageView img;
        @InjectView(R.id.tv_resource_description)
        TextView description;
        @InjectView(R.id.tv_resource_address)
        TextView address;
        @InjectView(R.id.tv_resource_time)
        TextView time;

        public ViewHold(View view) {
            ButterKnife.inject(this, view);
        }
    }

    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHold vh = null;

        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(R.layout.list_cell_villageservice_proofresource, null);
            vh = new ViewHold(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHold)convertView.getTag();
        }

        VillageProofResource villageServiceProofResource = (VillageProofResource) mDatas.get(position);
        new Core.Builder().view(vh.img).url(ApiHttpClient.getAbsoluteApiUrl(villageServiceProofResource.getImageFilePath())).doTask();
        vh.description.setText(villageServiceProofResource.getDescription());
        vh.time.setText(villageServiceProofResource.getCreateDate());
        vh.address.setText(villageServiceProofResource.getAddress());
        return convertView;
    }
}
