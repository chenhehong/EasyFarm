package com.scau.easyfarm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.scau.easyfarm.R;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.base.CommonDetailFragment;
import com.scau.easyfarm.bean.ManualContent;
import com.scau.easyfarm.bean.ManualContentDetail;
import com.scau.easyfarm.bean.News;
import com.scau.easyfarm.bean.NewsDetail;
import com.scau.easyfarm.bean.User;
import com.scau.easyfarm.bean.VillageService;
import com.scau.easyfarm.bean.VillageServiceOpinion;
import com.scau.easyfarm.ui.empty.EmptyLayout;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.StringUtils;
import com.scau.easyfarm.util.UIHelper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cz.msebera.android.httpclient.Header;

/**
 * Created by chenhehong on 2016/9/9.
 */
public class ManualContentDetailFragment extends CommonDetailFragment<ManualContent> {

    @Override
    protected String getCacheKey() {
        return "manual_" + mId;
    }

    @Override
    protected void sendRequestDataForNet() {
        EasyFarmServerApi.getManualDetail(mId, mDetailHeandler);
    }

    @Override
    protected ManualContent parseData(InputStream is) {
        return JsonUtils.toBean(ManualContentDetail.class, is).getManualContent();
    }

    //  HTML数据文本构造
    @Override
    protected String getWebViewBody(ManualContent detail) {
        StringBuffer body = new StringBuffer();
        body.append(UIHelper.WEB_STYLE).append(UIHelper.WEB_LOAD_IMAGES);
        body.append("<body ><div class='contentstyle' id='article_body'>");
        // 添加title
        body.append(String.format("<div class='title'>%s</div>", mDetail.getTitle()));
        // 添加作者和时间
//        String time = StringUtils.friendly_time(mDetail.getPubDate());
//        String author = String.format("<a class='author' href=''>%s</a>", "");
//        body.append(String.format("<div class='authortime'>%s&nbsp;&nbsp;&nbsp;&nbsp;%s</div>", author, time));
        // 添加图片点击放大支持
        body.append(UIHelper.setHtmlCotentSupportImagePreview(mDetail.getContent()));

        // 封尾
        body.append("</div></body>");
        return  body.toString();
    }

    @Override
    protected int getFavoriteState() {
        return 0;
    }

    @Override
    protected void updateFavoriteChanged(int newFavoritedState) {

    }
}
