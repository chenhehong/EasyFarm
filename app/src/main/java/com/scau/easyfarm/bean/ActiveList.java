package com.scau.easyfarm.bean;


import java.util.ArrayList;
import java.util.List;

/**
 * 动态实体列表
 *
 */
@SuppressWarnings("serial")
public class ActiveList extends Entity implements ListEntity<Active> {

    public final static int CATALOG_LASTEST = 1;// 最新
    public final static int CATALOG_ATME = 2;// @我
    public final static int CATALOG_COMMENT = 3;// 评论
    public final static int CATALOG_MYSELF = 4;// 我自己

    private int pageSize;

    private int activeCount;

    private List<Active> activelist = new ArrayList<Active>();
    
    private Result result;

    public int getPageSize() {
	return pageSize;
    }

    public int getActiveCount() {
	return activeCount;
    }

    public List<Active> getActivelist() {
	return activelist;
    }

    @Override
    public List<Active> getList() {
	return activelist;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
