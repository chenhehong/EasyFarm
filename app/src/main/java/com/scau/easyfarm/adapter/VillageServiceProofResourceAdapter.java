package com.scau.easyfarm.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scau.easyfarm.R;
import com.scau.easyfarm.api.ApiHttpClient;
import com.scau.easyfarm.base.ListBaseAdapter;
import com.scau.easyfarm.bean.VillageProofResource;
import com.scau.easyfarm.ui.ImageGalleryActivity;
import com.scau.easyfarm.ui.VideoPlayActivity;

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
    protected View getRealView(final int position, View convertView, final ViewGroup parent) {
        ViewHold vh = null;

        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(R.layout.list_cell_villageservice_proofresource, null);
            vh = new ViewHold(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHold)convertView.getTag();
        }

        final VillageProofResource villageServiceProofResource = (VillageProofResource) mDatas.get(position);
        final String uploadFilePath = villageServiceProofResource.getUploadFilePath();
        String suffix = uploadFilePath.substring(uploadFilePath.lastIndexOf(".") + 1);
        if(suffix.equalsIgnoreCase("jpg")||suffix.equalsIgnoreCase("jpeg")||suffix.equalsIgnoreCase("png")){
            new Core.Builder().view(vh.img).url(ApiHttpClient.getAbsoluteApiUrl(uploadFilePath)).doTask();
            vh.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageGalleryActivity.show(parent.getContext(), ApiHttpClient.getAbsoluteApiUrl(uploadFilePath),"");
                }
            });
        }else if(suffix.equalsIgnoreCase("mp4")||suffix.equalsIgnoreCase("3gp")){
            vh.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(parent.getContext(), VideoPlayActivity.class);
                    intent.putExtra(VideoPlayActivity.BUNDLEKEY_VIDEOSOURCE_TYPE, VideoPlayActivity.VIDEOTYPEURL);
                    intent.putExtra(VideoPlayActivity.BUNDLEKEY_VIDEOSOURCE,ApiHttpClient.getAbsoluteApiUrl(uploadFilePath));
                    parent.getContext().startActivity(intent);
                }
            });
        }

        vh.description.setText(villageServiceProofResource.getDescription());
        vh.time.setText(villageServiceProofResource.getCreateDate());
        vh.address.setText(villageServiceProofResource.getAddress());
        return convertView;
    }
}
