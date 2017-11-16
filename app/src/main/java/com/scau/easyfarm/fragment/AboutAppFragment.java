package com.scau.easyfarm.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scau.easyfarm.R;
import com.scau.easyfarm.api.ApiHttpClient;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.bean.SimpleBackPage;
import com.scau.easyfarm.util.DialogHelp;
import com.scau.easyfarm.util.TDevice;
import com.scau.easyfarm.util.UIHelper;
import com.scau.easyfarm.util.UpdateManager;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pub.devrel.easypermissions.EasyPermissions;

public class AboutAppFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks{

    @InjectView(R.id.tv_version)
    TextView mTvVersionStatus;

    @InjectView(R.id.tv_version_name)
    TextView mTvVersionName;

    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.inject(this, view);
        initView(view);
        initData();
        return view;
    }

    @Override
    public void initView(View view) {
        view.findViewById(R.id.rl_check_update).setOnClickListener(this);
        view.findViewById(R.id.rl_feedback).setOnClickListener(this);
        view.findViewById(R.id.tv_website).setOnClickListener(this);
        view.findViewById(R.id.tv_knowmore).setOnClickListener(this);
    }

    @Override
    public void initData() {
        mTvVersionName.setText("V " + TDevice.getVersionName());
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
        case R.id.rl_check_update:
            onClickUpdate();
            break;
        case R.id.rl_feedback:
            showFeedBack();
            break;
        case R.id.tv_website:
            UIHelper.openBrowser(getActivity(), ApiHttpClient.getAbsoluteApiUrl(""));
            break;
        case R.id.tv_knowmore:
            UIHelper.openBrowser(getActivity(), ApiHttpClient.getAbsoluteApiUrl(""));
            break;
        default:
            break;
        }
    }

    private void onClickUpdate() {
        new UpdateManager(getActivity(), true).checkUpdate();
    }

    private void showFeedBack() {
//         TDevice.sendEmail(getActivity(), "用户反馈-OSC Android客户端", "",
//         "apposchina@163.com");
        UIHelper.showSimpleBack(getActivity(), SimpleBackPage.FEED_BACK);
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
        String tip = ">在设置-应用-华南农技通权限中允许读取文件，以正常使用下载和更新app的功能";
        if (perms.get(0).equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            tip = ">在设置-应用-华南农技通权限中允许读取文件，以正常使用下载和更新app的功能";
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
