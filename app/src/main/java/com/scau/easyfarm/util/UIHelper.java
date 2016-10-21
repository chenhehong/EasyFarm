package com.scau.easyfarm.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ZoomButtonsController;

import com.scau.easyfarm.AppConfig;
import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.base.BaseListFragment;
import com.scau.easyfarm.bean.ExpertBaseDetail;
import com.scau.easyfarm.bean.SimpleBackPage;
import com.scau.easyfarm.bean.Tweet;
import com.scau.easyfarm.bean.VillageService;
import com.scau.easyfarm.fragment.BaseManualCategoryListFragment;
import com.scau.easyfarm.fragment.ExpertBaseDetailFragment;
import com.scau.easyfarm.fragment.ExpertBaseListFragment;
import com.scau.easyfarm.fragment.ManualListFragment;
import com.scau.easyfarm.fragment.ServerSummaryFragment;
import com.scau.easyfarm.fragment.ServiceStatisticDetailFragment;
import com.scau.easyfarm.fragment.TweetChooseManualCategoryFragment;
import com.scau.easyfarm.fragment.TweetExpertChooseFragment;
import com.scau.easyfarm.fragment.VillageServiceDetailFragment;
import com.scau.easyfarm.fragment.VillageServiceProofResourceFragment;
import com.scau.easyfarm.interf.ICallbackResult;
import com.scau.easyfarm.interf.OnWebViewImageListener;
import com.scau.easyfarm.service.DownloadService;
import com.scau.easyfarm.service.NoticeService;
import com.scau.easyfarm.ui.DetailActivity;
import com.scau.easyfarm.ui.ImagePreviewActivity;
import com.scau.easyfarm.ui.LoginActivity;
import com.scau.easyfarm.ui.SimpleBackActivity;

/**
 * 界面帮助类
 */
public class UIHelper {


    /** 全局web样式 */
    // 链接样式文件，代码块高亮的处理
    public final static String linkCss = "<script type=\"text/javascript\" src=\"file:///android_asset/shCore.js\"></script>"
            + "<script type=\"text/javascript\" src=\"file:///android_asset/brush.js\"></script>"
            + "<script type=\"text/javascript\" src=\"file:///android_asset/client.js\"></script>"
            + "<script type=\"text/javascript\" src=\"file:///android_asset/detail_page.js\"></script>"
            + "<script type=\"text/javascript\">SyntaxHighlighter.all();</script>"
            + "<script type=\"text/javascript\">function showImagePreview(var url){window.location.url= url;}</script>"
            + "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/shThemeDefault.css\">"
            + "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/shCore.css\">"
            + "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/css/common.css\">";
    public final static String WEB_STYLE = linkCss;

    public static final String WEB_LOAD_IMAGES = "<script type=\"text/javascript\"> var allImgUrls = getAllImgSrc(document.body.innerHTML);</script>";

    private static final String SHOWIMAGE = "ima-api:action=showImage&data=";


    /**
     * 显示用户中心页面
     *
     * @param context
     * @param hisuid
     * @param hisuid
     * @param hisname
     */
    public static void showUserCenter(Context context, int hisuid,
                                      String hisname) {
        if (hisuid == 0 && hisname.equalsIgnoreCase("匿名")) {
            AppContext.showToast("提醒你，该用户为非会员");
            return;
        }
        Bundle args = new Bundle();
        args.putInt("his_id", hisuid);
        args.putString("his_name", hisname);
        showSimpleBack(context, SimpleBackPage.USER_CENTER, args);
    }

    /**
     * 显示登录界面
     */
    public static void showLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

//  把class拼装进SimpleBackActivity的fragment中，带参数
    public static void showSimpleBack(Context context, SimpleBackPage page,
                                      Bundle args) {
        Intent intent = new Intent(context, SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_ARGS, args);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        context.startActivity(intent);
    }

    public static void showSimpleBack(Fragment fragment, SimpleBackPage page) {
        Intent intent = new Intent(fragment.getActivity(), SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        fragment.startActivity(intent);
    }

    public static void showSimpleBack(Fragment fragment, SimpleBackPage page,
                                      Bundle args) {
        Intent intent = new Intent(fragment.getActivity(), SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_ARGS, args);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        fragment.startActivity(intent);
    }

    public static void showSimpleBack(Context context, SimpleBackPage page) {
        Intent intent = new Intent(context, SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        context.startActivity(intent);
    }

    public static void showSimpleBackForResult(Fragment fragment,
                                               int requestCode, SimpleBackPage page, Bundle args) {
        Intent intent = new Intent(fragment.getActivity(),
                SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_ARGS, args);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void showSimpleBackForResult(Fragment fragment,
                                               int requestCode, SimpleBackPage page) {
        Intent intent = new Intent(fragment.getActivity(),
                SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void showSimpleBackForResult(Activity context,
                                               int requestCode, SimpleBackPage page, Bundle args) {
        Intent intent = new Intent(context, SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_ARGS, args);
        context.startActivityForResult(intent, requestCode);
    }

    public static void showSimpleBackForResult(Activity context,
                                               int requestCode, SimpleBackPage page) {
        Intent intent = new Intent(context, SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 发送通知广播
     *
     * @param context
     */
    public static void sendBroadcastForNotice(Context context) {
        Intent intent = new Intent(NoticeService.INTENT_ACTION_BROADCAST);
        context.sendBroadcast(intent);
    }

    /**
     * 显示用户的消息中心
     *
     * @param context
     */
    public static void showMyMes(Context context) {
        showSimpleBack(context, SimpleBackPage.MY_MES);
    }

    /**
     * 显示设置界面
     *
     * @param context
     */
    public static void showSetting(Context context) {
        showSimpleBack(context, SimpleBackPage.SETTING);
    }

    /**
     * 显示通知设置界面
     *
     * @param context
     */
    public static void showSettingNotification(Context context) {
        showSimpleBack(context, SimpleBackPage.SETTING_NOTIFICATION);
    }

    /**
     * 显示关于界面
     *
     * @param context
     */
    public static void showAboutApp(Context context) {
        showSimpleBack(context, SimpleBackPage.ABOUT_APP);
    }

    /**
     * 显示修改密码界面
     *
     * @param context
     */
    public static void showChangePassword(Context context) {
        showSimpleBack(context, SimpleBackPage.CHANGE_PASSWORD);
    }


    /**
     * 清除app缓存
     *
     * @param activity
     */
    public static void clearAppCache(Activity activity) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    AppContext.showToastShort("缓存清除成功");
                } else {
                    AppContext.showToastShort("缓存清除失败");
                }
            }
        };
        new Thread() {
            @Override
            public void run() {
                Message msg = new Message();
                try {
                    AppContext.getInstance().clearAppCache();
                    msg.what = 1;
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = -1;
                }
                handler.sendMessage(msg);
            }
        }.start();
    }

//  打开下载新版本apk服务
    public static void openDownLoadService(Context context, String downurl,
                                           String tilte) {
        final ICallbackResult callback = new ICallbackResult() {

            @Override
            public void OnBackResult(Object s) {}
        };
        ServiceConnection conn = new ServiceConnection() {

            @Override
            public void onServiceDisconnected(ComponentName name) {}

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                DownloadService.DownloadBinder binder = (DownloadService.DownloadBinder) service;
                binder.addCallback(callback);
                binder.start();

            }
        };
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(DownloadService.BUNDLE_KEY_DOWNLOAD_URL, downurl);
        intent.putExtra(DownloadService.BUNDLE_KEY_TITLE, tilte);
        context.startService(intent);
        context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

//  显示添加问答界面
    public static void showTweetPub(Context context) {
        showSimpleBack(context, SimpleBackPage.TWEET_PUB);
    }

    /**
     * 显示问题详情
     *
     * @param context context
     * @param tweetid 动弹的id
     */
    public static void showTweetDetail(Context context, Tweet tweet, int tweetid) {
        Intent intent = new Intent(context, DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("tweet_id", tweetid);
        bundle.putInt(DetailActivity.BUNDLE_KEY_DISPLAY_TYPE,
                DetailActivity.DISPLAY_TWEET);
        if (tweet != null) {
            bundle.putParcelable("tweet", tweet);
        }
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void showTweetTypeChoose(Fragment fragment, int parentId,String type,int requestCode) {
        Bundle args = new Bundle();
        args.putInt(BaseManualCategoryListFragment.BUNDLEKEY_PARENTID, parentId);
        args.putString(BaseManualCategoryListFragment.BUNDLEKEY_TYPE, type);
        showSimpleBackForResult(fragment, requestCode, SimpleBackPage.TWEET_CHOOSE_TYPE, args);
    }

    public static void showTweetExpertChoose(Fragment fragment, int typeId, int requestCode) {
        Bundle bundle = new Bundle();
        bundle.putInt(TweetExpertChooseFragment.TWEET_EXPERT_MANUAL_TYPE, typeId);
        showSimpleBackForResult(fragment, requestCode, SimpleBackPage.TWEET_CHOOSE_EXPERT, bundle);
    }

    public static void findUser(Fragment fragment,int requestCode){
        showSimpleBackForResult(fragment,requestCode,SimpleBackPage.FIND_USER);
    }

    public static void chooseVillageServiceReason(Fragment fragment,int requestCode){
        showSimpleBackForResult(fragment, requestCode, SimpleBackPage.CHOOSE_VILLAGE_SERVICE_REASON);
    }

    //  显示添加问答界面
    public static void showVillageServiceApply(Context context) {
        showSimpleBack(context, SimpleBackPage.VILLAGE_SERVICE_APPLY);
    }

    public static void showVillageServiceDetail(Context context, int villageServiceId) {
        Bundle bundle = new Bundle();
        bundle.putInt(VillageServiceDetailFragment.VILLAGE_SERVICE_ID_CODE, villageServiceId);
        showSimpleBack(context, SimpleBackPage.VILLAGE_SERVICE_DETAIL, bundle);
    }

    public static void showVillageServiceStatisticDetail(Context context, int villageServiceId) {
        Bundle bundle = new Bundle();
        bundle.putInt(ServiceStatisticDetailFragment.VILLAGE_SERVICE_ID_CODE, villageServiceId);
        showSimpleBack(context, SimpleBackPage.VILLAGE_SERVICE_STATISTIC_DETAIL, bundle);
    }

    public static void showVillageServiceVerify(Fragment fragment,int requestCode,int villageServiceId) {
        Bundle bundle = new Bundle();
        bundle.putInt(VillageServiceDetailFragment.VILLAGE_SERVICE_ID_CODE,villageServiceId);
        showSimpleBackForResult(fragment, requestCode, SimpleBackPage.VILLAGE_SERVICE_VERIFY, bundle);
    }

    public static void showAreaChoose(Fragment fragment, int requestCode) {
        showSimpleBackForResult(fragment, requestCode, SimpleBackPage.CHOOSE_AREA);
    }

    public static void showManualCategory(Fragment fragment,int parentId,String type){
        Bundle budle = new Bundle();
        budle.putInt(BaseManualCategoryListFragment.BUNDLEKEY_PARENTID,parentId);
        budle.putString(BaseManualCategoryListFragment.BUNDLEKEY_TYPE,type);
        showSimpleBack(fragment, SimpleBackPage.CHOOSE_MANUAL_CATEGORY, budle);
    }

    public static void showManualList(Fragment fragment,String categoryCode){
        Bundle bundle = new Bundle();
        bundle.putString(ManualListFragment.MANUALCATEGORYCODE, categoryCode);
        showSimpleBack(fragment, SimpleBackPage.MANUAL_LIST, bundle);
    }

    public static void showManualContentDetail(Context context, int ManualContentId) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("id", ManualContentId);
        intent.putExtra(DetailActivity.BUNDLE_KEY_DISPLAY_TYPE,
                DetailActivity.DISPLAY_MAANUAL);
        context.startActivity(intent);
    }

    public static void showVillageServiceProofChoose(Fragment fragment,int requestCode) {
        showSimpleBackForResult(fragment, requestCode, SimpleBackPage.CHOOSE_PROOF_VILLAGE_SERVICE);
    }

    public static void showVillageServicProofList(Context context) {
        showSimpleBack(context, SimpleBackPage.VILLAGE_SERVICE_PROOF);
    }

    public static void showVillageServiceProofResource(Fragment fragment,VillageService villageService,int resourceCatalog) {
        Bundle args = new Bundle();
        args.putSerializable(VillageServiceProofResourceFragment.BUNDLEKEY_VILLAGESERVICE, villageService);
        args.putInt(BaseListFragment.BUNDLE_KEY_CATALOG,resourceCatalog);
        showSimpleBack(fragment,SimpleBackPage.VILLAGE_SERVICE_PROOF_RESOURCE,args);
    }

    public static void showServiceStatisticList(Fragment fragment) {
        showSimpleBack(fragment,SimpleBackPage.SERVICE_STATISTIC);
    }

    /**
     * 显示新闻详情
     *
     * @param context
     * @param newsId
     */
    public static void showNewsDetail(Context context, int newsId) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("id", newsId);
        intent.putExtra(DetailActivity.BUNDLE_KEY_DISPLAY_TYPE,
                DetailActivity.DISPLAY_NEWS);
        context.startActivity(intent);
    }

    @SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
    public static void initWebView(WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setDefaultFontSize(15);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        int sysVersion = Build.VERSION.SDK_INT;
        if (sysVersion >= 11) {
            settings.setDisplayZoomControls(false);
        } else {
            ZoomButtonsController zbc = new ZoomButtonsController(webView);
            zbc.getZoomControls().setVisibility(View.GONE);
        }
        webView.setWebViewClient(UIHelper.getWebViewClient());
    }

    /**
     * 添加网页的点击图片展示支持
     */
    @SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
    @JavascriptInterface
    public static void addWebImageShow(final Context cxt, WebView wv) {
        wv.getSettings().setJavaScriptEnabled(true);
        wv.addJavascriptInterface(new OnWebViewImageListener() {
            @Override
            @JavascriptInterface
            public void showImagePreview(String bigImageUrl) {
                if (bigImageUrl != null && !StringUtils.isEmpty(bigImageUrl)) {
                    UIHelper.showImagePreview(cxt, new String[]{bigImageUrl});
                }
            }
        }, "mWebViewImageListener");
    }

    /**
     * 获取webviewClient对象
     *
     * @return
     */
    public static WebViewClient getWebViewClient() {

        return new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                showUrlRedirect(view.getContext(), url);
                return true;
            }
        };
    }

    /**
     * url跳转
     *
     * @param context
     * @param url
     */
    public static void showUrlRedirect(Context context, String url) {
        if (url == null)
            return;
        openBrowser(context, url);
    }

    /**
     * 打开内置浏览器
     *
     * @param context
     * @param url
     */
    public static void openBrowser(Context context, String url) {

        if (StringUtils.isImgUrl(url)) {
            ImagePreviewActivity.showImagePrivew(context, 0,
                    new String[]{url});
            return;
        }
        try {
            // 启用外部浏览器
             Uri uri = Uri.parse(url);
             Intent it = new Intent(Intent.ACTION_VIEW, uri);
             context.startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
            AppContext.showToastShort("无法浏览此网页");
        }
    }

    public static String setHtmlCotentSupportImagePreview(String body) {
        // 读取用户设置：是否加载文章图片--默认有wifi下始终加载图片
        if (AppContext.get(AppConfig.KEY_LOAD_IMAGE, true)
                || TDevice.isWifiOpen()) {
            // 过滤掉 img标签的width,height属性
            body = body.replaceAll("(<img[^>]*?)\\s+width\\s*=\\s*\\S+", "$1");
            body = body.replaceAll("(<img[^>]*?)\\s+height\\s*=\\s*\\S+", "$1");
            // 添加点击图片放大支持
            // 添加点击图片放大支持
            body = body.replaceAll("(<img[^>]+src=\")(\\S+)\"",
                    "$1$2\" onClick=\"showImagePreview('$2')\"");
        } else {
            // 过滤掉 img标签
            body = body.replaceAll("<\\s*img\\s+([^>]*)\\s*>", "");
        }
        return body;
    }

    @JavascriptInterface
    public static void showImagePreview(Context context, String[] imageUrls) {
        ImagePreviewActivity.showImagePrivew(context, 0, imageUrls);
    }

    public static void chooseProofResourceDescription(Fragment fragment,int requestCode){
        showSimpleBackForResult(fragment, requestCode, SimpleBackPage.CHOOSE_PROOF_RESOURCE_DESCRIPTITON);
    }

    public static void showServerSummary(Fragment fragment, VillageService service,int position, int requestCode) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ServerSummaryFragment.BUNDLEKEY_SERVICE, service);
        bundle.putInt(ServerSummaryFragment.BUNDLEKEY_POSITION,position);
        showSimpleBackForResult(fragment, requestCode, SimpleBackPage.SERVER_SUMMARY, bundle);
    }

    public static void showExpertBaseManualCategory(Fragment fragment,int parentId,String type){
        Bundle budle = new Bundle();
        budle.putInt(BaseManualCategoryListFragment.BUNDLEKEY_PARENTID, parentId);
        budle.putString(BaseManualCategoryListFragment.BUNDLEKEY_TYPE, type);
        showSimpleBack(fragment, SimpleBackPage.EXPERTBASE_CHOOSE_MANUAL_CATEGORY, budle);
    }

    public static void showExpertBaseList(Fragment fragment,int categoryCodeId){
        Bundle bundle = new Bundle();
        bundle.putInt(ExpertBaseListFragment.BUNDLEKEY_MANUALCOTEGORY_ID, categoryCodeId);
        showSimpleBack(fragment, SimpleBackPage.EXPERTBASE_LIST,bundle);
    }

    public static void showExpertBseDetail(Fragment fragment, int expertBaseId) {
        Bundle bundle = new Bundle();
        bundle.putInt(ExpertBaseDetailFragment.BUNDLEKEY_EXPERTBASE_ID,expertBaseId);
        showSimpleBack(fragment,SimpleBackPage.EXPERTBASE_DETAIL,bundle);
    }

}














