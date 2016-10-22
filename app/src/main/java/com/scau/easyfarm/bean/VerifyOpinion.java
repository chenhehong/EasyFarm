package com.scau.easyfarm.bean;

import java.util.Date;

/**
 * Created by ChenHehong on 2016/9/2.
 */
public class VerifyOpinion extends Entity{

    private String opinionPerson;
    private int seq;
    private String opinion;
    private Date opinionDate;
    private int status;

    public String getOpinionPerson() {
        return opinionPerson;
    }

    public void setOpinionPerson(String opinionPerson) {
        this.opinionPerson = opinionPerson;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public Date getOpinionDate() {
        return opinionDate;
    }

    public void setOpinionDate(Date opinionDate) {
        this.opinionDate = opinionDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
