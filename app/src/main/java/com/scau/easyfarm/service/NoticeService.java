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

//  发送消息请求的时间间隔：单位毫秒
    private static final long INTERVAL = 1000 * 60;
    boolean shouldStop = false;

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
        while (true){
            if (shouldStop) break;
            TLog.log("easyfarm定时获取消息");
            requestNotice();
            try {
                Thread.sleep(INTERVAL);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelThread();
    }

    private void cancelThread() {
        shouldStop = true;
    }

    /**
     * 请求是否有新通知
     */
    private void requestNotice() {
        EasyFarmServerApi.getNoticeCount(mGetNoticeHandler);
    }
}
