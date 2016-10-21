package com.scau.easyfarm.bean;

import java.util.ArrayList;
import java.util.HashMap;

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
    public final static int PERFORMANCE_PASS = 7;
    public final static int PERFORMANCE_REJECT = 8;
    public final static int PERFORMANCE_WAITING = 9;

    private String performanceCode;
    private String applyDate;
    private int applyManId;
    private String applyManName;
    private String applyManOrganization;
    private int performanceTypeId;
    private String performanceTypeStr;
    private String performanceStartDate;
    private String performanceEndDate;
    private Float WorkingHours;
    private ArrayList<String> fileList = new ArrayList<String>();
    private int status;
    private String verifyOption;

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

    public String getApplyManOrganization() {
        return applyManOrganization;
    }

    public void setApplyManOrganization(String applyManOrganization) {
        this.applyManOrganization = applyManOrganization;
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

    public String getPerformanceStartDate() {
        return performanceStartDate;
    }

    public void setPerformanceStartDate(String performanceStartDate) {
        this.performanceStartDate = performanceStartDate;
    }

    public String getPerformanceEndDate() {
        return performanceEndDate;
    }

    public void setPerformanceEndDate(String performanceEndDate) {
        this.performanceEndDate = performanceEndDate;
    }

    public Float getWorkingHours() {
        return WorkingHours;
    }

    public void setWorkingHours(Float workingHours) {
        WorkingHours = workingHours;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getVerifyOption() {
        return verifyOption;
    }

    public void setVerifyOption(String verifyOption) {
        this.verifyOption = verifyOption;
    }

    public ArrayList<String> getFileList() {
        return fileList;
    }

    public void setFileList(ArrayList<String> fileList) {
        this.fileList = fileList;
    }
}
