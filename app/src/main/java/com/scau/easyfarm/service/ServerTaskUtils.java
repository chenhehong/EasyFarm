package com.scau.easyfarm.service;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.scau.easyfarm.bean.VillageProofResource;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class ServerTaskUtils{

    private final static int RC_STORE_PERM = 1011;

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

    @AfterPermissionGranted(RC_STORE_PERM)
    public static void startDownloadAppService(Context context,String downloadUrl,String versionName){
        if (EasyPermissions.hasPermissions(context,Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Intent intent = new Intent(context,DownloadService.class);
            Bundle bundle = new Bundle();
            bundle.putString(DownloadService.BUNDLE_KEY_DOWNLOAD_URL,downloadUrl);
            bundle.putString(DownloadService.BUNDLE_KEY_TITLE,versionName);
            intent.putExtras(bundle);
            context.startService(intent);
        } else {
            EasyPermissions.requestPermissions(context, "请求读取和写文件的权限", RC_STORE_PERM, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

}
