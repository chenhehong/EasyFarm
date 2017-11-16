package com.scau.easyfarm.fragment;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;

import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.CommonDetailFragment;
import com.scau.easyfarm.bean.News;
import com.scau.easyfarm.bean.NewsDetail;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.StringUtils;
import com.scau.easyfarm.util.UIHelper;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 火蚁 on 15/5/25.
 * 设置独特的CacheKey、MobileAPI调用、数据解析
 */
public class NewsDetailFragment extends CommonDetailFragment<News> {

    @Override
    protected String getCacheKey() {
        return "news_" + mId;
    }

    @Override
    protected void sendRequestDataForNet() {
        EasyFarmServerApi.getNewsDetail(mId, mDetailHeandler);
    }

    @Override
    protected News parseData(InputStream is) {
        return JsonUtils.toBean(NewsDetail.class, is).getNews();
    }

//  HTML数据文本构造
    @Override
    protected String getWebViewBody(News detail) {
        StringBuffer body = new StringBuffer();
        body.append(UIHelper.WEB_STYLE).append(UIHelper.WEB_LOAD_IMAGES);
        body.append("<body ><div class='contentstyle' id='article_body'>");
        // 添加title
        body.append(String.format("<div class='title'>%s</div>", mDetail.getTitle()));
        // 添加作者和时间
        String time = StringUtils.friendly_time(mDetail.getPubDate());
        String author = String.format("<a class='author' href=''>%s</a>", mDetail.getAuthor());
        body.append(String.format("<div class='authortime'>%s&nbsp;&nbsp;&nbsp;&nbsp;%s</div>", author, time));

//      正则表达式替换img标签里面的样式，使之能够在手机上面自适应屏幕
        Pattern p = Pattern.compile("sudyfile-attr.*/>");
        Matcher m = p.matcher(mDetail.getContent());
        String content = m.replaceAll("/>");

        // 添加图片点击放大支持
        body.append(UIHelper.setHtmlCotentSupportImagePreview(content));

        // 封尾
        body.append("</div></body>");
        return  body.toString();
    }

    @Override
    protected boolean isPdfFile(News detail) {
        return false;
    }

    @Override
    protected String getPdfFilePath(News detail) {
        return null;
    }

    @Override
    protected int getFavoriteState() {
        return 0;
    }

    @Override
    protected void updateFavoriteChanged(int newFavoritedState) {

    }
}
