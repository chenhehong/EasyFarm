package com.scau.easyfarm.api.remote;


import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.api.ApiHttpClient;
import com.scau.easyfarm.bean.Tweet;

import java.io.File;
import java.io.FileNotFoundException;


public class EasyFarmServerApi {

    /**
     * 登陆
     *
     * @param username
     * @param password
     * @param handler
     */
    public static void login(String username, String password,
            AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("loginName", username);
        params.put("password", password);
        params.put("keep_login", 1);
        String loginurl = "front/mobile/expert/login";
        ApiHttpClient.post(loginurl, params, handler);
    }

    /**
     * 获取用户信息
     * @param uid
     * @param handler
     */
    public static void getMyInformation(int uid,String type,
                                        AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("uid", uid);
        params.put("type",type);
        ApiHttpClient.get("action/api/my_information", params, handler);
    }

    public static void getMyInformation(int uid,AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("uid", uid);
        ApiHttpClient.get("action/api/my_information", params, handler);
    }

//  获取版本更新
    public static void checkUpdate(AsyncHttpResponseHandler handler) {
        ApiHttpClient.get("MobileAppVersion.xml", handler);
    }

    /**
     * 反馈意见
     *
     * @param data
     * @param handler
     */
    public static void feedback(String data, AsyncHttpResponseHandler handler) {
        uploadLog(data, "2", handler);
    }

//  上传日志
    private static void uploadLog(String data, String report,
                                  AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("app", "1");
        params.put("report", report);
        params.put("msg", data);
        ApiHttpClient.post("action/api/user_report_to_admin", params, handler);
    }

    public static void change_password(String originalPassword,String newPassword,AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("originalPassword",originalPassword);
        params.put("newPassword",newPassword);
        ApiHttpClient.post("",params,handler);
    }

    public static void pubTweet(Tweet tweet, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("uid", tweet.getAuthorid());
        params.put("msg", tweet.getContent());

        // Map<String, File> files = new HashMap<String, File>();
        if (!TextUtils.isEmpty(tweet.getImageFilePath())) {
            try {
                params.put("img", new File(tweet.getImageFilePath()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        ApiHttpClient.post("action/api/tweet_pub", params, handler);
    }

    public static void deleteTweet(int uid, int tweetid,
                                   AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("uid", uid);
        params.put("tweetid", tweetid);
        ApiHttpClient.post("action/api/tweet_delete", params, handler);
    }


    public static void getTweetList(int uid, int page,
                                    AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("uid", uid);
        params.put("pageIndex", page);
        params.put("pageSize", AppContext.PAGE_SIZE);
        ApiHttpClient.get("action/api/tweet_list", params, handler);
    }

    public static void getManualCatalogList(int parentId,int currenPage,int pageSize,AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("parentId",parentId);
        params.put("currenPage",currenPage);
        params.put("pageSize",pageSize);
        ApiHttpClient.post("", params, handler);
    }

    public static void getExpertList(int typeId,int currenPage,int pageSize,AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("typeId",typeId);
        params.put("currenPage",currenPage);
        params.put("pageSize",pageSize);
        ApiHttpClient.post("", params, handler);
    }

    public static void getTweetDetail(int id, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams("id", id);
        ApiHttpClient.get("action/api/tweet_detail", params, handler);
    }

    /**
     * 获取评论列表
     *
     * @PARAM ID
     * @PARAM CATALOG
     *            1新闻 2帖子 3动弹 4动态
     * @PARAM PAGE
     * @PARAM HANDLER
     */
    public static void getCommentList(int id, int catalog, int page,
                                      AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("catalog", catalog);
        params.put("id", id);
        params.put("pageIndex", page);
        params.put("pageSize", AppContext.PAGE_SIZE);
        params.put("clientType", "android");
        ApiHttpClient.get("action/api/comment_list", params, handler);
    }

    public static void deleteComment(int id, int catalog, int replyid,
                                     int authorid, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        params.put("catalog", catalog);
        params.put("replyid", replyid);
        params.put("authorid", authorid);
        ApiHttpClient.post("action/api/comment_delete", params, handler);
    }

    public static void replyComment(int id, int catalog, int replyid,
                                    int authorid, int uid, String content,
                                    AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("catalog", catalog);
        params.put("id", id);
        params.put("uid", uid);
        params.put("content", content);
        params.put("replyid", replyid);
        params.put("authorid", authorid);
        ApiHttpClient.post("action/api/comment_reply", params, handler);
    }

    public static void publicComment(int catalog, int id, int uid,
                                     String content, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("catalog", catalog);
        params.put("id", id);
        params.put("uid", uid);
        params.put("content", content);
        ApiHttpClient.post("action/api/comment_pub", params, handler);
    }

    public static void getAreaList(int jsonId,AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("jsonId",jsonId);
        ApiHttpClient.post("",params,handler);
    }

}
