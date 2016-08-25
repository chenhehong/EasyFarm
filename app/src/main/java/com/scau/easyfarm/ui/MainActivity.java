package com.scau.easyfarm.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.scau.easyfarm.AppConfig;
import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.AppManager;
import com.scau.easyfarm.R;
import com.scau.easyfarm.bean.Constants;
import com.scau.easyfarm.bean.Notice;
import com.scau.easyfarm.bean.SimpleBackPage;
import com.scau.easyfarm.fragment.MyInformationFragment;
import com.scau.easyfarm.interf.BaseViewInterface;
import com.scau.easyfarm.service.NoticeUtils;
import com.scau.easyfarm.util.UIHelper;
import com.scau.easyfarm.util.UpdateManager;
import com.scau.easyfarm.widget.BadgeView;
import com.scau.easyfarm.widget.MyFragmentTabHost;

import butterknife.ButterKnife;
import butterknife.InjectView;

@SuppressLint("InflateParams")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends ActionBarActivity implements
        TabHost.OnTabChangeListener, BaseViewInterface, View.OnClickListener,
        View.OnTouchListener {

    private DoubleClickExitHelper mDoubleClickExit;
    public static Notice mNotice;

    @InjectView(android.R.id.tabhost)
    public MyFragmentTabHost mTabHost;

    private BadgeView mBvNotice;

    //  菜单栏中间的快速按钮
    @InjectView(R.id.quick_option_iv)
    View mAddBt;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//          通过控制BadgeView 控件发生改变来控制是否显示消息提示的小红点
            if (intent.getAction().equals(Constants.INTENT_ACTION_NOTICE)) {
                mNotice = (Notice) intent.getSerializableExtra("notice_bean");
                int atmeCount = mNotice.getAtmeCount();// @我
                int msgCount = mNotice.getMsgCount();// 留言
                int reviewCount = mNotice.getReviewCount();// 评论
                int activeCount = atmeCount + reviewCount + msgCount;

                Fragment fragment = getCurrentFragment();
                if (fragment instanceof MyInformationFragment) {
                    ((MyInformationFragment) fragment).setNotice();
                } else {
                    if (activeCount > 0) {
                        mBvNotice.setText(activeCount + "");
                        mBvNotice.show();
                    } else {
                        mBvNotice.hide();
                        mNotice = null;
                    }
                }
            } else if (intent.getAction()
                    .equals(Constants.INTENT_ACTION_LOGOUT)) {
                mBvNotice.hide();
                mNotice = null;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initView();
//      将当前Activity压入栈中
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    public void initView() {
//      双击退出
        mDoubleClickExit = new DoubleClickExitHelper(this);

        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        if (android.os.Build.VERSION.SDK_INT > 10) {
            mTabHost.getTabWidget().setShowDividers(0);
        }

        initTabs();

        // 中间按键图片触发
        mAddBt.setOnClickListener(this);

        mTabHost.setCurrentTab(0);
        mTabHost.setOnTabChangedListener(this);

        // 注册接收注销通知和消息通知，控制是否显示消息提示小红点
        IntentFilter filter = new IntentFilter(Constants.INTENT_ACTION_NOTICE);
        filter.addAction(Constants.INTENT_ACTION_LOGOUT);
        registerReceiver(mReceiver, filter);
//      开启NoticeService服务，该服务进行了初始化的服务操作：例如注册接收必要的广播通知等。
//      将本activity与NoticeService绑定
        NoticeUtils.bindToService(this);

        checkUpdate();
    }

    private void checkUpdate() {
        if (!AppContext.get(AppConfig.KEY_CHECK_UPDATE, true)) {
            return;
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                new UpdateManager(MainActivity.this, false).checkUpdate();
            }
        }, 2000);
    }

    @Override
    public void initData() {

    }

    private void initTabs() {
        //调用枚举类MainTab的values方法，获得MainTab枚举类数组
        MainTab[] tabs = MainTab.values();
        final int size = tabs.length;
        for (int i = 0; i < size; i++) {
            //根据index获得具体对应的MainTab
            MainTab mainTab = tabs[i];
            //初始化每个Tab对应的图标、文字
            TabHost.TabSpec tab = mTabHost.newTabSpec(getString(mainTab.getResName()));
            View indicator = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.tab_indicator, null);
            TextView title = (TextView) indicator.findViewById(R.id.tab_title);
            Drawable drawable = this.getResources().getDrawable(
                    mainTab.getResIcon());
            title.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null,
                    null);
            if (i == 2) {//中间的快速按钮内容页设置为不可见
                indicator.setVisibility(View.INVISIBLE);
                mTabHost.setNoTabChangedTag(getString(mainTab.getResName()));
            }
            title.setText(getString(mainTab.getResName()));
            tab.setIndicator(indicator);
            tab.setContent(new TabHost.TabContentFactory() {

                @Override
                public View createTabContent(String tag) {
                    return new View(MainActivity.this);
                }
            });
            //为TabHost添加对应的页面内容
            mTabHost.addTab(tab, mainTab.getClz(), null);
            //如果对应的是我的界面则添加一个消息提醒的右上角图标。
            if (mainTab.equals(MainTab.ME)) {
                View cn = indicator.findViewById(R.id.tab_mes);
                mBvNotice = new BadgeView(MainActivity.this, cn);
                mBvNotice.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
                mBvNotice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                mBvNotice.setBackgroundResource(R.drawable.notification_bg);
                mBvNotice.setGravity(Gravity.CENTER);
            }
            mTabHost.getTabWidget().getChildAt(i).setOnTouchListener(this);
        }
    }

    @Override
    public void onTabChanged(String tabId) {
        final int size = mTabHost.getTabWidget().getTabCount();
        for (int i = 0; i < size; i++) {
            View v = mTabHost.getTabWidget().getChildAt(i);
            if (i == mTabHost.getCurrentTab()) {
                v.setSelected(true);
            } else {
                v.setSelected(false);
            }
        }
//      点击了“我”栏目，消息提示红点隐去
        if (tabId.equals(getString(MainTab.ME.getResName()))) {
            mBvNotice.setText("");
            mBvNotice.hide();
        }
        supportInvalidateOptionsMenu();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.quick_option_iv:
                onClickTweetPub();
                break;
            default:
                break;
        }
    }

//  添加问答
    private void onClickTweetPub() {
        UIHelper.showTweetPub(this);
    }


    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentByTag(
                mTabHost.getCurrentTabTag());
    }

    /**
     * 监听返回--是否退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 是否退出应用
            if (AppContext.get(AppConfig.KEY_DOUBLE_CLICK_EXIT, true)) {
                return mDoubleClickExit.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
