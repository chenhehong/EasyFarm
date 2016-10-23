package com.scau.easyfarm.bean;

import android.os.Bundle;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by ChenHehong on 2016/10/23.
 */
public class Module extends Entity {

    public static final String MODULE_SERVICE_MANAGE = "serviceManage";
    public static final String MODULE_PERFORMANCE_MANAGE = "performanceManage";
    public static final String MODULE_INDUSTRY_BASE = "industryBase";
    public static final String MODULE_SKILL_BASE = "skillBase";
    public static final String MODULE_ACHIEVEMENT_BASE = "achievementBase";
    public static final String MODULE_VARIETY_BASE = "varietyBase";
    public static final String MODULE_EXPERT_BASE = "expertBase";
    public static final String MODULE_SERVICE_APPLY = "serviceApply";
    public static final String MODULE_SERVICE_VERIFY = "serviceAudit";
    public static final String MODULE_SERVICE_PROOF = "serviceUploadfile";
    public static final String MODULE_SERVICE_STATISTICS_EXPERT = "serviceStatisticsExpert";
    public static final String MODULE_SERVICE_STATISTICS_VERIFIER = "serviceStatisticsAudit";
    public static final String MODULE_PERFORMANCE_APPLY = "performanceApply";
    public static final String MODULE_PERFORMANCE_VIREFY = "performanceAudit";
    public static final String MODULE_PERFORMANCE_STATISTICS_EXPERT = "performanceStatisticsExpert";
    public static final String MODULE_PERFORMANCE_STATISTICS_VERIFIER = "performanceStatisticsAudit";


    private SimpleBackPage simpleBackPage;
    private String name;
    private int icon;
    private Bundle args;

    public Module(SimpleBackPage simpleBackPage, String name, int icon, Bundle args) {
        this.simpleBackPage = simpleBackPage;
        this.name = name;
        this.icon = icon;
        this.args = args;
    }

    public SimpleBackPage getSimpleBackPage() {
        return simpleBackPage;
    }

    public void setSimpleBackPage(SimpleBackPage simpleBackPage) {
        this.simpleBackPage = simpleBackPage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Bundle getArgs() {
        return args;
    }

    public void setArgs(Bundle args) {
        this.args = args;
    }

    public static boolean existModule(ArrayList<String> list,String s){
        for (int i=0;i<list.size();i++){
            if (list.get(i).equals(s))
                return true;
        }
        return false;
    }

    public static ArrayList<String> trimBlank(ArrayList<String> stringList){
        for (int i=0;i<stringList.size();i++){
            String s = stringList.get(i);
            stringList.set(i,s.trim());
        }
        return stringList;
    }
}
