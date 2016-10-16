package com.scau.easyfarm.adapter;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scau.easyfarm.AppConfig;
import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.R;
import com.scau.easyfarm.base.ListBaseAdapter;
import com.scau.easyfarm.bean.User;
import com.scau.easyfarm.bean.VillageService;
import com.scau.easyfarm.bean.VillageServiceReason;
import com.scau.easyfarm.fragment.VillageServiceAddFragment;
import com.scau.easyfarm.widget.ToggleButton;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by chenhehong on 2016/8/31.
 */
public class SelectedUserAdapter extends ListBaseAdapter<User>{

    Fragment fragment;

    static class ViewHold{
        @InjectView(R.id.tv_name)
        TextView name;
        @InjectView(R.id.im_del)
        ImageView delImg;
        @InjectView(R.id.tb_isleader)
        ToggleButton isleader;
        public ViewHold(View view) {
            ButterKnife.inject(this, view);
        }
    }

    public SelectedUserAdapter(Fragment fragment){
        super();
        this.fragment = fragment;
    }

    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHold vh = null;

        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(R.layout.list_cell_selected_user, null);
            vh = new ViewHold(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHold)convertView.getTag();
        }

        final User user = (User) mDatas.get(position);
        vh.name.setText(user.getRealName());
        vh.delImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(user);
                ((VillageServiceAddFragment) fragment).setListViewHeight();
            }
        });
        if (user.isServerLeader()){
            vh.isleader.setToggleOn();
        }
        vh.isleader.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                user.setIsServerLeader(on);
            }
        });
        return convertView;
    }
}
