package com.scau.easyfarm.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.bean.Notice;
import com.scau.easyfarm.bean.ResultBean;
import com.scau.easyfarm.bean.Tweet;
import com.scau.easyfarm.bean.VillageProofResource;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.TLog;
import com.scau.easyfarm.util.UIHelper;

import java.io.ByteArrayInputStream;


public class ServerTaskUtils {

    public static void uploadProofResource(Context context,VillageProofResource villageProofResource){
        Intent intent = new Intent(context,ProofResourceService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ProofResourceService.BUNDLE_PUB_RESOURCE,villageProofResource);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    public static void startGetNoticeService(Context context){
        Intent intent = new Intent(context, NoticeService.class);
        context.startService(intent);
    }

    public static void stopGetNoticeService(Context context){
        Intent intent = new Intent(context,NoticeService.class);
        context.stopService(intent);
    }

}
