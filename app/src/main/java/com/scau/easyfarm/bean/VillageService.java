package com.scau.easyfarm.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.scau.easyfarm.util.DateTimeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ChenHehong on 2016/9/2.
 */
public class VillageService extends Entity {

    public static final String[] statusStrArray = {"待审核","通过","不通过"};
    public static final HashMap<String,Integer> statusStrMap = new HashMap<String,Integer>(){
        { put("待审核",VILLAGE_SERVICE_WAITING);  put("通过",VILLAGE_SERVICE_PASS);  put("不通过",VILLAGE_SERVICE_REJECT);put("已结束",VILLAGE_SERVICE_COMPLETED);}
    };
    public static final HashMap<Integer,String> statusIntMap = new HashMap<Integer,String>(){
        { put(VILLAGE_SERVICE_WAITING,"待审核");  put(VILLAGE_SERVICE_PASS,"通过");  put(VILLAGE_SERVICE_REJECT,"不通过");put(VILLAGE_SERVICE_COMPLETED,"已结束");}
    };
    public final static int VILLAGE_SERVICE_ALL = 0;
    public final static int VILLAGE_SERVICE_PASS = 7;
    public final static int VILLAGE_SERVICE_REJECT = 8;
    public final static int VILLAGE_SERVICE_WAITING = 9;
    public final static int VILLAGE_SERVICE_COMPLETED = 49;

    public static final String[] myStatusStrArray = {"通过","不通过"};
    public static final HashMap<String,Integer> myStatusStrMap = new HashMap<String,Integer>(){
        { put("通过",MY_STATUS_PASS);  put("不通过",MY_STATUS_REJECT);}
    };
    public final static int MY_STATUS_PASS = 10;
    public final static int MY_STATUS_REJECT = 11;

    @JSONField(name = "personalID")
    private int applyManId;
    @JSONField(name = "applyMan")
    private String applyManName;
    private boolean isLeader = false;
    @JSONField(name = "applyDateDesc")
    private String applyDate="";
    private String businessArea;
    private String businessAddress;
    @JSONField(name = "businessDateDesc")
    private String businessDate="";
    @JSONField(name = "returnDateDesc")
    private String returnDate="";
    @JSONField(name = "returnDate")
    private long returnDateTimeStamp;
    private String businessReason;
    private int status;
    @JSONField(name = "waitAuditOrganizationDesc")
    private  String nextVerifyOrganization;
    private int serverType;//服务类型
    @JSONField(name = "serialCode")
    private String serverNumber="";//服务单号
    @JSONField(name = "villageServiceOpinionList")
    List<VerifyOpinion> verifyOpinions = new ArrayList<VerifyOpinion>();
    @JSONField(name = "villagePersonList")
    List<User> villageServicePerson = new ArrayList<User>();
    @JSONField(name = "villageLeaderList")
    List<User> leaders = new ArrayList<User>();
    @JSONField(name = "villageCommentCode")
    private String commentQrcodePath;

    public int getApplyManId() {
        return applyManId;
    }

    public void setApplyManId(int applyManId) {
        this.applyManId = applyManId;
    }

    public String getApplyManName() {
        return applyManName;
    }

    public void setApplyManName(String applyManName) {
        this.applyManName = applyManName;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getBusinessArea() {
        return businessArea;
    }

    public void setBusinessArea(String businessArea) {
        this.businessArea = businessArea;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(String businessDate) {
        this.businessDate = DateTimeUtil.dateTimeToDate(businessDate);
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = DateTimeUtil.dateTimeToDate(returnDate);
    }

    public String getBusinessReason() {
        return businessReason;
    }

    public void setBusinessReason(String businessReason) {
        this.businessReason = businessReason;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<VerifyOpinion> getVerifyOpinions() {
        return verifyOpinions;
    }

    public void setVerifyOpinions(List<VerifyOpinion> verifyOpinions) {
        this.verifyOpinions = verifyOpinions;
    }

    public List<User> getVillageServicePerson() {
        return villageServicePerson;
    }

    public void setVillageServicePerson(List<User> villageServicePerson) {
        this.villageServicePerson = villageServicePerson;
    }

    public int getServerType() {
        return serverType;
    }

    public void setServerType(int serverType) {
        this.serverType = serverType;
    }

    public List<User> getLeaders() {
        return leaders;
    }

    public void setLeaders(List<User> leaders) {
        this.leaders = leaders;
    }

    public String getServerNumber() {
        return serverNumber;
    }

    public void setServerNumber(String serverNumber) {
        this.serverNumber = serverNumber;
    }

    public boolean isLeader() {
        return isLeader;
    }

    public void setIsLeader(boolean isLeader) {
        this.isLeader = isLeader;
    }

    public String getNextVerifyOrganization() {
        return nextVerifyOrganization;
    }

    public void setNextVerifyOrganization(String nextVerifyOrganization) {
        this.nextVerifyOrganization = nextVerifyOrganization;
    }

    public String getCommentQrcodePath() {
        return commentQrcodePath;
    }

    public void setCommentQrcodePath(String commentQrcodePath) {
        this.commentQrcodePath = commentQrcodePath;
    }

    public String getStatusString(){
        if (status==VILLAGE_SERVICE_WAITING){
            return "等待"+nextVerifyOrganization+"审核";
        }
        return statusIntMap.get(status);
    }

    public long getReturnDateTimeStamp() {
        return returnDateTimeStamp;
    }

    public void setReturnDateTimeStamp(long returnDateTimeStamp) {
        this.returnDateTimeStamp = returnDateTimeStamp;
    }
}
