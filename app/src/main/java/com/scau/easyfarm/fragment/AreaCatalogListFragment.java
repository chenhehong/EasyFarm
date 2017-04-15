package com.scau.easyfarm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.R;
import com.scau.easyfarm.adapter.AreaListAdapter;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.base.ListBaseAdapter;
import com.scau.easyfarm.bean.Area;
import com.scau.easyfarm.bean.AreaList;
import com.scau.easyfarm.ui.empty.EmptyLayout;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.UIHelper;
import com.scau.easyfarm.widget.ScrollLayout;

import java.io.ByteArrayInputStream;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class AreaCatalogListFragment extends BaseFragment implements
		OnItemClickListener{
	protected static final int STATE_NONE = 0;
	protected static final int STATE_REFRESH = 1;
	protected static final int STATE_LOADMORE = 2;

	private final static int SCREEN_PROVINCE = 0;
	private final static int SCREEN_CITY = 1;
	private final static int SCREEN_COUNTY = 2;

//	使用左右滑动的控件实现不同fragment的切换
	private static ScrollLayout mScrollLayout;
	private static ListView mLvProvince, mLvCity, mLvCounty;
	private static EmptyLayout mEmptyView;
	private static AreaListAdapter mProvinceAdapter, mCityAdapter,mCountyAdapter;
	private static int mState = STATE_NONE;
	private static int curScreen = SCREEN_PROVINCE;// 默认当前屏幕
	private static int mCurrentJsonId;

	public static final int AREA_REQUEST_CODE = 201;
	public static final String AREA_SELECTED_CODE="area_selected_code";
	private String selectedProvince;
	private String selectedCity;
	private String selectedCounty;

	private OperationResponseHandler mProvinceHandler = new OperationResponseHandler() {

		@Override
		public void onSuccess(int code, ByteArrayInputStream is, Object[] args) {
			try {
				AreaList list = JsonUtils.toBean(
						AreaList.class, is);
				if (mState == STATE_REFRESH)
					mProvinceAdapter.clear();
				List<Area> data = list.getAreaList();
//				省份设置为父节点
				for (int i=0;i<data.size();i++){
					data.get(i).setIsParent(true);
				}
				mProvinceAdapter.addData(data);
				mEmptyView.setErrorType(EmptyLayout.HIDE_LAYOUT);
				if (data.size() == 0 && mState == STATE_REFRESH) {
					mEmptyView.setErrorType(EmptyLayout.NODATA);
				} else {
					mProvinceAdapter
							.setState(ListBaseAdapter.STATE_LESS_ONE_PAGE);
				}
			} catch (Exception e) {
				e.printStackTrace();
				onFailure(code,e.getMessage(),args);
			}
		}

		@Override
		public void onFailure(int code, String errorMessage, Object[] args) {
			AppContext.showToast(errorMessage + code);
			mEmptyView.setErrorType(EmptyLayout.NETWORK_ERROR);
		}

		public void onFinish() {
			mState = STATE_NONE;
		}
	};

	private OperationResponseHandler mCityHandler = new OperationResponseHandler() {

		@Override
		public void onSuccess(int code, ByteArrayInputStream is, Object[] args) {
			try {
				AreaList list = JsonUtils.toBean(
						AreaList.class, is);
				if (mState == STATE_REFRESH)
					mCityAdapter.clear();
				List<Area> data = list.getAreaList();
//				城市设置为父节点
				for (int i=0;i<data.size();i++){
					data.get(i).setIsParent(true);
				}
				mCityAdapter.addData(data);
				mEmptyView.setErrorType(EmptyLayout.HIDE_LAYOUT);
				if (data.size() == 0 && mState == STATE_REFRESH) {
					mEmptyView.setErrorType(EmptyLayout.NODATA);
				} else {
					mCityAdapter.setState(ListBaseAdapter.STATE_LESS_ONE_PAGE);
				}
			} catch (Exception e) {
				e.printStackTrace();
				onFailure(code,e.getMessage(),args);
			}
		}

		@Override
		public void onFailure(int code, String errorMessage, Object[] args) {
			mEmptyView.setErrorType(EmptyLayout.NETWORK_ERROR);
		}

		public void onFinish() {
			mState = STATE_NONE;
		}
	};

	private OperationResponseHandler mCountyHandler = new OperationResponseHandler() {

		@Override
		public void onSuccess(int code, ByteArrayInputStream is, Object[] args){
			try {
				AreaList list = JsonUtils.toBean(AreaList.class,is);
//				针对有某些城市没有区 县等三级地区可以选，直接返回“省-市”即可
				if (list.getAreaList().size()==0){
					Intent intent = new Intent();
					String area = selectedProvince+"-"+selectedCity;
					intent.putExtra(AREA_SELECTED_CODE,area);
					getActivity().setResult(getActivity().RESULT_OK, intent);
					getActivity().finish();
					return;
				}
				if (mState == STATE_REFRESH)
					mCountyAdapter.clear();
				List<Area> data = list.getAreaList();
				mCountyAdapter.addData(data);
				mEmptyView.setErrorType(EmptyLayout.HIDE_LAYOUT);
				if (data.size() == 0 && mState == STATE_REFRESH) {
					mEmptyView.setErrorType(EmptyLayout.NODATA);
				} else {
					mCountyAdapter.setState(ListBaseAdapter.STATE_LESS_ONE_PAGE);
				}
			} catch (Exception e) {
				e.printStackTrace();
				onFailure(code,e.getMessage(),args);
			}
		}

		@Override
		public void onFailure(int code, String errorMessage, Object[] args) {
			mEmptyView.setErrorType(EmptyLayout.NETWORK_ERROR);
		}

		public void onFinish() {
			mState = STATE_NONE;
		}
	};

	private OnItemClickListener mProvinceOnItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Area type = (Area) mProvinceAdapter
					.getItem(position);
			if (type != null && type.getJsonId() > 0) {
				// 加载二级分类
				curScreen = SCREEN_CITY;
				mScrollLayout.scrollToScreen(curScreen);
				mCurrentJsonId = type.getJsonId();
				selectedProvince = type.getName();
				sendRequestCityData(mCityHandler);
			}
		}
	};

	private OnItemClickListener mCityOnItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Area type = (Area) mCityAdapter.getItem(position);
			if (type != null && type.getJsonId() > 0) {
				curScreen = SCREEN_COUNTY;
				mScrollLayout.scrollToScreen(curScreen);
				mCurrentJsonId = type.getJsonId();
				selectedCity = type.getName();
				sendRequestCountyData(mCountyHandler);
			}
		}
	};

	private OnItemClickListener mCountyOnItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
								long id) {
			Area type = (Area) mCountyAdapter.getItem(position);
			if (type != null && type.getJsonId() > 0) {
				selectedCounty = type.getName();
				Intent intent = new Intent();
				String area = selectedProvince+"-"+selectedCity+"-"+selectedCounty;
				intent.putExtra(AREA_SELECTED_CODE,area);
				getActivity().setResult(getActivity().RESULT_OK, intent);
				getActivity().finish();
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_area, container,
				false);
		initViews(view);
		return view;
	}

	private void initViews(View view) {
		mScrollLayout = (ScrollLayout) view.findViewById(R.id.scrolllayout);
		mScrollLayout.setIsScroll(false);

		mEmptyView = (EmptyLayout) view.findViewById(R.id.error_layout);
		mLvProvince = (ListView) view.findViewById(R.id.lv_province);
		mLvProvince.setOnItemClickListener(mProvinceOnItemClick);
		mLvCity = (ListView) view.findViewById(R.id.lv_city);
		mLvCity.setOnItemClickListener(mCityOnItemClick);
//		if (mProvinceAdapter == null) {
		mProvinceAdapter = new AreaListAdapter();
		sendRequestProvinceData(mProvinceHandler);
//		}
		mLvProvince.setAdapter(mProvinceAdapter);

		if (mCityAdapter == null) {
			mCityAdapter = new AreaListAdapter();
		}
		mLvCity.setAdapter(mCityAdapter);

		if (mCountyAdapter == null) {
			mCountyAdapter = new AreaListAdapter();
		}

		mLvCounty = (ListView) view.findViewById(R.id.lv_county);
		mLvCounty.setOnItemClickListener(mCountyOnItemClick);
		mLvCounty.setAdapter(mCountyAdapter);
	}

	@Override
	public boolean onBackPressed() {
		mEmptyView.setErrorType(EmptyLayout.HIDE_LAYOUT);
		switch (curScreen) {
		case SCREEN_COUNTY:
			curScreen = SCREEN_CITY;
			mScrollLayout.scrollToScreen(SCREEN_CITY);
			return true;
		case SCREEN_CITY:
			curScreen = SCREEN_PROVINCE;
			mScrollLayout.scrollToScreen(SCREEN_PROVINCE);
			return true;
		case SCREEN_PROVINCE:
			return false;
		}
		return super.onBackPressed();
	}

	private void sendRequestProvinceData(AsyncHttpResponseHandler handler) {
		mState = STATE_REFRESH;
		mEmptyView.setErrorType(EmptyLayout.NETWORK_LOADING);
		EasyFarmServerApi.getProvinceList(handler);
//		start-模拟数据
//		if (handler==mProvinceHandler){
//			Area a1 = new Area();
//			a1.setIsParent(true);a1.setJsonId(2012);a1.setName("广东");
//			Area a2 = new Area();
//			a2.setIsParent(true);a2.setJsonId(2012);a2.setName("河南");
//			Area a3 = new Area();
//			a3.setIsParent(true);a3.setJsonId(2012);a3.setName("山东");
//			AreaList list = new AreaList();
//			list.getAreaList().add(a1);list.getAreaList().add(a2);list.getAreaList().add(a3);
//			if (mState == STATE_REFRESH)
//				mProvinceAdapter.clear();
//			List<Area> data = list.getAreaList();
//			mProvinceAdapter.addData(data);
//			mEmptyView.setErrorType(EmptyLayout.HIDE_LAYOUT);
//			if (data.size() == 0 && mState == STATE_REFRESH) {
//				mEmptyView.setErrorType(EmptyLayout.NODATA);
//			} else {
//				mProvinceAdapter
//						.setState(ListBaseAdapter.STATE_LESS_ONE_PAGE);
//			}
//		}else if (handler==mCityHandler){
//			Area a1 = new Area();
//			a1.setIsParent(true);a1.setJsonId(2012);a1.setName("揭阳");
//			Area a2 = new Area();
//			a2.setIsParent(true);a2.setJsonId(2012);a2.setName("广州");
//			Area a3 = new Area();
//			a3.setIsParent(true);a3.setJsonId(2012);a3.setName("茂名");
//			AreaList list = new AreaList();
//			list.getAreaList().add(a1);list.getAreaList().add(a2);list.getAreaList().add(a3);
//			if (mState == STATE_REFRESH)
//				mCityAdapter.clear();
//			List<Area> data = list.getAreaList();
//			mCityAdapter.addData(data);
//			mEmptyView.setErrorType(EmptyLayout.HIDE_LAYOUT);
//			if (data.size() == 0 && mState == STATE_REFRESH) {
//				mEmptyView.setErrorType(EmptyLayout.NODATA);
//			} else {
//				mCityAdapter.setState(ListBaseAdapter.STATE_LESS_ONE_PAGE);
//			}
//		}else if (handler==mCountyHandler){
//			Area a1 = new Area();
//			a1.setIsParent(false);a1.setJsonId(2012);a1.setName("普宁");
//			Area a2 = new Area();
//			a2.setIsParent(false);a2.setJsonId(2012);a2.setName("蓉城");
//			Area a3 = new Area();
//			a3.setIsParent(false);a3.setJsonId(2012);a3.setName("惠来");
//			AreaList list = new AreaList();
//			list.getAreaList().add(a1);list.getAreaList().add(a2);list.getAreaList().add(a3);
//			if (mState == STATE_REFRESH)
//				mCountyAdapter.clear();
//			List<Area> data = list.getAreaList();
//			mCountyAdapter.addData(data);
//			mEmptyView.setErrorType(EmptyLayout.HIDE_LAYOUT);
//			if (data.size() == 0 && mState == STATE_REFRESH) {
//				mEmptyView.setErrorType(EmptyLayout.NODATA);
//			} else {
//				mCountyAdapter.setState(ListBaseAdapter.STATE_LESS_ONE_PAGE);
//			}
//		}
//		end-模拟数据
	}

	private void sendRequestCityData(AsyncHttpResponseHandler handler) {
		mState = STATE_REFRESH;
		mEmptyView.setErrorType(EmptyLayout.NETWORK_LOADING);
		EasyFarmServerApi.getCityList(mCurrentJsonId,handler);
	}

	private void sendRequestCountyData(AsyncHttpResponseHandler handler) {
		mState = STATE_REFRESH;
		mEmptyView.setErrorType(EmptyLayout.NETWORK_LOADING);
		EasyFarmServerApi.getCountyList(mCurrentJsonId, handler);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}
}
