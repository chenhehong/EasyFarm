package com.scau.easyfarm.bean;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.util.UIHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 问题实体类
 *
 */
@SuppressWarnings("serial")
public class VillageProofResource extends Entity{


    private String address;
    private String description;
    private String createDate;
    private String imgSmall;
    private String imgBig;
    private int villageServiceId;
    private String villageServiceAddress;
    private String villageServiceReason;
    private String imageFilePath;


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

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }
}
