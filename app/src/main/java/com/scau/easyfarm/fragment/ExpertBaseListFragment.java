package com.scau.easyfarm.fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.scau.easyfarm.R;
import com.scau.easyfarm.adapter.ExpertBaseAdapter;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseListFragment;
import com.scau.easyfarm.bean.ExpertBase;
import com.scau.easyfarm.bean.ExpertBaseList;
import com.scau.easyfarm.bean.ManualCategory;
import com.scau.easyfarm.bean.SimpleBackPage;
import com.scau.easyfarm.ui.SimpleBackActivity;
import com.scau.easyfarm.ui.empty.EmptyLayout;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.UIHelper;

import java.io.InputStream;
import java.io.Serializable;

/**
 * Created by chenhehong on 2016/8/26.
 */
public class ExpertBaseListFragment extends BaseListFragment<ExpertBase>{

    private static final String CACHE_KEY_PREFIX = "expertbaselist_";
    public static final String BUNDLEKEY_MANUALCOTEGORY = "bundlekey_manualcotegory";

    private ManualCategory manualCategory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle args = getArguments();
        if (args != null) {
            manualCategory = (ManualCategory) args.getSerializable(BUNDLEKEY_MANUALCOTEGORY);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
//  重载设置子类的列表适配器
    protected ExpertBaseAdapter getListAdapter() {
        return new ExpertBaseAdapter();
    }

//  重载该方法，定义子类自己的cachekey
    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX;
    }

//  重载该方法，对服务器返回的数据进行解析
    @Override
    protected ExpertBaseList parseList(InputStream is) throws Exception {
        ExpertBaseList list = JsonUtils.toBean(ExpertBaseList.class, is);
        return list;
    }

//  用于从缓存中读出序列化数据
    @Override
    protected ExpertBaseList readList(Serializable seri) {
        return ((ExpertBaseList) seri);
    }


    @Override
    protected void sendRequestData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
//            如果需要做搜索功能，可以通过bundle传人参数，进行带参数的请求
        }
        EasyFarmServerApi.getExpertBaseList(manualCategory.getId(), mCurrentPage, mHandler);
    }

//  重载点击事件，自定义子类的点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        ExpertBase expertBase= mAdapter.getItem(position);
        if (expertBase != null) {
            UIHelper.showExpertBseDetail(this, expertBase.getId());
        }
    }


    @Override
    public void initView(View view) {
        super.initView(view);
        setHasOptionsMenu(true);
//      设置状态栏点击事件
        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                    requestData(true);
            }
        });
        if (getActivity() instanceof SimpleBackActivity){
            ((SimpleBackActivity)getActivity()).setActionBarTitle(manualCategory.getCategoryName());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_icon_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.search_icon:
                UIHelper.showSimpleBack(this, SimpleBackPage.SEARCH_EXPERT);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
