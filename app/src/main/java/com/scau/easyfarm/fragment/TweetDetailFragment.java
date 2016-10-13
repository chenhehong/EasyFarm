package com.scau.easyfarm.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.R;
import com.scau.easyfarm.adapter.CommentAdapter;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BeseHaveHeaderListFragment;
import com.scau.easyfarm.base.ListBaseAdapter;
import com.scau.easyfarm.bean.Comment;
import com.scau.easyfarm.bean.CommentList;
import com.scau.easyfarm.bean.Result;
import com.scau.easyfarm.bean.ResultBean;
import com.scau.easyfarm.bean.Tweet;
import com.scau.easyfarm.bean.TweetDetail;
import com.scau.easyfarm.cache.CacheManager;
import com.scau.easyfarm.emoji.OnSendClickListener;
import com.scau.easyfarm.ui.DetailActivity;
import com.scau.easyfarm.ui.empty.EmptyLayout;
import com.scau.easyfarm.util.DialogHelp;
import com.scau.easyfarm.util.HTMLUtil;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.StringUtils;
import com.scau.easyfarm.util.TDevice;
import com.scau.easyfarm.util.TypefaceUtils;
import com.scau.easyfarm.util.UIHelper;
import com.scau.easyfarm.widget.AvatarView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/***
 * 问答详情，上面显示详情，下方实际每个item显示的数据类型是Comment
 * 
 * TweetDetailFragment.java
 */
public class TweetDetailFragment extends
        BeseHaveHeaderListFragment<Comment, TweetDetail> implements
        OnItemClickListener, OnItemLongClickListener, OnSendClickListener {

    private static final String CACHE_KEY_PREFIX = "tweet_";
    private static final String CACHE_KEY_TWEET_COMMENT = "tweet_comment_";
    private AvatarView mIvAvatar;
    private TextView mTvName, mTvFrom, mTvTime, mTvCommentCount;
    private TextView mContent;
    private int mTweetId;
    private Tweet mTweet;

    private TextView mTvLikeState;

    private DetailActivity outAty;

    @Override
    protected CommentAdapter getListAdapter() {
        return new CommentAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        outAty = (DetailActivity) getActivity();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_TWEET_COMMENT + mTweetId + "_" + mCurrentPage;
    }

    @Override
    protected CommentList parseList(InputStream is) throws Exception {
        CommentList list = JsonUtils.toBean(CommentList.class, is);
        return list;
    }

    @Override
    protected CommentList readList(Serializable seri) {
        return ((CommentList) seri);
    }

    @Override
    protected void sendRequestData() {
        EasyFarmServerApi.getCommentList(mTweetId, CommentList.CATALOG_TWEET,
                mCurrentPage, mHandler);
//      start--模拟数据
//        List<Comment> data = new ArrayList<Comment>();
//        Comment c1 = new Comment();
//        c1.setComenterName("许渭根[专家]");
//        c1.setCommentDate("2016-07-05 11:47:42");
//        c1.setContent("一是你种在什么地方？那里有什么杂草？是已经种下去了，还是打算种的空地？都没有说清楚，希望你到当地农业技术部门咨询，比较可靠。谢谢");
//        data.add(c1);
//        Comment c2 = new Comment();
//        c2.setComenterName("张富仙");
//        c2.setCommentDate("2016-07-05 11:47:42");
//        c2.setContent("如以禾本科杂草为主可用选择性除草剂精喹禾灵,这对苦瓜相对安全；如阔叶杂草较多得用草甘磷，但必须禁止喷到瓜苗，苦瓜是立架栽培，可采用纸板等隔离物边遮档瓜苗边压低喷头喷施。");
//        data.add(c2);
//        Comment c3 = new Comment();
//        c3.setComenterName("怀燕");
//        c3.setCommentDate("2016-07-09 11:47:42");
//        c3.setContent("合理密植。保持通风透光，加强中耕除草、培土，尤其是拔节后培土，遇到不良气候而影响正常授粉时，采用人工辅助授粉技术。");
//        data.add(c3);
//        executeOnLoadDataSuccess(data);
//      end--模拟数据
    }

    @Override
    protected boolean requestDataIfViewCreated() {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        outAty.emojiFragment.hideFlagButton();
    }

//  填充问答详情
    private void fillUI() {
        mIvAvatar.setUserInfo(mTweet.getAuthorid(), mTweet.getAuthor());
        mTvName.setText(mTweet.getAuthor());
        mTvTime.setText(StringUtils.friendly_time(mTweet.getCreateDate()));

        mTvCommentCount.setText(mTweet.getCommentCount() + "");
        mContent.setText(mTweet.getContent());
        setLikeState();
    }

    private void setLikeState() {
        if (mTweet != null) {
            if (mTweet.getIsLike() == 1) {
                mTvLikeState.setTextColor(AppContext.getInstance().getResources().getColor(R.color.day_colorPrimary));
            } else {
                mTvLikeState.setTextColor(AppContext.getInstance().getResources().getColor(R.color.gray));
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        if (position<1) return;
        final Comment comment = mAdapter.getItem(position - 1);
        if (comment == null)
            return;
        outAty.emojiFragment.getEditText().setHint("回复:" + comment.getComenterName());
        outAty.emojiFragment.getEditText().setTag(comment);
        outAty.emojiFragment.showSoftKeyboard();
    }

//  提交评论后的句柄
    private final OperationResponseHandler mCommentHandler = new OperationResponseHandler() {

        @Override
        public void onSuccess(int code, ByteArrayInputStream is, Object[] args) {
            try {
                ResultBean rsb = JsonUtils.toBean(ResultBean.class,is);
                Result res = rsb.getResult();
                if (res.OK()) {
                    hideWaitDialog();
                    AppContext.showToastShort(R.string.comment_publish_success);
                    mAdapter.setState(ListBaseAdapter.STATE_NO_MORE);
                    mAdapter.addItem(0, rsb.getComment());
                    setTweetCommentCount();
                } else {
                    hideWaitDialog();
                    AppContext.showToastShort(res.getErrorMessage());
                }
                outAty.emojiFragment.clean();
            } catch (Exception e) {
                e.printStackTrace();
                onFailure(code, e.getMessage(),args);
            }
        }

        @Override
        public void onFailure(int code, String errorMessage, Object[] args) {
            hideWaitDialog();
            Log.d("评论失败",errorMessage);
            // AppContext.showToastShort(R.string.comment_publish_faile);
            AppContext.showToast(errorMessage + code);
        }
    };

    class DeleteOperationResponseHandler extends OperationResponseHandler {

        DeleteOperationResponseHandler(Object... args) {
            super(args);
        }

        @Override
        public void onSuccess(int code, ByteArrayInputStream is, Object[] args) {
            try {
                Result res = JsonUtils.toBean(ResultBean.class, is).getResult();
                if (res.OK()) {
                    AppContext.showToastShort(R.string.delete_success);
                    mAdapter.removeItem(args[0]);
                    setTweetCommentCount();
                } else {
                    AppContext.showToastShort(res.getErrorMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
                onFailure(code, e.getMessage(), args);
            }
        }

        @Override
        public void onFailure(int code, String errorMessage, Object[] args) {
            AppContext.showToastShort(R.string.delete_faile);
        }
    }

    private void handleDeleteComment(Comment comment) {
        if (!AppContext.getInstance().isLogin()) {
            UIHelper.showLoginActivity(getActivity());
            return;
        }
        AppContext.showToastShort(R.string.deleting);
        EasyFarmServerApi.deleteComment(mTweetId, CommentList.CATALOG_TWEET,
                comment.getId(), comment.getCommenterId(),
                new DeleteOperationResponseHandler(comment));
    }

    private void setTweetCommentCount() {
        mAdapter.notifyDataSetChanged();
        if (mTweet != null) {
            mTweet.setCommentCount(mAdapter.getDataSize() + "");
            mTvCommentCount.setText(mTweet.getCommentCount() + "");
        }
    }

    @Override
//  长按事件处理
    public boolean onItemLongClick(AdapterView<?> parent, View view,
            int position, long id) {
        if (position - 1 == -1) {
            return false;
        }
        final Comment item = mAdapter.getItem(position - 1);
        if (item == null)
            return false;
        int itemsLen = item.getCommenterId() == AppContext.getInstance()
                .getLoginUid() ? 2 : 1;
        String[] items = new String[itemsLen];
        items[0] = getResources().getString(R.string.copy);
        if (itemsLen == 2) {
            items[1] = getResources().getString(R.string.delete);
        }
        DialogHelp.getSelectDialog(getActivity(), items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    TDevice.copyTextToBoard(HTMLUtil.delHTMLTag(item
                            .getContent()));
                } else if (i == 1) {
                    handleDeleteComment(item);
                }
            }
        }).show();
        return true;
    }

    @Override
    protected void requestDetailData(boolean isRefresh) {
        String key = getDetailCacheKey();
        mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        if (TDevice.hasInternet()
                && (!CacheManager.isExistDataCache(getActivity(), key) || isRefresh)) {
            EasyFarmServerApi.getTweetDetail(mTweetId, mDetailHandler);
        } else {
            readDetailCacheData(key);
        }
    }

    @Override
    protected boolean isRefresh() {
        return true;
    }

    @Override
    protected View initHeaderView() {
        Intent args = getActivity().getIntent();
        mTweetId = args.getIntExtra("tweet_id", 0);
        mTweet = (Tweet) args.getParcelableExtra("tweet");

        mListView.setOnItemLongClickListener(this);
        View header = LayoutInflater.from(getActivity()).inflate(
                R.layout.list_header_tweet_detail, null);
        mIvAvatar = (AvatarView) header.findViewById(R.id.iv_avatar);

        mTvName = (TextView) header.findViewById(R.id.tv_name);
        mTvFrom = (TextView) header.findViewById(R.id.tv_from);
        mTvTime = (TextView) header.findViewById(R.id.tv_time);
        mTvCommentCount = (TextView) header.findViewById(R.id.tv_comment_count);
        mContent = (TextView) header.findViewById(R.id.contentview);
        mTvLikeState = (TextView) header.findViewById(R.id.tv_like_state);
        mTvLikeState.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                likeOption();
            }
        });
        TypefaceUtils.setTypeface(mTvLikeState);
        return header;
    }

    private void likeOption() {
        if (mTweet == null)
            return;
        OperationResponseHandler handler = new OperationResponseHandler() {

            @Override
            public void onSuccess(int code, ByteArrayInputStream is, Object[] args) {}

            @Override
            public void onFailure(int code, String errorMessage, Object[] args) {}
        };
        if (AppContext.getInstance().isLogin()) {

        } else {
            AppContext.showToast("先登陆再点赞~");
            UIHelper.showLoginActivity(getActivity());
        }
    }

    @Override
    protected String getDetailCacheKey() {
        return CACHE_KEY_PREFIX + mTweetId;
    }

    @Override
    protected void executeOnLoadDetailSuccess(TweetDetail detailBean) {
        mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        this.mTweet = detailBean.getTweet();
        fillUI();
        mAdapter.setNoDataText(R.string.comment_empty);
    }

    @Override
    protected TweetDetail getDetailBean(ByteArrayInputStream is) {
        TweetDetail tweetDetail = JsonUtils.toBean(TweetDetail.class, is);
        return  tweetDetail;
    }

    @Override
    protected void executeOnLoadDataSuccess(List<Comment> data) {
        super.executeOnLoadDataSuccess(data);
        int commentCount = StringUtils.toInt(mTweet == null ? 0 : this.mTweet
                .getCommentCount());
        if (commentCount < (mAdapter.getCount() - 1)) {
            commentCount = mAdapter.getCount() - 1;
        }
        mTvCommentCount.setText(commentCount + "");
    }

    @Override
    public void onClickSendButton(Editable str) {
        if (!AppContext.getInstance().isLogin()) {
            UIHelper.showLoginActivity(getActivity());
            return;
        }
        if (!TDevice.hasInternet()) {
            AppContext.showToastShort(R.string.tip_network_error);
            return;
        }
        if (TextUtils.isEmpty(str)) {
            AppContext.showToastShort(R.string.tip_comment_content_empty);
            return;
        }
        showWaitDialog(R.string.progress_submit);
        try {
//          回复其他人
            if (outAty.emojiFragment.getEditText().getTag() != null) {
                Comment comment = (Comment) outAty.emojiFragment.getEditText()
                        .getTag();
                EasyFarmServerApi.replyComment(mTweetId, CommentList.CATALOG_TWEET,
                        comment.getId(), comment.getCommenterId(), AppContext
                                .getInstance().getLoginUid(), str.toString(),
                        mCommentHandler);
            } else {
//              评论问题
                EasyFarmServerApi.publicComment(CommentList.CATALOG_TWEET, mTweetId,
                        AppContext.getInstance().getLoginUid(), str.toString(),
                         mCommentHandler);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onClickFlagButton() {}

    @Override
    public boolean onBackPressed() {
        if (outAty.emojiFragment.isShowEmojiKeyBoard()) {
            outAty.emojiFragment.hideAllKeyBoard();
            return true;
        }
        if (outAty.emojiFragment.getEditText().getTag() != null) {
            outAty.emojiFragment.getEditText().setTag(null);
            outAty.emojiFragment.getEditText().setHint("说点什么吧");
            return true;
        }
        return super.onBackPressed();
    }
}
