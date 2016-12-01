package com.scau.easyfarm.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scau.easyfarm.R;
import com.scau.easyfarm.base.ListBaseAdapter;
import com.scau.easyfarm.bean.User;
import com.scau.easyfarm.bean.WitTweet;
import com.scau.easyfarm.util.StringUtils;
import com.scau.easyfarm.widget.AvatarView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 智能问答列表适配器
 */
public class WitTweetAdapter extends ListBaseAdapter<WitTweet> {

    @SuppressLint("InflateParams")
    @Override
    protected View getRealView(int position, View convertView,
	    final ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null || convertView.getTag() == null) {
		    convertView = getLayoutInflater(parent.getContext()).inflate(
			    R.layout.list_cell_wittweet, null);
		    vh = new ViewHolder(convertView);
		    convertView.setTag(vh);
		} else {
		    vh = (ViewHolder) convertView.getTag();
		}

		final WitTweet item = (WitTweet) mDatas.get(position);

		vh.title.setText(item.getTitle());
		vh.type.setText("【"+WitTweet.typeIntHashMap.get(item.getType())+"】");
		if (item.getType()==WitTweet.TYPE_TWEET){
			vh.commentcount.setVisibility(View.VISIBLE);
			vh.commentcount.setText(item.getCommentCount()+"条评论");
		}
		return convertView;
    }

    static class ViewHolder {

		@InjectView(R.id.tv_type)
		TextView type;
		@InjectView(R.id.tv_title)
		TextView title;
		@InjectView(R.id.tv_commentcount)
		TextView commentcount;

		public ViewHolder(View view) {
	    	ButterKnife.inject(this, view);
		}
    }
}
