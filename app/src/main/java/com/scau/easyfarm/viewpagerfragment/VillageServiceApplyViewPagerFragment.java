package com.scau.easyfarm.viewpagerfragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.scau.easyfarm.R;
import com.scau.easyfarm.adapter.ViewPageFragmentAdapter;
import com.scau.easyfarm.base.BaseListFragment;
import com.scau.easyfarm.base.BaseViewPagerFragment;
import com.scau.easyfarm.bean.SimpleBackPage;
import com.scau.easyfarm.bean.TweetsList;
import com.scau.easyfarm.bean.VillageServiceList;
import com.scau.easyfarm.fragment.TweetsFragment;
import com.scau.easyfarm.fragment.VillageServiceFragment;
import com.scau.easyfarm.util.UIHelper;

/**
 * Created by ChenHehong on 2016/6/11.
 */
public class VillageServiceApplyViewPagerFragment extends BaseViewPagerFragment{


    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
//      显示右上角菜单栏
        setHasOptionsMenu(true);

        String[] title = getResources().getStringArray(
                R.array.village_service_aply_viewpage_arrays);
//      传人频道号为参数
        adapter.addTab(title[0], "all_village_service", VillageServiceFragment.class,
                getBundle(VillageServiceList.ALL_VILLAGE_SERVICE));
        adapter.addTab(title[1], "pass_village_service", VillageServiceFragment.class,
                getBundle(VillageServiceList.PASS_VILAGE_SERVICE));
    }

    private Bundle getBundle(int catalog) {
        Bundle bundle = new Bundle();
        bundle.putInt(BaseListFragment.BUNDLE_KEY_CATALOG, catalog);
        return bundle;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.public_menu_add:
                UIHelper.showSimpleBack(getActivity(), SimpleBackPage.VILLAGE_SERVICE_ADD);
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {}

    @Override
    public void initView(View view) {}

    @Override
    public void initData() {}

}
