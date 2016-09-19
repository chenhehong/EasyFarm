package com.scau.easyfarm.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scau.easyfarm.R;
import com.scau.easyfarm.base.ListBaseAdapter;
import com.scau.easyfarm.bean.ManualContent;
import com.scau.easyfarm.bean.VillageService;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by chenhehong on 2016/8/31.
 * 知识库类别列表适配器
 */
public class ManualAdapter extends ListBaseAdapter<ManualContent>{

    static class ViewHold{
        @InjectView(R.id.tv_manual_title)
        TextView title;
        @InjectView(R.id.tv_manual_content)
        TextView content;

        public ViewHold(View view) {
            ButterKnife.inject(this, view);
        }
    }

    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHold vh = null;

        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(R.layout.list_cell_manual, null);
            vh = new ViewHold(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHold)convertView.getTag();
        }

        ManualContent manualContent = (ManualContent) mDatas.get(position);
        vh.title.setText(manualContent.getTitle());
        vh.content.setText(manualContent.getContent());
        return convertView;

    }
}
