package com.scau.easyfarm.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.bean.Tweet;


public class ServerTaskUtils {

    public static void pubTweet(Context context, Tweet tweet) {
        Intent intent = new Intent(ServerTaskService.ACTION_PUB_TWEET);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ServerTaskService.BUNDLE_PUB_TWEET_TASK, tweet);
        intent.putExtras(bundle);
        intent.setPackage(AppContext.getInstance().getPackageName());
        context.startService(intent);
    }

}
