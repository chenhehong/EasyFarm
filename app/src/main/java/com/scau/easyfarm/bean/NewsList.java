package com.scau.easyfarm.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenhehong on 2016/9/26.
 */
public class NewsList extends Entity implements ListEntity<News> {

    public final static int CATALOG_HOT = 1;
    public final static int CATALOG_ANNOUNCE = 2;


    private int catalog;
    private List<News> newsList = new ArrayList<News>();

    public int getCatalog() {
        return catalog;
    }

    public void setCatalog(int catalog) {
        this.catalog = catalog;
    }

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }

    @Override
    public List<News> getList() {
        return newsList;
    }
}
