package com.scau.easyfarm.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenHehong on 2016/10/1.
 */
public class ProofResourceDescriptionList extends Entity implements ListEntity<ProofResourceDescription> {

    @JSONField(name = "obj")
    private List<ProofResourceDescription> proofResourceDescriptionList = new ArrayList<ProofResourceDescription>();

    public List<ProofResourceDescription> getProofResourceDescriptionList() {
        return proofResourceDescriptionList;
    }

    public void setProofResourceDescriptionList(List<ProofResourceDescription> proofResourceDescriptionList) {
        this.proofResourceDescriptionList = proofResourceDescriptionList;
    }

    @Override
    public List<ProofResourceDescription> getList() {
        return proofResourceDescriptionList;
    }
}
