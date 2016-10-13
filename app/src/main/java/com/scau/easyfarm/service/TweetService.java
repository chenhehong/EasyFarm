package com.scau.easyfarm.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.scau.easyfarm.R;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.bean.Result;
import com.scau.easyfarm.bean.ResultBean;
import com.scau.easyfarm.bean.Tweet;
import com.scau.easyfarm.util.JsonUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TweetService extends BaseService {

    public static final String BUNDLE_PUB_TWEET_TASK = "BUNDLE_PUB_TWEET_TASK";
    private static final String KEY_TWEET = "tweet_";

    class PublicTweetResponseHandler extends OperationResponseHandler {

		String key = null;

		public PublicTweetResponseHandler(Looper looper, Object... args) {
		    super(looper, args);
		    key = (String) args[1];
		}

		@Override
		public void onSuccess(int code, ByteArrayInputStream is, Object[] args)
			throws Exception {
		    Tweet tweet = (Tweet) args[0];
		    final int id = tweet.getId();
		    Result res = JsonUtils.toBean(ResultBean.class, is).getResult();
		    if (res.OK()) {
				notifySimpleNotifycation(id,
				getString(R.string.tweet_publish_success),
				getString(R.string.tweet_public),
				getString(R.string.tweet_publish_success), false, true);
//				new Handler().postDelayed(new Runnable() {
//				    @Override
//				    public void run() {
//						cancellNotification(id);
//				    }
//				}, 3000);
				removePenddingTask(key + id);
				if (tweet.getImageFiles()!=null&&tweet.getImageFiles().size()>0) {
				    File imgFile = new File(tweet.getImageFiles().get(0).getPath());
				    if (imgFile.exists()) {
						imgFile.delete();
				    }
				}
		    } else {
				onFailure(100, res.getErrorMessage(), args);
		    }
		}

	@Override
	public void onFailure(int code, String errorMessage, Object[] args) {
	    Tweet tweet = (Tweet) args[0];
//		此id用于标示你下拉栏通知的消息，用于取消等操作
	    int id = tweet.getId();
	    notifySimpleNotifycation(id,
		    getString(R.string.tweet_publish_faile),
		    getString(R.string.tweet_public),
		    code == 100 ? errorMessage
			    : getString(R.string.tweet_publish_faile), false,
		    true);
	    removePenddingTask(key + id);
	}

		@Override
		public void onFinish() {
	    	tryToStopServie();
		}
    }

    public TweetService(String name) {
		super(name);
    }

    @Override
    public void onCreate() {
		super.onCreate();
    }

	@Override
	public void onMyHandleIntent(Intent intent) {
		Tweet tweet = intent.getParcelableExtra(BUNDLE_PUB_TWEET_TASK);
		if (tweet != null) {
			pubTweet(tweet);
		}
	}

	private void pubTweet(final Tweet tweet) {
		tweet.setId((int) System.currentTimeMillis());
		int id = tweet.getId();
		addPenddingTask(KEY_TWEET + id);
		notifySimpleNotifycation(id, getString(R.string.tweet_publishing),
			getString(R.string.tweet_public),
			getString(R.string.tweet_publishing), true, false);
		EasyFarmServerApi.pubTweet(tweet, new PublicTweetResponseHandler(getMainLooper(), tweet, KEY_TWEET));
    }

    private void notifySimpleNotifycation(int id, String ticker, String title,
	    String content, boolean ongoing, boolean autoCancel) {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
			this)
			.setTicker(ticker)
			.setContentTitle(title)
			.setContentText(content)
			.setAutoCancel(true)
			.setOngoing(false)
			.setOnlyAlertOnce(true)
			.setContentIntent(
					PendingIntent.getActivity(this, 0, new Intent(), 0))
			.setSmallIcon(R.drawable.ic_notification);

		// if (AppContext.isNotificationSoundEnable()) {
		// builder.setDefaults(Notification.DEFAULT_SOUND);
		// }

		Notification notification = builder.build();

		NotificationManagerCompat.from(this).notify(id, notification);
    }
}
