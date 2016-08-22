package com.scau.easyfarm.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scau.easyfarm.R;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.bean.SimpleBackPage;
import com.scau.easyfarm.util.TDevice;
import com.scau.easyfarm.util.UIHelper;
import com.scau.easyfarm.util.UpdateManager;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AboutAppFragment extends BaseFragment {

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
//            UIHelper.openBrowser(getActivity(), "https://www.oschina.net");
            break;
        case R.id.tv_knowmore:
//            UIHelper.openBrowser(getActivity(),
//                    "https://www.oschina.net/home/aboutosc");
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
}
