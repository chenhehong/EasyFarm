package com.scau.easyfarm.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scau.easyfarm.R;
import com.scau.easyfarm.base.ListBaseAdapter;
import com.scau.easyfarm.bean.Area;
import com.scau.easyfarm.bean.User;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by chenhehong on 2016/8/31.
 */
public class AreaListAdapter extends ListBaseAdapter<Area>{

    static class ViewHold{
        @InjectView(R.id.tv_area_name)
        TextView name;
        @InjectView(R.id.iv_area_goto)
        ImageView goTo;
        public ViewHold(View view) {
            ButterKnife.inject(this, view);
        }
    }

    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHold vh = null;

        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(R.layout.list_cell_area, null);
            vh = new ViewHold(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHold)convertView.getTag();
        }

        Area area = (Area) mDatas.get(position);
        vh.name.setText(area.getName());

        // 如果不是父级类别，取消箭头显示
        if (!area.isParent()){
            vh.goTo.setVisibility(View.GONE);
        }
        return convertView;
    }
}
