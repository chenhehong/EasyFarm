package com.scau.easyfarm.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.scau.easyfarm.R;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseService extends IntentService {

	private static final String SERVICE_NAME = "BaseService";

//	记录Service的所有排队执行中的事务
    public  List<String> penddingTasks = new ArrayList<String>();

    public synchronized void tryToStopServie() {
		if (penddingTasks == null || penddingTasks.size() == 0) {
	    	stopSelf();
		}
    }

	public synchronized void addPenddingTask(String key) {
		penddingTasks.add(key);
    }

    public synchronized void removePenddingTask(String key) {
		penddingTasks.remove(key);
    }

	public BaseService() {
		this(SERVICE_NAME);
	}

    public BaseService(String name) {
		super(name);
    }

    @Override
    public void onCreate() {
		super.onCreate();
    }

//	IntentService在onCreate()函数中通过HandlerThread单独开启一个线程来处理所有Intent请求对象（通过startService的方式发送过来的）所对应的任务，
// 这样以免事务处理阻塞主线程,把处理一个intent所对应的事务都封装到叫做onHandleIntent的虚函数；
// 因此我们直接实现虚函数onHandleIntent，再在里面根据Intent的不同进行不同的事务处理就可以了。
//  注意：IntentService是用单线程（一个工作线程）处理所有工作的，所以所有的任务需要排队执行
    @Override
	protected void onHandleIntent(Intent intent) {
		onMyHandleIntent(intent);
	}

	public abstract void onMyHandleIntent(Intent intent);

    public void notifySimpleNotifycation(int id, String ticker, String title,
	    String content, boolean ongoing, boolean autoCancel,Intent newIntent) {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
			this)
			.setTicker(ticker)
			.setContentTitle(title)
			.setContentText(content)
			.setAutoCancel(autoCancel)
			.setOngoing(ongoing)
			.setOnlyAlertOnce(true)
			.setContentIntent(
					PendingIntent.getActivity(this, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT))
			.setSmallIcon(R.drawable.ic_notification)
			.setWhen(System.currentTimeMillis());

		Notification notification = builder.build();
		NotificationManagerCompat.from(this).notify(id, notification);
    }

	public void cancellNotification(int id) {
		NotificationManagerCompat.from(this).cancel(id);
    }
}
