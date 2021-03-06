package com.scau.easyfarm.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.bean.Update;
import com.scau.easyfarm.service.ServerTaskUtils;

import java.io.ByteArrayInputStream;

/**
 * 更新管理类
 *
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年11月18日 下午4:21:00
 */

public class UpdateManager {

    private Update mUpdate;

    private Context mContext;

    private boolean isShow = false;

    private ProgressDialog _waitDialog;

    private OperationResponseHandler mCheckUpdateHandle = new OperationResponseHandler() {

        @Override
        public void onFailure(int code, String errorMessage, Object[] args) {
            hideCheckDialog();
            if (isShow) {
                showFaileDialog();
            }
        }

        @Override
        public void onSuccess(int code, ByteArrayInputStream is, Object[] args) {
            hideCheckDialog();
            mUpdate = JsonUtils.toBean(Update.class,is);
            onFinshCheck();
        }
    };

    public UpdateManager(Context context, boolean isShow) {
        this.mContext = context;
        this.isShow = isShow;
    }

    public boolean haveNew() {
        if (this.mUpdate == null) {
            return false;
        }
        boolean haveNew = false;
        int curVersionCode = TDevice.getVersionCode(AppContext
                .getInstance().getPackageName());
        if (curVersionCode < mUpdate.getAndroidBean()
                .getVersionCode()) {
            haveNew = true;
        }
        return haveNew;
    }

    public void checkUpdate() {
        if (isShow) {
            showCheckDialog();
        }
        EasyFarmServerApi.checkUpdate(mCheckUpdateHandle);
    }

    private void onFinshCheck() {
        if (haveNew()) {
            showUpdateInfo();
        } else {
            if (isShow) {
                showLatestDialog();
            }
        }
    }

    private void showCheckDialog() {
        if (_waitDialog == null) {
            _waitDialog = DialogHelp.getWaitDialog((Activity) mContext, "正在获取新版本信息...");
        }
        _waitDialog.show();
    }

    private void hideCheckDialog() {
        if (_waitDialog != null) {
            _waitDialog.dismiss();
        }
    }

    private void showUpdateInfo() {
        if (mUpdate == null) {
            return;
        }
        AlertDialog.Builder dialog = DialogHelp.getConfirmDialog(mContext, mUpdate.getAndroidBean().getUpdateLog()+"**请注意！你必须安装最新的版本才能够正常使用系统，否则某些功能可能会发生异常！是否更新到最新的版本？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ServerTaskUtils.startDownloadAppService(mContext, mUpdate.getAndroidBean().getDownloadUrl(), mUpdate.getAndroidBean().getVersionName());
            }
        });
        dialog.setTitle("发现新版本");
        dialog.show();
    }

    private void showLatestDialog() {
        DialogHelp.getMessageDialog(mContext, "已经是新版本了").show();
    }

    private void showFaileDialog() {
        DialogHelp.getMessageDialog(mContext, "网络异常，无法获取新版本信息").show();
    }
}
