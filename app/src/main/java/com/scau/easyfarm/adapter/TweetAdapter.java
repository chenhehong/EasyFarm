package com.scau.easyfarm.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.R;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.ListBaseAdapter;
import com.scau.easyfarm.bean.Tweet;
import com.scau.easyfarm.util.DialogHelp;
import com.scau.easyfarm.util.StringUtils;
import com.scau.easyfarm.util.TypefaceUtils;
import com.scau.easyfarm.util.UIHelper;
import com.scau.easyfarm.widget.AvatarView;


import java.io.ByteArrayInputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cz.msebera.android.httpclient.Header;

public class TweetAdapter extends ListBaseAdapter<Tweet> {

    static class ViewHolder {
        @InjectView(R.id.tv_tweet_name)
        TextView author;
        @InjectView(R.id.tv_tweet_time)
        TextView time;
        @InjectView(R.id.tweet_item)
        TextView content;
        @InjectView(R.id.tv_tweet_comment_count)
        TextView commentcount;
        @InjectView(R.id.iv_tweet_face)
        AvatarView face;
        @InjectView(R.id.tv_like_state)
        TextView tvLikeState;
        @InjectView(R.id.tv_del)
        TextView del;
        @InjectView(R.id.tv_likeusers)
        TextView likeUsers;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    private Context context;


//  返回适配器的View
    @Override
    protected View getRealView(final int position, View convertView, ViewGroup parent) {
        context = parent.getContext();
        final ViewHolder vh;
        if (convertView == null || convertView.getTag() == null) {
            convertView = View.inflate(context, R.layout.list_cell_tweet, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final Tweet tweet = mDatas.get(position);
//      如果是自己发的，显示删除按钮
        if (tweet.getAuthorid() == AppContext.getInstance().getLoginUid()) {
            vh.del.setVisibility(View.VISIBLE);
            vh.del.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    optionDel(context, tweet, position);
                }
            });
        } else {
            vh.del.setVisibility(View.GONE);
        }

        vh.face.setUserInfo(tweet.getAuthorid(), tweet.getAuthor());
        vh.author.setText(tweet.getAuthor());
        vh.time.setText(StringUtils.friendly_time(tweet.getCreateDate()));
        vh.content.setText(tweet.getTitle());
        vh.content.setFocusable(false);
        vh.content.setLongClickable(false);

        vh.commentcount.setText(tweet.getCommentCount());

        tweet.setLikeUsers(context, vh.likeUsers, true);

        if (tweet.getLikeUser() == null) {
            vh.tvLikeState.setVisibility(View.GONE);
        } else {
            vh.tvLikeState.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (AppContext.getInstance().isLogin()) {
                        updateLikeState(vh, tweet);
                    } else {
                        AppContext.showToast("先登陆再赞~");
                        UIHelper.showLoginActivity(context);
                    }
                }
            });
        }
//      工具类获取图标文件，把textview设置为图标
        TypefaceUtils.setTypeface(vh.tvLikeState);
        if (tweet.getIsLike() == 1) {
            vh.tvLikeState.setTextColor(AppContext.getInstance().getResources().getColor(R.color
                    .day_colorPrimary));
        } else {
            vh.tvLikeState.setTextColor(AppContext.getInstance().getResources().getColor(R.color
                    .gray));
        }
        return convertView;
    }

    private void updateLikeState(ViewHolder vh, Tweet tweet) {

    }

    private void optionDel(Context context, final Tweet tweet, final int position) {

        DialogHelp.getConfirmDialog(context, "确定删除吗?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EasyFarmServerApi.deleteTweet(tweet.getAuthorid(), tweet.getId(),
                        new OperationResponseHandler() {
                            @Override
                            public void onSuccess(int code, ByteArrayInputStream is, Object[] args) {
                                mDatas.remove(position);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure(int code, String errorMessage, Object[] args) {
                            }
                        });
            }
        }).show();
    }

}
