package com.scau.easyfarm.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chenhehong on 2016/10/21.
 */
public class Performance extends Entity {

    public static final String[] statusStrArray = {"待审核","通过","不通过"};
    public static final HashMap<String,Integer> statusStrMap = new HashMap<String,Integer>(){
        { put("待审核", PERFORMANCE_WAITING);  put("通过", PERFORMANCE_PASS);  put("不通过", PERFORMANCE_REJECT);}
    };
    public static final HashMap<Integer,String> statusIntMap = new HashMap<Integer,String>(){
        { put(PERFORMANCE_WAITING,"待审核");  put(PERFORMANCE_PASS,"通过");  put(PERFORMANCE_REJECT,"不通过");;}
    };
    public final static int PERFORMANCE_ALL = 0;
    public final static int PERFORMANCE_PASS = 65;
    public final static int PERFORMANCE_REJECT = 66;
    public final static int PERFORMANCE_WAITING = 67;

    public static final String[] myStatusStrArray = {"通过","不通过"};
    public static final HashMap<String,Integer> myStatusStrMap = new HashMap<String,Integer>(){
        { put("通过",MY_STATUS_PASS);  put("不通过",MY_STATUS_REJECT);}
    };
    public final static int MY_STATUS_PASS = 1;
    public final static int MY_STATUS_REJECT = 0;

    @JSONField(name = "serialCode")
    private String performanceCode;
    private String applyDate;
    @JSONField(name = "personalID")
    private int applyManId;
    @JSONField(name = "applyMan")
    private String applyManName;
    @JSONField(name = "applyType")
    private int performanceTypeId;
    @JSONField(name = "applyTypeDesc")
    private String performanceTypeStr;
    @JSONField(name = "declareDate")
    private String performanceServerDate;
    @JSONField(name = "declareDateDesc")
    private String performanceServerDateDesc;
    private int status;
    @JSONField(name = "waitAuditOrganizationDesc")
    private String nextVerifyOrganization;
    @JSONField(name = "workingHour")
    private float applyWorkTime;//申报的工作量
    @JSONField(name = "attachmentList")
    private ArrayList<FileResource> fileList = new ArrayList<>();
    List<VerifyOpinion> verifyOpinions = new ArrayList<VerifyOpinion>();
    @JSONField(name = "workTimeList")
    List<PerformanceMemberWorkTime> performanceMemberWorkTimeList = new ArrayList<PerformanceMemberWorkTime>();

    public String getPerformanceCode() {
        return performanceCode;
    }

    public void setPerformanceCode(String performanceCode) {
        this.performanceCode = performanceCode;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

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

    public int getPerformanceTypeId() {
        return performanceTypeId;
    }

    public void setPerformanceTypeId(int performanceTypeId) {
        this.performanceTypeId = performanceTypeId;
    }

    public String getPerformanceTypeStr() {
        return performanceTypeStr;
    }

    public void setPerformanceTypeStr(String performanceTypeStr) {
        this.performanceTypeStr = performanceTypeStr;
    }

    public String getPerformanceServerDate() {
        return performanceServerDate;
    }

    public void setPerformanceServerDate(String performanceServerDate) {
        this.performanceServerDate = performanceServerDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<PerformanceMemberWorkTime> getPerformanceMemberWorkTimeList() {
        return performanceMemberWorkTimeList;
    }

    public void setPerformanceMemberWorkTimeList(List<PerformanceMemberWorkTime> performanceMemberWorkTimeList) {
        this.performanceMemberWorkTimeList = performanceMemberWorkTimeList;
    }

    public float getApplyWorkTime() {
        return applyWorkTime;
    }

    public void setApplyWorkTime(float applyWorkTime) {
        this.applyWorkTime = applyWorkTime;
    }

    public String getNextVerifyOrganization() {
        return nextVerifyOrganization;
    }

    public void setNextVerifyOrganization(String nextVerifyOrganization) {
        this.nextVerifyOrganization = nextVerifyOrganization;
    }

    public List<VerifyOpinion> getVerifyOpinions() {
        return verifyOpinions;
    }

    public void setVerifyOpinions(List<VerifyOpinion> verifyOpinions) {
        this.verifyOpinions = verifyOpinions;
    }

    public String getPerformanceServerDateDesc() {
        return performanceServerDateDesc;
    }

    public void setPerformanceServerDateDesc(String performanceServerDateDesc) {
        this.performanceServerDateDesc = performanceServerDateDesc;
    }

    public String getStatusString(){
        if (status==PERFORMANCE_WAITING){
            return "等待"+nextVerifyOrganization+"审核";
        }
        return statusIntMap.get(status);
    }

    public ArrayList<FileResource> getFileList() {
        return fileList;
    }

    public void setFileList(ArrayList<FileResource> fileList) {
        this.fileList = fileList;
    }
}
