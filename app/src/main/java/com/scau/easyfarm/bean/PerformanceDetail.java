package com.scau.easyfarm.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by chenhehong on 2016/10/21.
 */
public class PerformanceDetail extends Entity {
    @JSONField(name = "obj")
    private Performance performance;

    public Performance getPerformance() {
        return performance;
    }

    public void setPerformance(Performance performance) {
        this.performance = performance;
    }
}
