package com.scau.easyfarm.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scau.easyfarm.AppConfig;
import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.R;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.widget.ToggleButton;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SettingsNotificationFragment extends BaseFragment {
	
	@InjectView(R.id.tb_accept)
	ToggleButton mTbAccept;
	@InjectView(R.id.tb_voice) ToggleButton mTbVoice;
	@InjectView(R.id.tb_vibration) ToggleButton mTbVibration;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_settings_notifcation, container,
				false);
		ButterKnife.inject(this, view);
		initView(view);
		initData();
		return view;
	}
	
	@Override
	public void initView(View view) {
		setToggleChanged(mTbAccept, AppConfig.KEY_NOTIFICATION_ACCEPT);
		setToggleChanged(mTbVoice, AppConfig.KEY_NOTIFICATION_SOUND);
		setToggleChanged(mTbVibration, AppConfig.KEY_NOTIFICATION_VIBRATION);

		view.findViewById(R.id.rl_accept).setOnClickListener(this);
		view.findViewById(R.id.rl_voice).setOnClickListener(this);
		view.findViewById(R.id.rl_vibration).setOnClickListener(this);
	}

	public void initData() {
		setToggle(AppContext.get(AppConfig.KEY_NOTIFICATION_ACCEPT, true), mTbAccept);
		setToggle(AppContext.get(AppConfig.KEY_NOTIFICATION_SOUND, true), mTbVoice);
		setToggle(AppContext.get(AppConfig.KEY_NOTIFICATION_VIBRATION, true), mTbVibration);
	}
	
	private void setToggleChanged(ToggleButton tb, final String key) {
		tb.setOnToggleChanged(new ToggleButton.OnToggleChanged() {

			@Override
			public void onToggle(boolean on) {
				AppContext.set(key, on);
			}
		});
	}
	
	private void setToggle(boolean value, ToggleButton tb) {
		if (value)
			tb.setToggleOn();
		else
			tb.setToggleOff();
	}

	@Override
	public void onClick(View v) {
		final int id = v.getId();
		switch (id) {
		case R.id.rl_accept:
			mTbAccept.toggle();
			break;
		case R.id.rl_voice:
			mTbVoice.toggle();
			break;
		case R.id.rl_vibration:
			mTbVibration.toggle();
			break;
		}
	}
}
