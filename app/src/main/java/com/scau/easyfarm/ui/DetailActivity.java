package com.scau.easyfarm.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;

import com.scau.easyfarm.R;
import com.scau.easyfarm.base.BaseActivity;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.emoji.KJEmojiFragment;
import com.scau.easyfarm.emoji.OnSendClickListener;
import com.scau.easyfarm.fragment.ManualContentDetailFragment;
import com.scau.easyfarm.fragment.NewsDetailFragment;
import com.scau.easyfarm.fragment.TweetDetailFragment;

/**
 * 详情activity（包括：问答、资讯）
 */
public class DetailActivity extends BaseActivity implements OnSendClickListener {

//    多个标志位来区分进行不同的Fragment操作
    public static final int DISPLAY_NEWS = 0;
    public static final int DISPLAY_TWEET = 1;
    public static final int DISPLAY_MAANUAL = 2;

    public static final String BUNDLE_KEY_DISPLAY_TYPE = "BUNDLE_KEY_DISPLAY_TYPE";

    private OnSendClickListener currentFragment;
//  使用了emoji带表情的编辑栏工具包
    public KJEmojiFragment emojiFragment = new KJEmojiFragment();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.actionbar_title_detail;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        int displayType = getIntent().getIntExtra(BUNDLE_KEY_DISPLAY_TYPE,
                DISPLAY_NEWS);
        BaseFragment fragment = null;
        int actionBarTitle = 0;
        switch (displayType) {
            case DISPLAY_TWEET:
                actionBarTitle = R.string.actionbar_tweet_detail;
                fragment = new TweetDetailFragment();
                break;
            case DISPLAY_NEWS:
                actionBarTitle = R.string.actionbar_title_news;
                fragment = new NewsDetailFragment();
                break;
            case DISPLAY_MAANUAL:
                actionBarTitle = R.string.manualdetail_actionbar_title;
                fragment = new ManualContentDetailFragment();
                break;
            default:
                break;
        }
//      设置actionbarTitle
        setActionBarTitle(actionBarTitle);
//      插入fragment
        FragmentTransaction trans = getSupportFragmentManager()
                .beginTransaction();
        trans.replace(R.id.container, fragment);
        trans.commitAllowingStateLoss();
//      因为内容fragment要监听下方编辑工具栏的发送状态，所以必须实现OnSendClickListener接口
        if (fragment instanceof OnSendClickListener) {
            currentFragment = (OnSendClickListener) fragment;
        } else {
            currentFragment = new OnSendClickListener() {
                @Override
                public void onClickSendButton(Editable str) {
                }

                @Override
                public void onClickFlagButton() {
                }
            };
        }
    }

    @Override
    public void onClick(View v) {
    }

    @Override
//  设置下方编辑工具栏
    public void initView() {
        if (currentFragment instanceof TweetDetailFragment){
            getSupportFragmentManager().beginTransaction().replace(R.id.emoji_keyboard, emojiFragment).commit();
        }
    }

    @Override
    public void initData() {
    }

    @Override
    public void onClickSendButton(Editable str) {
        currentFragment.onClickSendButton(str);
    }

    @Override
//  设置返回键对应文字输入栏的控制
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            try {
                if (emojiFragment.isShowEmojiKeyBoard()) {
                    emojiFragment.hideAllKeyBoard();
                    return true;
                }
                if (emojiFragment.getEditText().getTag() != null) {
                    emojiFragment.getEditText().setTag(null);
                    emojiFragment.getEditText().setHint("说点什么吧");
                    return true;
                }
            } catch (NullPointerException e) {
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClickFlagButton() {

    }
}
