package com.scau.easyfarm.viewpagerfragment;

import android.os.Bundle;
import android.view.View;

import com.scau.easyfarm.R;
import com.scau.easyfarm.adapter.ViewPageFragmentAdapter;
import com.scau.easyfarm.base.BaseListFragment;
import com.scau.easyfarm.base.BaseViewPagerFragment;
import com.scau.easyfarm.fragment.VerifyVillageServiceListFragment;

/**
 * Created by ChenHehong on 2016/6/11.
 */
public class VillageServiceAllViewPagerFragment extends BaseViewPagerFragment{


    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
//      显示右上角菜单栏
        setHasOptionsMenu(true);

        String[] title = getResources().getStringArray(
                R.array.village_service_verify_viewpage_arrays);
//      传人频道号为参数
        adapter.addTab(title[0], "all_village_service", VerifyVillageServiceListFragment.class,
                getBundle(VerifyVillageServiceListFragment.WAITING_VILAGE_SERVICE));
        adapter.addTab(title[1], "pass_village_service", VerifyVillageServiceListFragment.class,
                getBundle(VerifyVillageServiceListFragment.ALL_VILLAGE_SERVICE));
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
