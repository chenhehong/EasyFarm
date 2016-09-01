package com.scau.easyfarm.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scau.easyfarm.R;
import com.scau.easyfarm.base.ListBaseAdapter;
import com.scau.easyfarm.bean.ManualCategory;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by chenhehong on 2016/8/31.
 * 知识库类别列表适配器
 */
public class ManualCategoryListAdapter extends ListBaseAdapter<ManualCategory>{

    static class ViewHold{
        @InjectView(R.id.tv_manual_cotegory_name)
        TextView name;
        @InjectView(R.id.iv_manual_cotegory_goto)
        ImageView goTo;

        public ViewHold(View view) {
            ButterKnife.inject(this, view);
        }
    }

    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHold vh = null;

        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(R.layout.list_cell_manualcotegory, null);
            vh = new ViewHold(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHold)convertView.getTag();
        }

        ManualCategory manualCategory = (ManualCategory) mDatas.get(position);
        vh.name.setText(manualCategory.getCategoryName());
//      如果不是父级类别，取消箭头显示
        if (!manualCategory.isParent()){
            vh.goTo.setVisibility(View.GONE);
        }
        return convertView;

    }
}
