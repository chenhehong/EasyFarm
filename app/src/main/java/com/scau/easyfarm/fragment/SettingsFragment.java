package com.scau.easyfarm.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scau.easyfarm.AppConfig;
import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.AppManager;
import com.scau.easyfarm.R;
import com.scau.easyfarm.base.BaseFragment;

import org.kymjs.kjframe.http.HttpConfig;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.scau.easyfarm.bean.SimpleBackPage;
import com.scau.easyfarm.bean.User;
import com.scau.easyfarm.util.DialogHelp;
import com.scau.easyfarm.util.FileUtil;
import com.scau.easyfarm.util.MethodsCompat;
import com.scau.easyfarm.util.UIHelper;
import com.scau.easyfarm.widget.ToggleButton;

/**
 * 系统设置界面
 *
 * @author kymjs
 */
public class SettingsFragment extends BaseFragment {

    @InjectView(R.id.tb_loading_img)
    ToggleButton mTbLoadImg;
    @InjectView(R.id.tv_cache_size)
    TextView mTvCacheSize;
    @InjectView(R.id.setting_logout)
    TextView mTvExit;
    @InjectView(R.id.rl_myinformation_settings)
    View myinformationSetting;
    @InjectView(R.id.tb_double_click_exit)
    ToggleButton mTbDoubleClickExit;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container,
                false);
        ButterKnife.inject(this, view);
        initView(view);
        initData();
        return view;
    }

    @Override
    public void initView(View view) {
        mTbLoadImg.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                AppContext.setLoadImage(on);
            }
        });

        mTbDoubleClickExit.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                AppContext.set(AppConfig.KEY_DOUBLE_CLICK_EXIT, on);
            }
        });

        view.findViewById(R.id.rl_password_setting).setOnClickListener(this);
        view.findViewById(R.id.rl_myinformation_settings).setOnClickListener(this);
        view.findViewById(R.id.rl_loading_img).setOnClickListener(this);
        view.findViewById(R.id.rl_notification_settings).setOnClickListener(
                this);
        view.findViewById(R.id.rl_clean_cache).setOnClickListener(this);
        view.findViewById(R.id.rl_double_click_exit).setOnClickListener(this);
        view.findViewById(R.id.rl_about).setOnClickListener(this);
        view.findViewById(R.id.rl_exit).setOnClickListener(this);

        if (!AppContext.getInstance().isLogin()) {
            mTvExit.setText("退出");
        }
    }

    @Override
    public void initData() {
        if (AppContext.get(AppConfig.KEY_LOAD_IMAGE, true)) {
            mTbLoadImg.setToggleOn();
        } else {
            mTbLoadImg.setToggleOff();
        }

        if (AppContext.get(AppConfig.KEY_DOUBLE_CLICK_EXIT, true)) {
            mTbDoubleClickExit.setToggleOn();
        } else {
            mTbDoubleClickExit.setToggleOff();
        }

        caculateCacheSize();
    }

    /**
     * 计算缓存的大小
     */
    private void caculateCacheSize() {
        long fileSize = 0;
        String cacheSize = "0KB";
        File filesDir = getActivity().getFilesDir();
        File cacheDir = getActivity().getCacheDir();

        fileSize += FileUtil.getDirSize(filesDir);
        fileSize += FileUtil.getDirSize(cacheDir);
        // 2.2版本才有将应用缓存转移到sd卡的功能
        if (AppContext.isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            File externalCacheDir = MethodsCompat
                    .getExternalCacheDir(getActivity());
            fileSize += FileUtil.getDirSize(externalCacheDir);
            fileSize += FileUtil.getDirSize(new File(
                    org.kymjs.kjframe.utils.FileUtils.getSDCardPath()
                            + File.separator + HttpConfig.CACHEPATH));
        }
        if (fileSize > 0)
            cacheSize = FileUtil.formatFileSize(fileSize);
        mTvCacheSize.setText(cacheSize);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.rl_loading_img:
                mTbLoadImg.toggle();
                break;
            case R.id.rl_notification_settings:
                UIHelper.showSettingNotification(getActivity());
                break;
            case R.id.rl_clean_cache:
                onClickCleanCache();
                break;
            case R.id.rl_double_click_exit:
                mTbDoubleClickExit.toggle();
                break;
            case R.id.rl_about:
                UIHelper.showAboutApp(getActivity());
                break;
            case R.id.rl_exit:
                onClickExit();
                break;
            case R.id.rl_password_setting:
                if (!AppContext.getInstance().isLogin()){
                    showLoginActivity();
                }else{
                    UIHelper.showChangePassword(getActivity());
                }
                break;
            case R.id.rl_myinformation_settings:
                if (!AppContext.getInstance().isLogin()){
                    showLoginActivity();
                }else{
                    if (AppContext.getInstance().getLoginUser().getRoleName().equals(User.NORMALROLE)){
                        UIHelper.showSimpleBack(getActivity(), SimpleBackPage.MODIFIED_COMMONUSERINFORMATION);
                    }else{
                        UIHelper.showSimpleBack(getActivity(),SimpleBackPage.MODIFIED_EXPERTINFORMATION);
                    }
                }
                break;
            default:
                break;
        }

    }

    public void showLoginActivity(){
        AppContext.showToast(R.string.unlogin);
        UIHelper.showLoginActivity(getActivity());
    }

    private void onClickCleanCache() {
        DialogHelp.getConfirmDialog(getActivity(), "是否清空缓存?", new DialogInterface.OnClickListener
                () {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                UIHelper.clearAppCache(getActivity());
                mTvCacheSize.setText("0KB");
            }
        }).show();
    }

    private void onClickExit() {
        AppManager.getAppManager().AppExit(getActivity());
        getActivity().finish();
    }
}
