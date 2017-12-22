package com.scau.easyfarm.viewpagerfragment;

import android.os.Bundle;
import android.view.View;

import com.scau.easyfarm.R;
import com.scau.easyfarm.adapter.ViewPageFragmentAdapter;
import com.scau.easyfarm.base.BaseListFragment;
import com.scau.easyfarm.base.BaseViewPagerFragment;
import com.scau.easyfarm.bean.NewsList;
import com.scau.easyfarm.fragment.NewsListFragment;

/**
 * 资讯viewpager页面
 */
public class NewsViewPagerFragment extends BaseViewPagerFragment {

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
        String[] title = getResources().getStringArray(
                R.array.news_viewpage_arrays);
//      传人频道号为参数
        adapter.addTab(title[0], "news_hot", NewsListFragment.class,
                getBundle(NewsList.CATALOG_HOT));
        adapter.addTab(title[1], "news_announce", NewsListFragment.class,
                getBundle(NewsList.CATALOG_ANNOUNCE));
    }

    private Bundle getBundle(int catalog) {
        Bundle bundle = new Bundle();
        bundle.putInt(BaseListFragment.BUNDLE_KEY_CATALOG, catalog);
        return bundle;
    }

    @Override
    public void onClick(View v) {}

    @Override
    public void initView(View view) {}

    @Override
    public void initData() {}
}
