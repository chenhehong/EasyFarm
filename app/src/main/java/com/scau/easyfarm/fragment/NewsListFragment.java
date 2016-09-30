package com.scau.easyfarm.fragment;

import android.view.View;
import android.widget.AdapterView;

import com.scau.easyfarm.adapter.NewsAdapter;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseListFragment;
import com.scau.easyfarm.base.ListBaseAdapter;
import com.scau.easyfarm.bean.News;
import com.scau.easyfarm.bean.NewsList;
import com.scau.easyfarm.ui.empty.EmptyLayout;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.UIHelper;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

/**
 * Created by chenhehong on 2016/8/26.
 */
public class NewsListFragment extends BaseListFragment<News>{

    private static final String CACHE_KEY_PREFIX = "newsList_";

    @Override
    protected NewsAdapter getListAdapter() {
        return new NewsAdapter();
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mCatalog;
    }

    @Override
    protected NewsList parseList(InputStream is) throws Exception {
        NewsList list = null;
        try {
            //数据使用的是xml格式
            list = JsonUtils.toBean(NewsList.class, is);
        } catch (NullPointerException e) {
            list = new NewsList();
        }
        return list;
    }

    @Override
    protected NewsList readList(Serializable seri) {
        return ((NewsList) seri);
    }

    //覆盖获取数据的方法
    @Override
    protected void sendRequestData() {
        EasyFarmServerApi.getNewsList(mCatalog, mCurrentPage, mHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        News news = mAdapter.getItem(position);
        if (news != null) {
            int newsId = news.getId();
            UIHelper.showNewsDetail(getActivity(), newsId);
        }
    }
}
