package com.scau.easyfarm.service;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;

import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.bean.VillageProofResource;
import com.scau.easyfarm.util.DialogHelp;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class ServerTaskUtils extends BaseFragment implements EasyPermissions.PermissionCallbacks {

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

    @Override
    public boolean shouldShowRequestPermissionRationale(String permission) {
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        String tip = ">在设置-应用-农技通权限中允许读取文件，以正常使用下载和更新app的功能";
        if (perms.get(0).equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            tip = ">在设置-应用-农技通权限中允许读取文件，以正常使用下载和更新app的功能";
        }
        // 权限被拒绝了
        DialogHelp.getConfirmDialog(getActivity(),
                "权限申请",
                tip,
                "去设置",
                "取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_APPLICATION_SETTINGS));
                    }
                },
                null).show();
    }
}
