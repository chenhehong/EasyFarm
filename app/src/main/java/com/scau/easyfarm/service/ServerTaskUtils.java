package com.scau.easyfarm.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.bean.Tweet;
import com.scau.easyfarm.bean.VillageProofResource;


public class ServerTaskUtils {

    public static void pubTweet(Context context, Tweet tweet) {
        Intent intent = new Intent(context,TweetService.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(TweetService.BUNDLE_PUB_TWEET_TASK, tweet);
        intent.putExtras(bundle);
        intent.setPackage(AppContext.getInstance().getPackageName());
        context.startService(intent);
    }

    public static void uploadProofResource(Context context,VillageProofResource villageProofResource){
        Intent intent = new Intent(context,ProofResourceService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ProofResourceService.BUNDLE_PUB_RESOURCE,villageProofResource);
        intent.putExtras(bundle);
        context.startService(intent);
    }

}
