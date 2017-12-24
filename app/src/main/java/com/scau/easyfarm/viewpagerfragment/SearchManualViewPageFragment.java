package com.scau.easyfarm.viewpagerfragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.scau.easyfarm.R;
import com.scau.easyfarm.adapter.ViewPageFragmentAdapter;
import com.scau.easyfarm.base.BaseListFragment;
import com.scau.easyfarm.base.BaseViewPagerFragment;
import com.scau.easyfarm.bean.ManualList;
import com.scau.easyfarm.fragment.SearchManulFragment;
import com.scau.easyfarm.util.TDevice;


public class SearchManualViewPageFragment extends BaseViewPagerFragment {
	
	
	private SearchView mSearchView;
	
	public static SearchManualViewPageFragment newInstance(){
		return new SearchManualViewPageFragment();
	}
	
	@Override
	protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
		String[] title = getResources().getStringArray(R.array.search_manual);
		adapter.addTab(title[0], "search_1", SearchManulFragment.class, getBundle(ManualList.CATALOG_VARIETY));
		adapter.addTab(title[1], "search_2", SearchManulFragment.class, getBundle(ManualList.CATALOG_INDUSTRY));
		adapter.addTab(title[2], "search_3", SearchManulFragment.class, getBundle(ManualList.CATALOG_TECHNOLOGY));
		adapter.addTab(title[3], "search_4", SearchManulFragment.class, getBundle(ManualList.CATALOG_ACHIEVEMENT));
	}
	
	private Bundle getBundle(String catalog) {
		Bundle bundle = new Bundle();
		bundle.putString(BaseListFragment.BUNDLE_KEY_CATALOG, catalog);
		return bundle;
	}
	

	@Override
	protected void setScreenPageLimit() {
		mViewPager.setOffscreenPageLimit(3);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.search_menu, menu);
		MenuItem search=menu.findItem(R.id.search_content);
		mSearchView=(SearchView) search.getActionView();
		mSearchView.setIconifiedByDefault(false);
		setSearch();
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	private void setSearch() {
		mSearchView.setQueryHint("搜索");
		TextView textView = (TextView) mSearchView.findViewById(R.id.search_src_text);
		textView.setTextColor(Color.WHITE);
		
		mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String arg0) {
				TDevice.hideSoftKeyboard(mSearchView);
				search(arg0);
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String arg0) {
				return false;
			}
		});
		mSearchView.requestFocus();
	}
	
	private void search(String content) {
		int index = mViewPager.getChildCount();
		for (int i = 0; i < index; i++) {
			SearchManulFragment fragment = (SearchManulFragment) getChildFragmentManager().getFragments().get(i);
			if (fragment != null) {
				fragment.search(content);
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

}
