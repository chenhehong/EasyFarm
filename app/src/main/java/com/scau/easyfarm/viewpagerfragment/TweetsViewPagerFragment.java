package com.scau.easyfarm.viewpagerfragment;

import android.os.Bundle;
import android.view.View;

import com.scau.easyfarm.R;
import com.scau.easyfarm.adapter.ViewPageFragmentAdapter;
import com.scau.easyfarm.base.BaseListFragment;
import com.scau.easyfarm.base.BaseViewPagerFragment;
import com.scau.easyfarm.bean.TweetsList;
import com.scau.easyfarm.fragment.TweetsFragment;

/**
 * Created by ChenHehong on 2016/6/11.
 */
public class TweetsViewPagerFragment extends BaseViewPagerFragment{


    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
        String[] title = getResources().getStringArray(
                R.array.tweets_viewpage_arrays);
//      传人频道号为参数
        adapter.addTab(title[0], "new_tweets", TweetsFragment.class,
                getBundle(TweetsList.CATALOG_LATEST));
        adapter.addTab(title[1], "hot_tweets", TweetsFragment.class,
                getBundle(TweetsList.CATALOG_HOT));
        adapter.addTab(title[2], "my_tweets", TweetsFragment.class,
                getBundle(TweetsList.CATALOG_ME));
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
