package com.scau.easyfarm.fragment;

import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.CommonDetailFragment;
import com.scau.easyfarm.bean.ManualContent;
import com.scau.easyfarm.bean.ManualContentDetail;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.StringUtils;
import com.scau.easyfarm.util.UIHelper;

import java.io.InputStream;

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
    protected boolean isPdfFile(ManualContent detail) {
        if (StringUtils.isEmpty(detail.getFilePath())){
            return false;
        }
        return true;
    }

    @Override
    protected String getPdfFilePath(ManualContent detail) {
        return detail.getFilePath();
    }


    @Override
    protected int getFavoriteState() {
        return 0;
    }

    @Override
    protected void updateFavoriteChanged(int newFavoritedState) {

    }
}
