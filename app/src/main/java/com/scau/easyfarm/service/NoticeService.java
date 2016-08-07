package com.scau.easyfarm.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.ByteArrayInputStream;
import java.lang.ref.WeakReference;

import cz.msebera.android.httpclient.Header;

/**
 * 通知服务类，例如收广播或发广播
 */
public class NoticeService extends Service {
    public static final String INTENT_ACTION_GET = "easyfarm.app.service.GET_NOTICE";
    public static final String INTENT_ACTION_CLEAR = "easyfarm.app.service.CLEAR_NOTICE";
    public static final String INTENT_ACTION_BROADCAST = "easyfarm.app.service.BROADCAST";
    public static final String INTENT_ACTION_SHUTDOWN = "easyfarm.app.service.SHUTDOWN";
    public static final String INTENT_ACTION_REQUEST = "easyfarm.app.service.REQUEST";
    public static final String BUNDLE_KEY_TPYE = "bundle_key_type";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
