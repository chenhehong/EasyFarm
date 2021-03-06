package com.scau.easyfarm.viewpagerfragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;

import com.scau.easyfarm.R;
import com.scau.easyfarm.adapter.ViewPageFragmentAdapter;
import com.scau.easyfarm.base.BaseListFragment;
import com.scau.easyfarm.base.BaseViewPagerFragment;
import com.scau.easyfarm.bean.Constants;
import com.scau.easyfarm.bean.Notice;
import com.scau.easyfarm.fragment.ActiveFragment;
import com.scau.easyfarm.ui.MainActivity;
import com.scau.easyfarm.widget.BadgeView;
import com.scau.easyfarm.widget.PagerSlidingTabStrip;

/**
 * 消息中心页面
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @author kymjs (https://github.com/kymjs)
 * @created 2014年9月25日 下午2:21:52
 * 
 */
public class NoticeViewPagerFragment extends BaseViewPagerFragment {

    public BadgeView mBvAtMe, mBvComment;
    public static int sCurrentPage = 0;
    private BroadcastReceiver mNoticeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setNoticeTip();
            changePagers();
        }
    };

    /**
     * 界面每次显示，去重置tip的显示
     */
    @Override
    public void onResume() {
        super.onResume();
        setNoticeTip();
        changePagers();
        mViewPager.setOffscreenPageLimit(2);
    }

    /**
     * 设置tip
     */
    private void setNoticeTip() {
        Notice notice = MainActivity.mNotice;
        if (notice != null) {
            changeTip(mBvAtMe, notice.getAtmeCount());// @我
            changeTip(mBvComment, notice.getReviewCount());// 评论
        } else {
            switch (mViewPager.getCurrentItem()) {
            case 0:
                changeTip(mBvAtMe, -1);
                break;
            case 1:
                changeTip(mBvComment, -1);
                break;
            }
        }
    }

    /**
     * 判断指定控件是否应该显示tip红点
     * 
     * @author kymjs
     */
    private void changeTip(BadgeView view, int count) {
        if (count > 0) {
            view.setText(count + "");
            view.show();
        } else {
            view.hide();
        }
    }

    /**
     * 当前tip是否在显示
     * 
     * @param which
     *            哪个界面的tip
     * @return
     */
    private boolean tipIsShow(int which) {
        switch (which) {
        case 0:
            return mBvAtMe.isShown();
        case 1:
            return mBvComment.isShown();
        default:
            return false;
        }
    }

    /**
     * 首次进入，切换到有tip的page
     */
    private void changePagers() {
        Notice notice = MainActivity.mNotice;
        if (notice == null) {
            return;
        }
        if (notice.getAtmeCount() != 0) {
            mViewPager.setCurrentItem(0);
            sCurrentPage = 0;
            refreshPage(0);
        } else if (notice.getReviewCount() != 0) {
            mViewPager.setCurrentItem(1);
            sCurrentPage = 1;
            refreshPage(1);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 注册接收者接受tip广播
        IntentFilter filter = new IntentFilter(Constants.INTENT_ACTION_NOTICE);
        getActivity().registerReceiver(mNoticeReceiver, filter);

        mBvAtMe = new BadgeView(getActivity(), mTabStrip.getBadgeView(0));
        mBvAtMe.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        mBvAtMe.setBadgePosition(BadgeView.POSITION_CENTER);
        mBvAtMe.setGravity(Gravity.CENTER);
        mBvAtMe.setBackgroundResource(R.drawable.notification_bg);

        mBvComment = new BadgeView(getActivity(), mTabStrip.getBadgeView(1));
        mBvComment.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        mBvComment.setBadgePosition(BadgeView.POSITION_CENTER);
        mBvComment.setGravity(Gravity.CENTER);
        mBvComment.setBackgroundResource(R.drawable.notification_bg);

        mTabStrip.getBadgeView(0).setVisibility(View.VISIBLE);
        mTabStrip.getBadgeView(1).setVisibility(View.VISIBLE);
        initData();
        initView(view);
    }

    @Override
    protected void setScreenPageLimit() {
        mViewPager.setOffscreenPageLimit(2);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mNoticeReceiver);
        mNoticeReceiver = null;
    }

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
        String[] title = getResources().getStringArray(
                R.array.mymes_viewpage_arrays);
        adapter.addTab(title[0], "active_me", ActiveFragment.class,
                getBundle(ActiveFragment.CATALOG_ATME));
        adapter.addTab(title[1], "active_comment", ActiveFragment.class,
                getBundle(ActiveFragment.CATALOG_COMMENT));
    }

    private Bundle getBundle(int catalog) {
        Bundle bundle = new Bundle();
        bundle.putInt(BaseListFragment.BUNDLE_KEY_CATALOG, catalog);
        return bundle;
    }

    @Override
    public void onClick(View v) {}

    @Override
    public void initView(View view) {
        changePagers();
        mViewPager.setOffscreenPageLimit(2);
        mTabStrip.setOnPagerChange(new PagerSlidingTabStrip.OnPagerChangeLis() {
            @Override
            public void onChanged(int page) {
                refreshPage(page);
                sCurrentPage = page;
            }
        });
    }

//  对有红点的标签栏目，自动刷新
    private void refreshPage(int index) {
        if (tipIsShow(index)) {
            try {
                ((BaseListFragment) getChildFragmentManager().getFragments()
                        .get(index)).onRefresh();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initData() {}
}
