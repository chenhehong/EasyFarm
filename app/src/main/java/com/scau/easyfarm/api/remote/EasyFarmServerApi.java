package com.scau.easyfarm.api.remote;


import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.api.ApiHttpClient;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.bean.ResultBean;
import com.scau.easyfarm.bean.Tweet;
import com.scau.easyfarm.bean.VillageProofResource;
import com.scau.easyfarm.util.JsonUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;


public class EasyFarmServerApi {

    private static final OperationResponseHandler mHandler = new OperationResponseHandler() {

        @Override
        public void onSuccess(int code, ByteArrayInputStream is, Object[] args) {
            ResultBean resultBean = JsonUtils.toBean(ResultBean.class, is);
            if (resultBean != null) {
                AppContext.getInstance().ACCESS = resultBean.getObj().toString();
            }
        }

        @Override
        public void onFailure(int code, String errorMessage, Object[] args) {
            AppContext.showToast(errorMessage + code);
        }
    };

//   获取ACCESSTOKEN
    public static void getAccessToken(){
        RequestParams params = new RequestParams();
        params.put("usercode", AppContext.ACCESS_TOKEN_USERCODE);
        params.put("secret", AppContext.ACCESS_TOKEN_USERSECRET);
        String loginurl = "front/mobile/api/base/getaccesstoken";
        ApiHttpClient.post(loginurl, params, mHandler);
    }


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
        String loginurl = "front/mobile/user/login";
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
        params.put("personalid", uid);
        ApiHttpClient.post("front/mobile/user/getuser", params, handler);
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
        params.put("personalid",AppContext.getInstance().getLoginUid());
        params.put("oldPwd",originalPassword);
        params.put("pwd",newPassword);
        ApiHttpClient.post("front/mobile/user/api/editUserPwd",params,handler);
    }

    public static void pubTweet(Tweet tweet, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("personalID", tweet.getAuthorid());
        params.put("author", AppContext.getInstance().getLoginUser().getRealName());
        params.put("title",tweet.getTitle());
        params.put("content", tweet.getContent());
        params.put("manualCategoryID",tweet.getManualCategoryID());
        params.put("expertPersonalID",1);
        params.put("expertName",tweet.getExpertName());
        // Map<String, File> files = new HashMap<String, File>();
        if (!TextUtils.isEmpty(tweet.getImageFilePath())) {
            try {
                params.put("file", new File(tweet.getImageFilePath()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        ApiHttpClient.post("front/mobile/communicate/api/addquestion", params, handler);
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
        params.put("page", page+1);
        params.put("rows", AppContext.PAGE_SIZE);
        ApiHttpClient.get("front/mobile/communicate/api/getquestionList", params, handler);
    }

    public static void getMyTweetList(int uid, int page,
                                    AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("id", uid);
        params.put("page", page+1);
        params.put("rows", AppContext.PAGE_SIZE);
        ApiHttpClient.get("front/mobile/communicate/api/getquestionListByID", params, handler);
    }

    public static void getManualCatalogList(int parentId,int currenPage,int pageSize,AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("manualCategoryID",parentId);
        params.put("currenPage",currenPage);
        params.put("pageSize", pageSize);
        ApiHttpClient.post("front/mobile/manual/api/getChildById", params, handler);
    }

    public static void getExpertList(int typeId,int currenPage,int pageSize,AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("manualCategoryID",typeId);
        params.put("currenPage",currenPage);
        params.put("pageSize", pageSize);
        ApiHttpClient.post("front/mobile/user/getAllEpxert", params, handler);
    }

    public static void getTweetDetail(int id, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams("questionID", id);
        ApiHttpClient.get("front/mobile/communicate/api/getquestion", params, handler);
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
        params.put("questionID", id);
        params.put("pageIndex", page);
        params.put("pageSize", AppContext.PAGE_SIZE);
        ApiHttpClient.get("front/mobile/communicate/api/getcomments", params, handler);
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
        params.put("questionID", id);
        params.put("commentID", replyid);
        params.put("personalID", uid);
        params.put("content", content);
        params.put("commentName",AppContext.getInstance().getLoginUser().getRealName());
        ApiHttpClient.post("front/mobile/communicate/api/comment", params, handler);
    }

    public static void publicComment(int catalog, int id, int uid,
                                     String content, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("catalog", catalog);
        params.put("questionID", id);
        params.put("personalID", uid);
        params.put("content", content);
        params.put("commentName",AppContext.getInstance().getLoginUser().getRealName());
        ApiHttpClient.post("front/mobile/communicate/api/comment", params, handler);
    }

    public static void getProvinceList(AsyncHttpResponseHandler handler){
        ApiHttpClient.post("front/mobile/area/getProvinces",handler);
    }

    public static void getCityList(int CityID,AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("ProID",CityID);
        ApiHttpClient.post("front/mobile/area/getCitys",params,handler);
    }

    public static void getCountyList(int jsonId,AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("CityID",jsonId);
        ApiHttpClient.post("front/mobile/area/getCountys", params, handler);
    }

    public static void getMyVillageServiceList(int categoryId, int page, int status,
                                               AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("personalID", AppContext.getInstance().getLoginUid());
        params.put("status",status);
        params.put("page", page+1);
        params.put("rows", AppContext.PAGE_SIZE);
        ApiHttpClient.get("front/mobile/village/api/getServiceByPersonalID", params, handler);
    }

    public static void getMyVillageServiceProofList(int isFinish, int page,
                                               AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("personalID", AppContext.getInstance().getLoginUid());
        params.put("isfinish",isFinish);
        params.put("page", page+1);
        params.put("rows", AppContext.PAGE_SIZE);
        ApiHttpClient.get("front/mobile/village/api/getServiceByTime", params, handler);
    }

    public static void getVillageServiceDetail(int id,AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("id",id);
        ApiHttpClient.post("front/mobile/village/api/getServiceDetail", params, handler);
    }

    public static void addVillageService(String businessArea,String businessAddress,int businessReasonType,String businessReason,
                                         String businessDate,String returnDate,String villageServicePersonIds, AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("uid",AppContext.getInstance().getLoginUid());
        params.put("businessArea",businessArea);
        params.put("businessAddress",businessAddress);
        params.put("businessDate",businessDate);
        params.put("returnDate",returnDate);
        params.put("reasonType",businessReasonType);
        params.put("businessReason",businessReason);
        params.put("personIDs",villageServicePersonIds);
        ApiHttpClient.post("front/mobile/village/api/addService", params, handler);
    }

    public static void deleteVillageService(int villageServiceId,
                                   AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("id", villageServiceId);
        ApiHttpClient.post("front/mobile/village/api/deleteServiceById", params, handler);
    }

    public static void getManualList(int uid,String categoryCode, int page,
                                    AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("uid", uid);
        params.put("page", page+1);
        params.put("rows", AppContext.PAGE_SIZE);
        params.put("categoryCode", categoryCode);
        ApiHttpClient.get("front/mobile/manual/api/getContentByCode", params, handler);
    }

    public static void getVillageServiceList(int categoryId, int page, int status,
                                               AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("status",status);
        params.put("page", page+1);
        params.put("rows", AppContext.PAGE_SIZE);
        ApiHttpClient.get("front/mobile/village/api/getAllServiceDetail", params, handler);
    }

    public static void verifyVillageService(int villageServiceId, int status,String optinion,
                                             AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("opinionStatus",status);
        params.put("PersonalID", AppContext.getInstance().getLoginUid());
        params.put("villageServiceID", villageServiceId);
        params.put("opinion",optinion);
        ApiHttpClient.get("front/mobile/village/api/audit", params, handler);
    }

    public static void pubVillageServiceProof(VillageProofResource v, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("villageServiceId",v.getVillageServiceId());
        params.put("area",v.getAddress());
        params.put("description",v.getDescription());
        params.put("createDate",v.getCreateDate());
        // Map<String, File> files = new HashMap<String, File>();
        if (!TextUtils.isEmpty(v.getImageFilePath())) {
            try {
                params.put("file", new File(v.getImageFilePath()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        ApiHttpClient.post("front/mobile/village/api/uploadfile", params, handler);
    }

    public static void getVillageServiceProofResourceList(int categoryId, int page, int villageServiceId,
                                               AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("personalID", AppContext.getInstance().getLoginUid());
        params.put("serviceID",villageServiceId);
        params.put("page", page+1);
        params.put("rows", AppContext.PAGE_SIZE);
        ApiHttpClient.get("front/mobile/village/api/getResourceByServiceID", params, handler);
    }

    public static void deleteVillageServiceProofResource(int villageServiceId,
                                            AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("id", villageServiceId);
        ApiHttpClient.post("front/mobile/village/api/deleteResourceById", params, handler);
    }

    /**
     * 获取新闻列表
     *
     * @param catalog
     *            类别 （1，2，3）
     * @param page
     *            第几页
     * @param handler
     */
    public static void getNewsList(int catalog, int page,
                                   AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("catalog", catalog);
        params.put("pageIndex", page);
        params.put("pageSize", AppContext.PAGE_SIZE);
        ApiHttpClient.get("action/api/news_list", params, handler);
    }

    /**
     * 获取新闻明细
     *
     * @param id 新闻的id
     * @param handler
     */
    public static void getNewsDetail(int id, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams("id", id);
        ApiHttpClient.get("action/api/news_detail", params, handler);
    }

    /**
     * 查找用户
     *
     * @param loginName
     * @param handler
     */
    public static void findUser(String loginName,
                                AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("searchKey", loginName);
        ApiHttpClient.post("front/mobile/village/api/searchExpertByName", params, handler);
    }

    public static void getVillageServiceReasonList(int currenPage,int pageSize,AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("currenPage",currenPage);
        params.put("pageSize", pageSize);
        ApiHttpClient.post("front/mobile/village/api/getServiceReason", params, handler);
    }

    public static void getProofResourceDescriptionList(int currenPage,int pageSize,AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("currenPage",currenPage);
        params.put("pageSize", pageSize);
        ApiHttpClient.post("front/mobile/village/api/getResourceType", params, handler);
    }


}
