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
import com.scau.easyfarm.fragment.VillageServiceProofListFragment;
import com.scau.easyfarm.util.UIHelper;

/**
 * Created by ChenHehong on 2016/6/11.
 */
public class VillageServiceProofViewPagerFragment extends BaseViewPagerFragment{


    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
//      显示右上角菜单栏
        setHasOptionsMenu(true);

        String[] title = getResources().getStringArray(R.array.village_service_proof_viewpage_arrays);
//      传人频道号为参数
        adapter.addTab(title[0], "underway_village_service", VillageServiceProofListFragment.class,
                getBundle(VillageServiceProofListFragment.UNDERWAY_SERVICE));
        adapter.addTab(title[1], "completed_village_service", VillageServiceProofListFragment.class,
                getBundle(VillageServiceProofListFragment.COMPLETED_SERVICE));
    }

    private Bundle getBundle(int catalog) {
        Bundle bundle = new Bundle();
        bundle.putInt(BaseListFragment.BUNDLE_KEY_CATALOG, catalog);
        return bundle;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.takephoto_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.public_menu_takephoto:
                UIHelper.showSimpleBack(getActivity(), SimpleBackPage.VILLAGE_SERVICE_PROOF_RESOURCE_ADD);
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
