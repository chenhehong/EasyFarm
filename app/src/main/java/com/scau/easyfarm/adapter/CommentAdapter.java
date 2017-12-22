package com.scau.easyfarm.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scau.easyfarm.R;
import com.scau.easyfarm.api.ApiHttpClient;
import com.scau.easyfarm.base.ListBaseAdapter;
import com.scau.easyfarm.bean.Comment;
import com.scau.easyfarm.util.StringUtils;
import com.scau.easyfarm.widget.AvatarView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CommentAdapter extends ListBaseAdapter<Comment> {

    @SuppressLint({ "InflateParams", "CutPasteId" })
    @Override
    protected View getRealView(int position, View convertView,
            final ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.list_cell_comment, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        try {

            final Comment item = mDatas.get(position);

            // 若Authorid为0，则显示非会员
            vh.name.setText(item.getComenterName()
                    + (item.getCommenterId() == 0 ? "(非会员)" : ""));
            String contents = "";
            if (item.getCommentedName()!=null&&item.getCommentedName().length()>0){
                contents += "回复@"+item.getCommentedName()+":";
            }
            contents+=item.getContent();
            vh.content.setText(contents);
            vh.avatar.setAvatarUrl(ApiHttpClient.getAbsoluteApiUrl(item.getPortrait()));
            vh.time.setText(StringUtils.friendly_time(item.getCommentDate()));

        } catch (Exception e) {

        }
        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.iv_avatar)
        AvatarView avatar;
        @InjectView(R.id.tv_name)
        TextView name;
        @InjectView(R.id.tv_time)
        TextView time;
        @InjectView(R.id.tv_content)
        TextView content;
        @InjectView(R.id.ly_relies)
        LinearLayout relies;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
