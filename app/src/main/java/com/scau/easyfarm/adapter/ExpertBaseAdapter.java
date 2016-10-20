package com.scau.easyfarm.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scau.easyfarm.R;
import com.scau.easyfarm.base.ListBaseAdapter;
import com.scau.easyfarm.bean.ExpertBase;
import com.scau.easyfarm.util.StringUtils;
import com.scau.easyfarm.widget.AvatarView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 好友列表适配器
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年11月6日 上午11:22:27
 * 
 */
public class ExpertBaseAdapter extends ListBaseAdapter<ExpertBase> {

    @SuppressLint("InflateParams")
    @Override
    protected View getRealView(int position, View convertView,
	    final ViewGroup parent) {
	ViewHolder vh = null;
	if (convertView == null || convertView.getTag() == null) {
	    convertView = getLayoutInflater(parent.getContext()).inflate(
		    R.layout.list_cell_expertbase, null);
	    vh = new ViewHolder(convertView);
	    convertView.setTag(vh);
	} else {
	    vh = (ViewHolder) convertView.getTag();
	}

		final ExpertBase item = (ExpertBase) mDatas.get(position);

		vh.realName.setText(item.getRealName());

		vh.organization.setText(item.getOrganization());
		vh.techtype.setText(item.getTechType());
		vh.phoneNumber.setText(item.getPhoneNumber());

//	vh.avatar.setAvatarUrl(item.getPortrait());
//	vh.avatar.setExpertBaseInfo(item.getId(), item.getName());

		return convertView;
    }

    static class ViewHolder {

	@InjectView(R.id.tv_realname)
	TextView realName;
	@InjectView(R.id.tv_organization)
	TextView organization;
	@InjectView(R.id.tv_techtype)
	TextView techtype;
	@InjectView(R.id.tv_phonenumber)
	TextView phoneNumber;
	@InjectView(R.id.iv_avatar)
	AvatarView avatar;

	public ViewHolder(View view) {
	    ButterKnife.inject(this, view);
	}
    }
}
