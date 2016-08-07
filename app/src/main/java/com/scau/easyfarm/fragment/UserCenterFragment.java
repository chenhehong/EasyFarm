package com.scau.easyfarm.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.scau.easyfarm.R;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.ui.empty.EmptyLayout;

import java.io.ByteArrayInputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年10月29日 下午2:33:18
 * 
 */

public class UserCenterFragment extends BaseFragment implements
        OnItemClickListener, OnScrollListener {


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
