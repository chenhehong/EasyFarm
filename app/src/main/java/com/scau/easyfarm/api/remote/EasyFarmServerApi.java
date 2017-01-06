package com.scau.easyfarm.api.remote;


import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.api.ApiHttpClient;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.bean.Performance;
import com.scau.easyfarm.bean.ResultBean;
import com.scau.easyfarm.bean.Tweet;
import com.scau.easyfarm.bean.VillageProofResource;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.TLog;
import com.scau.easyfarm.util.UIHelper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;


public class EasyFarmServerApi {

    private static final OperationResponseHandler mGetAccessTokenHandler = new OperationResponseHandler() {

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
        ApiHttpClient.post(loginurl, params, mGetAccessTokenHandler);
    }

    public static void clearNoticeCount(int type,String ts,OperationResponseHandler mClearNoticeCountHandler){
        RequestParams params = new RequestParams();
        params.put("personalID",AppContext.getInstance().getLoginUid());
        params.put("type",type);
        params.put("ts",ts);
        ApiHttpClient.post("front/mobile/notice/setNotice", params, mClearNoticeCountHandler);
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

    public static void register(String loginName,String password,String realName,String age,int gender,String email,String mobile,String address, AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("loginName",loginName);
        params.put("password",password);
        params.put("realName",realName);
        params.put("phoneNumber",mobile);
        params.put("sex",gender);
        params.put("age",age);
        params.put("email",email);
        params.put("address",address);
        ApiHttpClient.post("front/mobile/user/register", params, handler);
    }

    public static void modifiedCommanUserInformation(String realName, String age, int gender, String email, String mobile, String address,String portrait, AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("autoID",AppContext.getInstance().getLoginUid());
        params.put("realName",realName);
        params.put("phoneNumber",mobile);
        params.put("sex",gender);
        params.put("age",age);
        params.put("email",email);
        params.put("address",address);
        if (!TextUtils.isEmpty(portrait)) {
            try {
                params.put("file", new File(portrait));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        ApiHttpClient.post("front/mobile/user/modifyuser", params, handler);
    }

    public static void modifiedExpertInformation(String age, int gender, String email, String mobile, String address,String jobPosition,String techType,String techType2,String description,String portrait, AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("autoID",AppContext.getInstance().getLoginUid());
        params.put("phoneNumber",mobile);
        params.put("sex",gender);
        params.put("age",age);
        params.put("email",email);
        params.put("address",address);
        params.put("techTitle",jobPosition);
        params.put("techType",techType);
        params.put("techType2",techType2);
        params.put("description",description);
        if (!TextUtils.isEmpty(portrait)) {
            try {
                params.put("file", new File(portrait));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        ApiHttpClient.post("front/mobile/user/modifyuser", params, handler);
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
        ApiHttpClient.get("app/MobileAppVersion.json", handler);
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
        ApiHttpClient.post("front/mobile/user/api/editUserPwd", params, handler);
    }

    public static void findWitTweet(String questionTitle,int categoryId,
                                AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("questionTitle", questionTitle);
        params.put("categoryId",categoryId);
        ApiHttpClient.post("front/mobile/communicate/api/searchByQuestion", params, handler);
    }

    public static void pubTweet(Tweet tweet, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("personalID", tweet.getAuthorid());
        params.put("author", AppContext.getInstance().getLoginUser().getRealName());
        params.put("title",tweet.getTitle());
        params.put("content", tweet.getContent());
        params.put("manualCategoryID",tweet.getManualCategoryID());
        params.put("expertPersonalID",tweet.getExpertPersonalID());
        params.put("expertName",tweet.getExpertName());
        File[] files = new File[tweet.getImageFiles().size()];
        if (tweet.getImageFiles()!=null&&tweet.getImageFiles().size()>0) {
            for (int i=0;i<tweet.getImageFiles().size();i++){
                files[i] = new File(tweet.getImageFiles().get(i).getPath());
            }
        }
        try {
            params.put("file",files);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ApiHttpClient.post("front/mobile/communicate/api/addQuestionWithImage", params, handler);
    }

    public static void deleteTweet(int uid, int tweetid,
                                   AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("uid", uid);
        params.put("tweetid", tweetid);
        ApiHttpClient.post("action/api/tweet_delete", params, handler);
    }


    public static void getTweetList(int page,
                                    AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("uid", AppContext.getInstance().getLoginUid());
        params.put("page", page+1);
        params.put("rows", AppContext.PAGE_SIZE);
        params.put("sort", "createDate");
        params.put("order", "desc");
        ApiHttpClient.get("front/mobile/communicate/api/getquestionList", params, handler);
    }

    public static void getMyTweetList(int page,
                                    AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("id", AppContext.getInstance().getLoginUid());
        params.put("page", page+1);
        params.put("rows", AppContext.PAGE_SIZE);
        params.put("sort", "createDate");
        params.put("order", "desc");
        ApiHttpClient.get("front/mobile/communicate/api/getquestionListByID", params, handler);
    }

    public static void getManualCatalogListByParentId(int parentId, int currenPage, int pageSize, AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("manualCategoryID",parentId);
        params.put("currenPage",currenPage);
        params.put("pageSize", pageSize);
        ApiHttpClient.post("front/mobile/manual/api/getChildById", params, handler);
    }

    public static void getManualCatalogListByCode(String parentCode, int currenPage, int pageSize, AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("manualCategoryCode",parentCode);
        params.put("currenPage",currenPage);
        params.put("pageSize", pageSize);
        ApiHttpClient.post("front/mobile/manual/api/getChildByCode", params, handler);
    }

    public static void getExpertList(int typeId,int currenPage,int pageSize,AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("id",typeId);
        params.put("page",currenPage+1);
        params.put("row", pageSize);
        ApiHttpClient.post("front/mobile/expert/getEpxertByManualID", params, handler);
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
        ApiHttpClient.post("front/mobile/area/getCitys", params, handler);
    }

    public static void getCountyList(int jsonId,AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("CityID",jsonId);
        ApiHttpClient.post("front/mobile/area/getCountys", params, handler);
    }

    public static void getMyApplyVillageServiceList(int categoryId, int page, int status,
                                                    AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("personalID", AppContext.getInstance().getLoginUid());
        params.put("status",status);
        params.put("page", page+1);
        params.put("rows", AppContext.PAGE_SIZE);
        ApiHttpClient.get("front/mobile/village/api/getServiceByApplyMan", params, handler);
    }

    public static void getMyVillageServiceProofList(int isFinish, int page,
                                               AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("personalID", AppContext.getInstance().getLoginUid());
        params.put("isfinish",isFinish);
        params.put("page", page+1);
        params.put("rows", AppContext.PAGE_SIZE);
        params.put("order", "desc");
        params.put("sort","createDate");
        ApiHttpClient.get("front/mobile/village/api/getTimeServiceByPersonalID", params, handler);
    }

    public static void getVillageServiceDetail(int id,AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("id",id);
        ApiHttpClient.post("front/mobile/village/api/getServiceDetail", params, handler);
    }

    public static void addVillageService(String businessArea,String businessAddress,int businessReasonType,String businessReason,
                                         String businessDate,String returnDate,String villageServicePersonIds,String leaderIds,int serverType, AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("uid",AppContext.getInstance().getLoginUid());
        params.put("businessArea",businessArea);
        params.put("businessAddress",businessAddress);
        params.put("businessDate",businessDate);
        params.put("returnDate",returnDate);
        params.put("reasonType",businessReasonType);
        params.put("businessReason",businessReason);
        params.put("personIDs",villageServicePersonIds);
        params.put("leaderIDs",leaderIds);
        params.put("villageType",serverType);
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

    public static void getVerifyServiceList(int needAudit, int page, int status,
                                            AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("uid",AppContext.getInstance().getLoginUid());
        params.put("needAudit",needAudit);
        params.put("status",status);
        params.put("page", page+1);
        params.put("rows", AppContext.PAGE_SIZE);
        params.put("order", "desc");
        params.put("sort","applyDate");
        ApiHttpClient.get("front/mobile/village/api/getServiceByAuditor", params, handler);
    }

    public static void verifyVillageService(int villageServiceId, int status,String optinion,
                                             AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("opinionStatus",status);
        params.put("uid", AppContext.getInstance().getLoginUid());
        params.put("villageServiceID", villageServiceId);
        params.put("opinion",optinion);
        ApiHttpClient.get("front/mobile/village/api/audit", params, handler);
    }

    public static void pubVillageServiceProof(VillageProofResource v, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("uid", AppContext.getInstance().getLoginUid());
        params.put("villageServiceId",v.getVillageServiceId());
        params.put("area",v.getAddress());
        params.put("description",v.getDescription());
        params.put("typeId",v.getDescriptionId());
        params.put("createDate",v.getCreateDate());
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
        params.put("categoryTypeID", catalog);
        params.put("page", page+1);
        params.put("rows", AppContext.PAGE_SIZE);
        ApiHttpClient.post("front/mobile/news/api/getListPublish", params, handler);
    }

    /**
     * 获取新闻明细
     *
     * @param id 新闻的id
     * @param handler
     */
    public static void getNewsDetail(int id, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams("id", id);
        ApiHttpClient.get("front/mobile/news/api/getArticle", params, handler);
    }

    public static void getManualDetail(int id, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams("id", id);
        ApiHttpClient.get("front/mobile/manual/api/getContentById", params, handler);
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

    public static void sendServerSummary(String summary,String visitLinkMan,String visitLinkPhone,int serverId, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("villageServiceID",serverId);
        params.put("uid",AppContext.getInstance().getLoginUid());
        params.put("serviceSummary",summary);
        params.put("visitLinkMan",visitLinkMan);
        params.put("visitLinkPhone",visitLinkPhone);
        ApiHttpClient.post("front/mobile/village/api/finishVillage", params, handler);
    }

    public static void getExpertBaseList(int categoryCodeId, int page,
                                     AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("uid", AppContext.getInstance().getLoginUid());
        params.put("page", page+1);
        params.put("rows", AppContext.PAGE_SIZE);
        params.put("id", categoryCodeId);
        ApiHttpClient.get("front/mobile/expert/getEpxertByManualID", params, handler);
    }

    public static void getExpertBaseDetail(int id,AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("id",id);
        ApiHttpClient.post("front/mobile/expert/getEpxertByPersonalID", params, handler);
    }

    public static void getApplyPerformanceList(int categoryId, int page, int status,
                                                    AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("uid", AppContext.getInstance().getLoginUid());
        params.put("status",status);
        params.put("page", page+1);
        params.put("rows", AppContext.PAGE_SIZE);
        ApiHttpClient.get("front/mobile/performance/getApplyListDataGrid", params, handler);
    }

    public static void deletePerformance(int performanceId,
                                            AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("id", performanceId);
        ApiHttpClient.post("front/mobile/village/api/deleteServiceById", params, handler);
    }

    public static void addPerformanceApply(AsyncHttpResponseHandler handler,Performance performance){
        RequestParams params = new RequestParams();
        params.put("uid",AppContext.getInstance().getLoginUid());
        params.put("saveFlag",1);
        params.put("startDate",performance.getServerStartDate());
        params.put("endDate",performance.getServerEndDate());
        params.put("applyType",performance.getPerformanceTypeId());
        params.put("unittimes",performance.getApplyWorkTime());
        params.put("description",performance.getDescription());
        File[] files = new File[performance.getFileList().size()];
        if (performance.getFileList()!=null&&performance.getFileList().size()>0) {
            for (int i=0;i<performance.getFileList().size();i++){
                files[i] = new File(performance.getFileList().get(i).getPath());
            }
            boolean flag = false;
            String typeIdString = "";
            String descriptionString = "";
            for (int i=0;i<performance.getFileList().size();i++){
                if (flag){
                    typeIdString+="$";
                    descriptionString+="$";
                }else{
                    flag=true;
                }
                typeIdString+=performance.getFileList().get(i).getTypeId();
                descriptionString+=performance.getFileList().get(i).getDescription();
            }
            try {
                params.put("file",files);
                params.put("fileTypeId",typeIdString);
                params.put("fileDescription",descriptionString);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        ApiHttpClient.post("front/mobile/performance/addApplyPerformance", params, handler);
    }

    public static void getPerformanceTypeList(int currenPage,int pageSize,AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("currenPage",currenPage);
        params.put("pageSize", pageSize);
        ApiHttpClient.post("front/mobile/performance/getTypeList", params, handler);
    }

    public static void getPerformanceDetail(int id,AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("id",id);
        ApiHttpClient.post("front/mobile/performance/auditPage", params, handler);
    }

    public static void getVerifyPerformanceList(int needAudit, int page, int status,
                                                AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("uid",AppContext.getInstance().getLoginUid());
        params.put("needAudit",needAudit);
        params.put("status",status);
        params.put("page", page+1);
        params.put("rows", AppContext.PAGE_SIZE);
        params.put("order", "desc");
        params.put("sort","applyDate");
        ApiHttpClient.get("front/mobile/performance/getAuditListDataGrid", params, handler);
    }

    public static void verifyPerformance(int performanceId, int status,String optinion,String userWorkTime,
                                            AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("auditFlag",status);
        params.put("uid", AppContext.getInstance().getLoginUid());
        params.put("applyPerformanceID", performanceId);
        params.put("auditPersonalWorkingHour",userWorkTime);
        params.put("opinion",optinion);
        ApiHttpClient.get("front/mobile/performance/auditPerformanceOpinion", params, handler);
    }

    public static void getMonthStaticsPerformanceList(int categoryId, int page, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("uid", AppContext.getInstance().getLoginUid());
        params.put("isPersonal",0);
        params.put("page", page+1);
        params.put("rows", AppContext.PAGE_SIZE);
        ApiHttpClient.get("front/mobile/performance/getStatisticsForMonth", params, handler);
    }

    public static void getStatisticsPerformanceList(int categoryId, int page, String  month,
                                               AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("uid", AppContext.getInstance().getLoginUid());
        params.put("declareDate",month);
        params.put("isPersonal",0);
        params.put("page", page+1);
        params.put("rows", AppContext.PAGE_SIZE);
        ApiHttpClient.get("front/mobile/performance/getStatisticsDataGrid", params, handler);
    }

    public static void getMonthStaticsMyPerformanceList(int categoryId, int page, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("uid", AppContext.getInstance().getLoginUid());
        params.put("isPersonal",1);
        params.put("page", page+1);
        params.put("rows", AppContext.PAGE_SIZE);
        ApiHttpClient.get("front/mobile/performance/getStatisticsForMonth", params, handler);
    }

    public static void getStatisticsMyPerformanceList(int categoryId, int page, String  month,
                                                    AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("uid", AppContext.getInstance().getLoginUid());
        params.put("declareDate",month);
        params.put("isPersonal",1);
        params.put("page", page+1);
        params.put("rows", AppContext.PAGE_SIZE);
        ApiHttpClient.get("front/mobile/performance/getStatisticsDataGrid", params, handler);
    }

    public static void getMonthStaticsServiceList(int categoryId, int page, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("uid", AppContext.getInstance().getLoginUid());
        params.put("isPersonal",0);
        params.put("page", page+1);
        params.put("rows", AppContext.PAGE_SIZE);
        ApiHttpClient.get("front/mobile/village/api/statistics", params, handler);
    }

    public static void getStatisticsServiceList(int categoryId, int page, String  month,
                                                    AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("uid", AppContext.getInstance().getLoginUid());
        params.put("queryDate",month);
        params.put("page", page+1);
        params.put("rows", AppContext.PAGE_SIZE);
        ApiHttpClient.get("front/mobile/village/api/getServiceAuditorStatistics", params, handler);
    }

    public static void getMonthStaticsMyServiceList(int categoryId, int page, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("uid", AppContext.getInstance().getLoginUid());
        params.put("isPersonal",1);
        params.put("page", page+1);
        params.put("rows", AppContext.PAGE_SIZE);
        ApiHttpClient.get("front/mobile/village/api/statistics", params, handler);
    }

    public static void getStatisticsMyServiceList(int categoryId, int page, String  month,
                                                AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("uid", AppContext.getInstance().getLoginUid());
        params.put("queryDate",month);
        params.put("page", page+1);
        params.put("rows", AppContext.PAGE_SIZE);
        ApiHttpClient.get("front/mobile/village/api/getServicePersonalStatistics", params, handler);
    }

    public static void getNoticeCount(AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("personalID", AppContext.getInstance().getLoginUid());
        params.put("isNotice", 0);
        ApiHttpClient.get("front/mobile/notice/getCount", params, handler);
    }

    public static void getNoticeList(int type,int page,AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("personalID", AppContext.getInstance().getLoginUid());
        params.put("type",type);
        params.put("page", page+1);
        params.put("rows", AppContext.PAGE_SIZE);
        ApiHttpClient.get("front/mobile/notice/getNotice", params, handler);
    }

    public static void getSimpleTextList(int currenPage,int pageSize,AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("currenPage",currenPage);
        params.put("pageSize", pageSize);
        ApiHttpClient.post("front/mobile/user/getTechTitle", params, handler);
    }

}
