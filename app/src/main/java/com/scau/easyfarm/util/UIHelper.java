package com.scau.easyfarm.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;

import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.bean.SimpleBackPage;
import com.scau.easyfarm.bean.Tweet;
import com.scau.easyfarm.bean.VillageService;
import com.scau.easyfarm.fragment.TweetExpertChooseFragment;
import com.scau.easyfarm.fragment.VillageServiceDetailFragment;
import com.scau.easyfarm.ui.TweetTypeChooseActivity;
import com.scau.easyfarm.interf.ICallbackResult;
import com.scau.easyfarm.service.DownloadService;
import com.scau.easyfarm.service.NoticeService;
import com.scau.easyfarm.ui.DetailActivity;
import com.scau.easyfarm.ui.LoginActivity;
import com.scau.easyfarm.ui.SimpleBackActivity;

/**
 * 界面帮助类
 */
public class UIHelper {


    /**
     * 显示用户中心页面
     *
     * @param context
     * @param hisuid
     * @param hisuid
     * @param hisname
     */
    public static void showUserCenter(Context context, int hisuid,
                                      String hisname) {
        if (hisuid == 0 && hisname.equalsIgnoreCase("匿名")) {
            AppContext.showToast("提醒你，该用户为非会员");
            return;
        }
        Bundle args = new Bundle();
        args.putInt("his_id", hisuid);
        args.putString("his_name", hisname);
        showSimpleBack(context, SimpleBackPage.USER_CENTER, args);
    }

    /**
     * 显示登录界面
     */
    public static void showLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

//  把class拼装进SimpleBackActivity的fragment中，带参数
    public static void showSimpleBack(Context context, SimpleBackPage page,
                                      Bundle args) {
        Intent intent = new Intent(context, SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_ARGS, args);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        context.startActivity(intent);
    }

    public static void showSimpleBack(Context context, SimpleBackPage page) {
        Intent intent = new Intent(context, SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        context.startActivity(intent);
    }

    public static void showSimpleBackForResult(Fragment fragment,
                                               int requestCode, SimpleBackPage page, Bundle args) {
        Intent intent = new Intent(fragment.getActivity(),
                SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_ARGS, args);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void showSimpleBackForResult(Fragment fragment,
                                               int requestCode, SimpleBackPage page) {
        Intent intent = new Intent(fragment.getActivity(),
                SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void showSimpleBackForResult(Activity context,
                                               int requestCode, SimpleBackPage page, Bundle args) {
        Intent intent = new Intent(context, SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_ARGS, args);
        context.startActivityForResult(intent, requestCode);
    }

    public static void showSimpleBackForResult(Activity context,
                                               int requestCode, SimpleBackPage page) {
        Intent intent = new Intent(context, SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 发送通知广播
     *
     * @param context
     */
    public static void sendBroadcastForNotice(Context context) {
        Intent intent = new Intent(NoticeService.INTENT_ACTION_BROADCAST);
        context.sendBroadcast(intent);
    }

    /**
     * 显示用户的消息中心
     *
     * @param context
     */
    public static void showMyMes(Context context) {
        showSimpleBack(context, SimpleBackPage.MY_MES);
    }

    /**
     * 显示设置界面
     *
     * @param context
     */
    public static void showSetting(Context context) {
        showSimpleBack(context, SimpleBackPage.SETTING);
    }

    /**
     * 显示通知设置界面
     *
     * @param context
     */
    public static void showSettingNotification(Context context) {
        showSimpleBack(context, SimpleBackPage.SETTING_NOTIFICATION);
    }

    /**
     * 显示关于界面
     *
     * @param context
     */
    public static void showAboutApp(Context context) {
        showSimpleBack(context, SimpleBackPage.ABOUT_APP);
    }

    /**
     * 显示修改密码界面
     *
     * @param context
     */
    public static void showChangePassword(Context context) {
        showSimpleBack(context, SimpleBackPage.CHANGE_PASSWORD);
    }


    /**
     * 清除app缓存
     *
     * @param activity
     */
    public static void clearAppCache(Activity activity) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    AppContext.showToastShort("缓存清除成功");
                } else {
                    AppContext.showToastShort("缓存清除失败");
                }
            }
        };
        new Thread() {
            @Override
            public void run() {
                Message msg = new Message();
                try {
                    AppContext.getInstance().clearAppCache();
                    msg.what = 1;
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = -1;
                }
                handler.sendMessage(msg);
            }
        }.start();
    }

//  打开下载新版本apk服务
    public static void openDownLoadService(Context context, String downurl,
                                           String tilte) {
        final ICallbackResult callback = new ICallbackResult() {

            @Override
            public void OnBackResult(Object s) {}
        };
        ServiceConnection conn = new ServiceConnection() {

            @Override
            public void onServiceDisconnected(ComponentName name) {}

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                DownloadService.DownloadBinder binder = (DownloadService.DownloadBinder) service;
                binder.addCallback(callback);
                binder.start();

            }
        };
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(DownloadService.BUNDLE_KEY_DOWNLOAD_URL, downurl);
        intent.putExtra(DownloadService.BUNDLE_KEY_TITLE, tilte);
        context.startService(intent);
        context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

//  显示添加问答界面
    public static void showTweetPub(Context context) {
        showSimpleBack(context, SimpleBackPage.TWEET_PUB);
    }

    /**
     * 显示问题详情
     *
     * @param context context
     * @param tweetid 动弹的id
     */
    public static void showTweetDetail(Context context, Tweet tweet, int tweetid) {
        Intent intent = new Intent(context, DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("tweet_id", tweetid);
        bundle.putInt(DetailActivity.BUNDLE_KEY_DISPLAY_TYPE,
                DetailActivity.DISPLAY_TWEET);
        if (tweet != null) {
            bundle.putParcelable("tweet", tweet);
        }
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void showTweetTypeChoose(Activity context, int parentId, int requestCode) {
        Intent intent = new Intent(context,TweetTypeChooseActivity.class);
        Bundle args = new Bundle();
        args.putInt(TweetTypeChooseActivity.MANUALCOTEGORYPARENT, parentId);
        intent.putExtras(args);
        context.startActivityForResult(intent, requestCode);
    }

    public static void showTweetExpertChoose(Fragment fragment, int typeId, int requestCode) {
        Bundle bundle = new Bundle();
        bundle.putInt(TweetExpertChooseFragment.TWEET_EXPERT_MANUAL_TYPE, typeId);
        showSimpleBackForResult(fragment, requestCode, SimpleBackPage.TWEET_CHOOSE_EXPERT, bundle);
    }

    //  显示添加问答界面
    public static void showVillageServiceApply(Context context) {
        showSimpleBack(context, SimpleBackPage.VILLAGE_SERVICE_APPLY);
    }

    public static void showVillageServiceDetail(Context context, int villageServiceId) {
        Bundle bundle = new Bundle();
        bundle.putInt(VillageServiceDetailFragment.VILLAGE_SERVICE_ID_CODE,villageServiceId);
        showSimpleBack(context,SimpleBackPage.VILLAGE_SERVICE_DETAIL,bundle);
    }

    public static void showAreaChoose(Fragment fragment, int requestCode) {
        showSimpleBackForResult(fragment, requestCode,SimpleBackPage.CHOOSE_AREA);
    }



}














