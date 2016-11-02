package com.scau.easyfarm.bean;

import com.scau.easyfarm.api.remote.EasyFarmServerApi;

/**
 * Created by chenhehong on 2016/11/1.
 */
public class NoticeDetail extends Entity {

    private Notice notice;

    @Override
    public Notice getNotice() {
        return notice;
    }

    @Override
    public void setNotice(Notice notice) {
        this.notice = notice;
    }
}
