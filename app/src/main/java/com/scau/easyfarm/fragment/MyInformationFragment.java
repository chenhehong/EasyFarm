package com.scau.easyfarm.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.R;
import com.scau.easyfarm.api.ApiHttpClient;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.bean.Constants;
import com.scau.easyfarm.bean.MyInformation;
import com.scau.easyfarm.bean.Notice;
import com.scau.easyfarm.bean.SimpleBackPage;
import com.scau.easyfarm.bean.User;
import com.scau.easyfarm.cache.CacheManager;
import com.scau.easyfarm.ui.MainActivity;
import com.scau.easyfarm.util.DialogHelp;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.TDevice;
import com.scau.easyfarm.util.UIHelper;
import com.scau.easyfarm.widget.AvatarView;
import com.scau.easyfarm.widget.BadgeView;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by ChenHehong on 2016/6/11.
 * 登录用户中心页面
 * */
public class MyInformationFragment extends BaseFragment{

    @InjectView(R.id.iv_avatar)
    AvatarView mIvAvatar;
    @InjectView(R.id.tv_name)
    TextView mTvName;
    @InjectView(R.id.tv_mes)
    View mMesView;
    @InjectView(R.id.ll_user_container)
    View mUserContainer;
    @InjectView(R.id.rl_user_unlogin)
    View mUserUnLogin;
    @InjectView(R.id.btn_logout)
    Button btnLogout;

    private static BadgeView mMesCount;
    private boolean mIsWatingLogin;

    private User mInfo;
//  缓存任务，用于异步操作用户的缓存信息
    private AsyncTask<String, Void, User> mCacheTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter(Constants.INTENT_ACTION_LOGOUT);
        filter.addAction(Constants.INTENT_ACTION_USER_CHANGE);
//      开启接收用户退出或者用户改变的广播
        getActivity().registerReceiver(mReceiver, filter);
    }

    //  fragmet类实例化后执行：onCreat>>onCreateView>>onViewCreated
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_information,
                container, false);
        ButterKnife.inject(this, view);
        initView(view);
        return view;
    }

    // 请求网络数据并设置给View
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestData(true);
        mInfo = AppContext.getInstance().getLoginUser();
        fillUI();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.INTENT_ACTION_LOGOUT)) {
                mIsWatingLogin = true;
                steupUser();
//              消息通知红点关闭
                mMesCount.hide();
            } else if (action.equals(Constants.INTENT_ACTION_USER_CHANGE)) {
                requestData(true);
            } else if (action.equals(Constants.INTENT_ACTION_NOTICE)) {
                setNotice();
            }
        }
    };

    @Override
    public void initView(View view) {
        mIvAvatar.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        view.findViewById(R.id.rl_message).setOnClickListener(this);
        view.findViewById(R.id.rl_setting).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIHelper.showSetting(getActivity());
                }
            }
        );
        mUserUnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.showLoginActivity(getActivity());
            }
        });

        mMesCount = new BadgeView(getActivity(), mMesView);
        mMesCount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        mMesCount.setBadgePosition(BadgeView.POSITION_CENTER);
        mMesCount.setGravity(Gravity.CENTER);
        mMesCount.setBackgroundResource(R.drawable.notification_bg);
    }

//  设置用户头像、用户名栏目的显示状态
    private void steupUser() {
        if (mIsWatingLogin) {
            mUserContainer.setVisibility(View.GONE);
            mUserUnLogin.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.GONE);
        } else {
            mUserContainer.setVisibility(View.VISIBLE);
            mUserUnLogin.setVisibility(View.GONE);
            btnLogout.setVisibility(View.VISIBLE);
        }
    }

    // 刷新登录用户的数据
    private void requestData(boolean refresh) {
        if (AppContext.getInstance().isLogin()) {
            mIsWatingLogin = false;
            String key = getCacheKey();
//          如果需要刷新或者有网络并且缓存没有数据
            if (refresh || TDevice.hasInternet()
                    && (!CacheManager.isExistDataCache(getActivity(), key))) {
                sendRequestData();
            } else {
                readCacheData(key);
            }
        } else {
            mIsWatingLogin = true;
        }
        steupUser();
    }

    private void readCacheData(String key) {
        cancelReadCacheTask();
        mCacheTask = new CacheTask(getActivity()).execute(key);
    }

    private void cancelReadCacheTask() {
        if (mCacheTask != null) {
            mCacheTask.cancel(true);
            mCacheTask = null;
        }
    }

//  发送用户信息获取请求
    private void sendRequestData() {
        int uid = AppContext.getInstance().getLoginUid();
        EasyFarmServerApi.getMyInformation(uid, mHandler);
    }


//  获取登录用户信息的handle
    private final OperationResponseHandler mHandler = new OperationResponseHandler() {
        @Override
        public void onSuccess(int code, ByteArrayInputStream is, Object[] args) {
            try {
                mInfo = JsonUtils.toBean(MyInformation.class,is).getUser();
                if (mInfo != null) {
                    fillUI();
                    AppContext.getInstance().updateUserInfo(mInfo);
                    new SaveCacheTask(getActivity(), mInfo, getCacheKey())
                            .execute();
                    checkInformationCompleteness();
                } else {
                    onFailure(code,null,args);
                }
            } catch (Exception e) {
                e.printStackTrace();
                onFailure(code,e.getMessage(),args);
            }
        }

        @Override
        public void onFailure(int code, String errorMessage, Object[] args) {
            AppContext.showToast(errorMessage + code);}
    };

//  对于非普通用户，要求必须添加邮箱和手机号码
    private void checkInformationCompleteness(){
        if (!AppContext.getInstance().getLoginUser().getRoleName().equals(User.NORMALROLE)){
            if (mInfo.getEmail().length()==0||mInfo.getPhoneNumber().length()==0){
                DialogHelp.getConfirmDialog(getActivity(), "当前用户尚未添加邮箱和手机号码，前往填写？", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UIHelper.showSimpleBack(getActivity(), SimpleBackPage.MODIFIED_EXPERTINFORMATION);
                    }
                }, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        }
    }

//  系统中每个用户信息缓存文件的cacheKey
    private String getCacheKey() {
        return "my_information" + AppContext.getInstance().getLoginUid();
    }

//  获取缓存数据并且进行操作的异步任务
    private class CacheTask extends AsyncTask<String, Void, User> {
        private final WeakReference<Context> mContext;

        private CacheTask(Context context) {
            mContext = new WeakReference<Context>(context);
        }

        @Override
        protected User doInBackground(String... params) {
            Serializable seri = CacheManager.readObject(mContext.get(),
                    params[0]);
            if (seri == null) {
                return null;
            } else {
                return (User) seri;
            }
        }

        // 读取数据成功之后就填充UI
        @Override
        protected void onPostExecute(User info) {
            super.onPostExecute(info);
            if (info != null) {
                mInfo = info;
                fillUI();
            }
        }
    }

    private void fillUI() {
        if (mInfo==null)
            return;
        mIvAvatar.setAvatarUrl(ApiHttpClient.getAbsoluteApiUrl(mInfo.getPortrait()));
        mTvName.setText("欢迎 "+mInfo.getRealName()+" 使用");
    }

//  保存缓存数据的异步任务
    private class SaveCacheTask extends AsyncTask<Void, Void, Void> {
        private final WeakReference<Context> mContext;
        private final Serializable seri;
        private final String key;

        private SaveCacheTask(Context context, Serializable seri, String key) {
            mContext = new WeakReference<Context>(context);
            this.seri = seri;
            this.key = key;
        }

        @Override
        protected Void doInBackground(Void... params) {
            CacheManager.saveObject(mContext.get(), seri, key);
            return null;
        }
    }

    @Override
    public void onClick(View v) {
        if (mIsWatingLogin) {
            AppContext.showToast(R.string.unlogin);
            UIHelper.showLoginActivity(getActivity());
            return;
        }
        final int id = v.getId();
        switch (id) {
            case R.id.iv_avatar:
                UIHelper.showSimpleBack(getActivity(),
                        SimpleBackPage.MY_INFORMATION_DETAIL);
                break;
            case R.id.rl_message:
                UIHelper.showMyMes(getActivity());
                setNoticeReaded();
                break;
            case R.id.btn_logout:
                AppContext.getInstance().Logout();
                steupUser();
                AppContext.showToastShort(R.string.tip_logout_success);
                break;
            default:
                break;
        }
    }

    private void setNoticeReaded() {
        mMesCount.setText("");
        mMesCount.hide();
    }

    public void setNotice() {
        if (MainActivity.mNotice != null) {
            Notice notice = MainActivity.mNotice;
            int atmeCount = notice.getAtmeCount();// @我
            int reviewCount = notice.getReviewCount();// 评论
            int activeCount = atmeCount + reviewCount;// 信息总数
            if (activeCount > 0) {
                mMesCount.setText(activeCount + "");
                mMesCount.show();
            } else {
                mMesCount.hide();
            }
        } else {
            mMesCount.hide();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }

}
