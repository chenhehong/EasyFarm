package com.scau.easyfarm.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 问题实体类
 *
 */
@SuppressWarnings("serial")
public class VillageProofResource extends Entity{


    private String address="";
    private String description="";
    private int descriptionId;
    @JSONField(name = "createDateDesc")
    private String createDate="";
    private String imgSmall="";
    private String imgBig="";
    @JSONField(name = "metaID")
    private int villageServiceId;
    private String villageServiceDescription="";
    private String villageServiceAddress="";
    private String villageServiceReason="";
    @JSONField(name = "metaPath")
    private String uploadFilePath ="";


    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getImgSmall() {
        return imgSmall;
    }

    public void setImgSmall(String imgSmall) {
        this.imgSmall = imgSmall;
    }

    public String getImgBig() {
        return imgBig;
    }

    public void setImgBig(String imgBig) {
        this.imgBig = imgBig;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getVillageServiceId() {
        return villageServiceId;
    }

    public void setVillageServiceId(int villageServiceId) {
        this.villageServiceId = villageServiceId;
    }

    public String getVillageServiceAddress() {
        return villageServiceAddress;
    }

    public void setVillageServiceAddress(String villageServiceAddress) {
        this.villageServiceAddress = villageServiceAddress;
    }

    public String getVillageServiceReason() {
        return villageServiceReason;
    }

    public void setVillageServiceReason(String villageServiceReason) {
        this.villageServiceReason = villageServiceReason;
    }

    public String getUploadFilePath() {
        return uploadFilePath;
    }

    public void setUploadFilePath(String uploadFilePath) {
        this.uploadFilePath = uploadFilePath;
    }

    public String getVillageServiceDescription() {
        return villageServiceDescription;
    }

    public void setVillageServiceDescription(String villageServiceDescription) {
        this.villageServiceDescription = villageServiceDescription;
    }

    public int getDescriptionId() {
        return descriptionId;
    }

    public void setDescriptionId(int descriptionId) {
        this.descriptionId = descriptionId;
    }
}
