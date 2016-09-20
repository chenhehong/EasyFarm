package com.scau.easyfarm.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ChenHehong on 2016/9/2.
 */
public class VillageService extends Entity {

    @JSONField(name = "personalID")
    private int applyManId;
    @JSONField(name = "applyMan")
    private String applyManName;
    @JSONField(name = "ApplyDate")
    private String applyDate;
    private String businessArea;
    private String businessAddress;
    private String businessDate;
    private String returnDate;
    private String businessReason;
    private int status;
    @JSONField(name = "villageServiceOpinionList")
    List<VillageServiceOpinion> villageServiceOpinions = new ArrayList<VillageServiceOpinion>();
    @JSONField(name = "villagePersonList")
    List<User> villageServicePerson = new ArrayList<User>();

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
        this.businessDate = businessDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
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

    public List<VillageServiceOpinion> getVillageServiceOpinions() {
        return villageServiceOpinions;
    }

    public void setVillageServiceOpinions(List<VillageServiceOpinion> villageServiceOpinions) {
        this.villageServiceOpinions = villageServiceOpinions;
    }

    public List<User> getVillageServicePerson() {
        return villageServicePerson;
    }

    public void setVillageServicePerson(List<User> villageServicePerson) {
        this.villageServicePerson = villageServicePerson;
    }
}
