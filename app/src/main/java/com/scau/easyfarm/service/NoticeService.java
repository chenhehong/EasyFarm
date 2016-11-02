package com.scau.easyfarm.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.scau.easyfarm.AppConfig;
import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.R;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.bean.Notice;
import com.scau.easyfarm.bean.NoticeDetail;
import com.scau.easyfarm.bean.SimpleBackPage;
import com.scau.easyfarm.ui.SimpleBackActivity;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.TLog;
import com.scau.easyfarm.util.UIHelper;

import java.io.ByteArrayInputStream;

/**
 * Created by ChenHehong on 2016/10/6.
 */
public class NoticeService extends BaseService{

//  发送消息请求的时间间隔：10秒
    private static final long INTERVAL = 1000 * 10;
    private AlarmManager mAlarmMgr;

//  定时接收器，该接收器执行消息请求
    public class AlarmReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            TLog.log("onReceive ->easyfarm收到定时获取消息");
            requestNotice();
        }
    }

    private final OperationResponseHandler mGetNoticeHandler = new OperationResponseHandler() {

        @Override
        public void onSuccess(int code, ByteArrayInputStream is, Object[] args) throws Exception {
            try {
                Notice notice = JsonUtils.toBean(NoticeDetail.class,is).getNotice();
                if (notice!=null){
                    UIHelper.sendNoticeBroadCast(NoticeService.this, notice);
//                  如果允许接收通知栏通知
                    if (AppContext.get(AppConfig.KEY_NOTIFICATION_ACCEPT, true)) {
                        notification(notice);
                    }
                }
            }catch (Exception e){
                onFailure(code,e.getMessage(),args);
            }
        }

        @Override
        public void onFailure(int code, String errorMessage, Object[] args) {
            TLog.error(code+errorMessage);
        }
    };

//  记录上次通知的消息条目
    private int lastNotifiyCount;
    private void notification(Notice notice){
        int atmeCount = notice.getAtmeCount();
        int reviewCount = notice.getReviewCount();

        int count = atmeCount + reviewCount;

        if (count == 0) {
            lastNotifiyCount = 0;
            cancellNotification(R.string.you_have_news_messages);
            return;
        }
        if (count == lastNotifiyCount)
            return;

        lastNotifiyCount = count;
        String contentTitle = getString(R.string.you_have_news_messages,
                count);
        String contentText;
        StringBuffer sb = new StringBuffer();
        if (atmeCount > 0) {
            sb.append(getString(R.string.atme_count, atmeCount)).append(" ");
        }
        if (reviewCount > 0) {
            sb.append(getString(R.string.review_count, reviewCount))
                    .append(" ");
        }
        contentText = sb.toString();

        Intent intent = new Intent(this, SimpleBackActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, SimpleBackPage.MY_MES.getValue());
        boolean setSound = false;
        boolean setVibration = false;
        if (AppContext.get(AppConfig.KEY_NOTIFICATION_SOUND, true)) {
            setSound = true;
        }
        if (AppContext.get(AppConfig.KEY_NOTIFICATION_VIBRATION, true)) {
            setVibration = true;
        }
        notifySimpleNotifycation(R.string.you_have_news_messages, contentTitle,contentTitle,contentText, true, true,intent,setSound,setVibration);
    }

    @Override
    public void onMyHandleIntent(Intent intent) {
//      利用Alarm Manager在循环运行消息请求，即使退出程序也能运行,但是目前在mainactivity退出后服务会停止并且不会继续运行消息请求
        mAlarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        startRequestAlarm();
        requestNotice();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelRequestAlarm();
    }

    private void startRequestAlarm() {
        cancelRequestAlarm();
        // 从1秒后开始，每隔INTERVAL时间执行getOperationIntent()
        mAlarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + 1000, INTERVAL,
                getOperationIntent());
    }

    /**
     * <!-- kymjs --> 即使启动PendingIntent的原进程结束了的话,PendingIntent本身仍然还存在，可在其他进程（
     * PendingIntent被递交到的其他程序）中继续使用.
     * 如果我在从系统中提取一个PendingIntent的，而系统中有一个和你描述的PendingIntent对等的PendingInent,
     * 那么系统会直接返回和该PendingIntent其实是同一token的PendingIntent，
     * 而不是一个新的token和PendingIntent。然而你在从提取PendingIntent时，通过FLAG_CANCEL_CURRENT参数，
     * 让这个老PendingIntent的先cancel()掉，这样得到的pendingInten和其token的就是新的了。
     */
    private void cancelRequestAlarm() {
        mAlarmMgr.cancel(getOperationIntent());
    }

    /**
     * 采用轮询方式实现消息推送
     * 每次被调用都去执行一次{@link #AlarmReceiver}onReceive()方法
     * @return
     */
    private PendingIntent getOperationIntent() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent operation = PendingIntent.getBroadcast(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        return operation;
    }

    /**
     * 请求是否有新通知
     */
    private void requestNotice() {
        EasyFarmServerApi.getNoticeCount(mGetNoticeHandler);
    }
}
