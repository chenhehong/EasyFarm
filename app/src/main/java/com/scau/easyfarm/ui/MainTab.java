package com.scau.easyfarm.ui;


import com.scau.easyfarm.R;
import com.scau.easyfarm.fragment.MyFunctionFragment;
import com.scau.easyfarm.fragment.MyInformationFragment;
import com.scau.easyfarm.viewpagerfragment.NewsViewPagerFragment;
import com.scau.easyfarm.viewpagerfragment.TweetsViewPagerFragment;

public enum MainTab {
//  每一个代表一个MainTab对象
	NEWS(0, R.string.main_tab_name_function, R.drawable.tab_icon_function,
			MyFunctionFragment.class),

	TWEET(1, R.string.main_tab_name_question, R.drawable.tab_icon_question,
			TweetsViewPagerFragment.class),

	QUICK(2, R.string.main_tab_name_quick, R.drawable.tab_icon_function,
			null),

	EXPLORE(3, R.string.main_tab_name_news, R.drawable.tab_icon_news,
			NewsViewPagerFragment.class),

	ME(4, R.string.main_tab_name_me, R.drawable.tab_icon_me,
			MyInformationFragment.class);

	private int idx;
	private int resName;
	private int resIcon;
	private Class<?> clz;

	/**
	 * @param idx 在TabHost中的位置，从0开始
	 * @param resName Tab的名称
	 * @param resIcon Tab的icon
	 * @param clz 每个枚举对应的Fragment的class
	 */
	private MainTab(int idx, int resName, int resIcon, Class<?> clz) {
		this.idx = idx;
		this.resName = resName;
		this.resIcon = resIcon;
		this.clz = clz;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public int getResName() {
		return resName;
	}

	public void setResName(int resName) {
		this.resName = resName;
	}

	public int getResIcon() {
		return resIcon;
	}

	public void setResIcon(int resIcon) {
		this.resIcon = resIcon;
	}

	public Class<?> getClz() {
		return clz;
	}

	public void setClz(Class<?> clz) {
		this.clz = clz;
	}
}
