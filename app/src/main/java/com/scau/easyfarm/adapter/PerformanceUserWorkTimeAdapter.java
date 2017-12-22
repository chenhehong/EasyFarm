package com.scau.easyfarm.adapter;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.scau.easyfarm.R;
import com.scau.easyfarm.base.ListBaseAdapter;
import com.scau.easyfarm.bean.PerformanceMemberWorkTime;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by chenhehong on 2016/8/31.
 */
public class PerformanceUserWorkTimeAdapter extends ListBaseAdapter<PerformanceMemberWorkTime>{

    Fragment fragment;

    static class ViewHold{
        @InjectView(R.id.tv_name)
        TextView name;
        @InjectView(R.id.et_worktime)
        EditText workTime;
        public ViewHold(View view) {
            ButterKnife.inject(this, view);
        }
    }

    public PerformanceUserWorkTimeAdapter(Fragment fragment){
        super();
        this.fragment = fragment;
    }

    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHold vh = null;

        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(R.layout.list_cell_user_worktime, null);
            vh = new ViewHold(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHold)convertView.getTag();
        }

        final PerformanceMemberWorkTime memberWorkTime = (PerformanceMemberWorkTime) mDatas.get(position);
        vh.name.setText(memberWorkTime.getUserName());
        vh.workTime.setText(memberWorkTime.getRealWorkTime()+"");

        return convertView;
    }
}
