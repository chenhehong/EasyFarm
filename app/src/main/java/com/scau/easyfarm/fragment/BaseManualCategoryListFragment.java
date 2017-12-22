package com.scau.easyfarm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.scau.easyfarm.R;
import com.scau.easyfarm.adapter.ManualCategoryListAdapter;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.base.ListBaseAdapter;
import com.scau.easyfarm.bean.Entity;
import com.scau.easyfarm.bean.ManualCategory;
import com.scau.easyfarm.bean.ManualCategoryList;
import com.scau.easyfarm.ui.SimpleBackActivity;
import com.scau.easyfarm.ui.empty.EmptyLayout;
import com.scau.easyfarm.util.JsonUtils;

import java.io.ByteArrayInputStream;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by chenhehong on 2016/9/9.
 */
public abstract class BaseManualCategoryListFragment extends BaseFragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    public static final String BUNDLEKEY_PARENT_MANUALCATEGORY = "bundlekey_parent_manualcategory";
    public static final int MANUAL_COTEGORY_LIST_REQUEST_CODE = 100;

    public ManualCategory parentManualCategory;

    private ManualCategoryListAdapter manualCategoryListAdapter;
    private static int mState = STATE_NONE;
    //  后面以currenPage==0判断为第一次加载数据列表
    private int currenPage;
    private int pageSize=20;

    @InjectView(R.id.error_layout)
    EmptyLayout mEmptyView;
    @InjectView(R.id.lv_manual_catalog)
    ListView manualCategoryListView;


    private OperationResponseHandler mHandler = new OperationResponseHandler() {

        @Override
        public void onSuccess(int code, ByteArrayInputStream is, Object[] args){
            try {
                ManualCategoryList list = JsonUtils.toBean(ManualCategoryList.class,
                        is);
                executeOnLoadDataSuccess(list.getManualCategoryList());
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manual_cotegory_list,container,false);
        initView(view);
        return view;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        ButterKnife.inject(this, view);
        manualCategoryListView.setOnItemClickListener(manualCategoryOnItemClick);
        if (manualCategoryListAdapter==null){
            manualCategoryListAdapter = new ManualCategoryListAdapter();
        }
        manualCategoryListView.setAdapter(manualCategoryListAdapter);
        mEmptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestManualCatalogData();
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initData();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void initData() {
        super.initData();
        Bundle bundle = getArguments();
        parentManualCategory = (ManualCategory) bundle.getSerializable(BUNDLEKEY_PARENT_MANUALCATEGORY);
        if (getActivity() instanceof SimpleBackActivity){
            ((SimpleBackActivity)getActivity()).setActionBarTitle(parentManualCategory.getCategoryName());
        }
        sendRequestManualCatalogData();
    }

    private void sendRequestManualCatalogData() {
        mState = STATE_REFRESH;
        mEmptyView.setErrorType(EmptyLayout.NETWORK_LOADING);
        if (parentManualCategory.getId() ==0){
            EasyFarmServerApi.getManualCatalogListByCode(parentManualCategory.getCategoryCode(), currenPage, pageSize, mHandler);
        }else {
            EasyFarmServerApi.getManualCatalogListByParentId(parentManualCategory.getId(), currenPage, pageSize, mHandler);
        }
    }

    private AdapterView.OnItemClickListener manualCategoryOnItemClick = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            ManualCategory selectManualCategory = (ManualCategory)manualCategoryListAdapter.getItem(position);
            if (selectManualCategory==null) return;
            handleSelectManualCategory(selectManualCategory);
        }
    };

    abstract void handleSelectManualCategory(ManualCategory selectManualCategory);

    private void executeOnLoadDataSuccess(List<ManualCategory> data) {
        if (data == null) {
            return;
        }
//      添加一个“全部”的选项
        ManualCategory m = new ManualCategory();
        m.setId(parentManualCategory.getId());
        m.setCategoryName("全部");
        m.setCategoryCode(parentManualCategory.getCategoryCode());
        data.add(0,m);
        mEmptyView.setErrorType(EmptyLayout.HIDE_LAYOUT);

        for (int i = 0; i < data.size(); i++) {
            if (compareTo(manualCategoryListAdapter.getData(), data.get(i))) {
                data.remove(i);
            }
        }
        int adapterState = ListBaseAdapter.STATE_EMPTY_ITEM;
        if (manualCategoryListAdapter.getCount() == 0 && mState == STATE_NONE) {
            mEmptyView.setErrorType(EmptyLayout.NODATA);
        } else if (data.size() == 0 || (data.size() < 20 && currenPage == 0)) {
            adapterState = ListBaseAdapter.STATE_NO_MORE;
        } else {
            adapterState = ListBaseAdapter.STATE_LOAD_MORE;
        }
        manualCategoryListAdapter.setState(adapterState);
        manualCategoryListAdapter.addData(data);
    }

    private boolean compareTo(List<? extends Entity> data, ManualCategory enity) {
        int s = data.size();
        if (enity != null) {
            for (int i = 0; i < s; i++) {
                if (enity.getId()==((ManualCategory)data.get(i)).getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
//  列表滚动事件
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // 数据已经全部加载，或数据为空时，或正在加载，不处理滚动事件
        if (mState == STATE_NOMORE || mState == STATE_LOADMORE
                || mState == STATE_REFRESH) {
            return;
        }
        if (manualCategoryListAdapter != null
                && manualCategoryListAdapter.getDataSize() > 0
                && manualCategoryListView.getLastVisiblePosition() == (manualCategoryListView
                .getCount() - 1)) {
            if (mState == STATE_NONE
                    && manualCategoryListAdapter.getState() == ListBaseAdapter.STATE_LOAD_MORE) {
                mState = STATE_LOADMORE;
                currenPage++;
                sendRequestManualCatalogData();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
