package com.scau.easyfarm;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.scau.easyfarm.api.ApiHttpClient;
import com.scau.easyfarm.base.BaseApplication;
import com.scau.easyfarm.bean.Constants;
import com.scau.easyfarm.bean.Expert;
import com.scau.easyfarm.bean.UserEntity;
import com.scau.easyfarm.util.CyptoUtils;
import com.scau.easyfarm.util.TLog;
import com.scau.easyfarm.util.UIHelper;

import org.kymjs.kjframe.http.HttpConfig;
import org.kymjs.kjframe.utils.KJLoger;
import org.kymjs.kjframe.utils.StringUtils;

import java.util.Properties;
import java.util.UUID;

/**
 * Created by ChenHehong on 2016/6/11.
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 */
public class AppContext extends BaseApplication {

    public static final int PAGE_SIZE = 20;// 默认分页大小
//  定义用户登录的身份类型
    public static final String MEMBER = "member";
    public static final String EXPERT="expert";
    public static final String ADMIN="admin";

    private static AppContext instance;

    private int loginUid;

    private boolean login=false;

    private String loginType="";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();
        initLogin();

//        Thread.setDefaultUncaughtExceptionHandler(AppException
//                .getAppExceptionHandler(this));
        UIHelper.sendBroadcastForNotice(this);
    }

    private void init() {
        // 初始化网络请求
        AsyncHttpClient client = new AsyncHttpClient();
//      用于存储Cookie，持久化存储登录状态。和AsyncHttpClient同属于一个包中
        PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
        client.setCookieStore(myCookieStore);
        ApiHttpClient.setHttpClient(client);
        ApiHttpClient.setCookie(ApiHttpClient.getCookie(this));

        // Log控制器
        KJLoger.openDebutLog(true);
        TLog.DEBUG = BuildConfig.DEBUG;

        // Bitmap缓存地址
        HttpConfig.CACHEPATH = "OSChina/imagecache";
    }

    //  初始化登录,利用AppConfig类读取Properties文件，获得用户的配置信息.登录与注销就是用的Properties来保存的
    private void initLogin() {
        UserEntity user = getLoginUser();
        if (null != user && user.getId() > 0) {
            login = true;
            loginUid = user.getId();
        } else {
            this.cleanLoginInfo();
        }
    }


    /**
     * 获得当前app运行的AppContext
     *
     * @return
     */
    public static AppContext getInstance() {
        return instance;
    }

    /**
     * 获取cookie时传AppConfig.CONF_COOKIE
     *
     * @param key
     * @return
     */
    public String getProperty(String key) {
        String res = AppConfig.getAppConfig(this).get(key);
        return res;
    }

    public void setProperties(Properties ps) {
        AppConfig.getAppConfig(this).set(ps);
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    /**
     * 获取App唯一标识
     *
     * @return
     */
    public String getAppId() {
        String uniqueID = getProperty(AppConfig.CONF_APP_UNIQUEID);
        if (StringUtils.isEmpty(uniqueID)) {
            uniqueID = UUID.randomUUID().toString();
            setProperty(AppConfig.CONF_APP_UNIQUEID, uniqueID);
        }
        return uniqueID;
    }

    public void setProperty(String key, String value) {
        AppConfig.getAppConfig(this).set(key, value);
    }

    /**
     * 保存专家用户登录信息
     */
    @SuppressWarnings("serial")
    public void saveUserInfo(final UserEntity user) {
        this.loginUid = user.getId();
        this.login = true;
        if (user instanceof Expert){
            this.loginType = EXPERT;
            final Expert expert = (Expert)user;
            setProperties(new Properties() {
                {
                    setProperty("expert.uid", String.valueOf(expert.getId()));
                    setProperty("expert.loginName", expert.getLoginName());
                    setProperty("expert.password",
                            CyptoUtils.encode("EasyFarm", expert.getPassword()));
                    setProperty("expert.realName", expert.getRealName());
                    setProperty("expert.department", expert.getDepartment());
                    setProperty("expert.phoneNumber", expert.getPhoneNumber());
                    setProperty("expert.techType", expert.getTechType());
                    setProperty("expert.isRememberMe",
                            String.valueOf(expert.isRememberMe()));// 是否记住我的信息
                }
            });
        }

    }

    /**
     * 更新专家用户信息
     */
    @SuppressWarnings("serial")
    public void updateUserInfo(final UserEntity user) {
        if (user instanceof Expert){
            loginType = EXPERT;
            final Expert expert = (Expert)user;
            setProperties(new Properties() {
                {
                    setProperty("expert.realName", expert.getRealName());
                    setProperty("expert.department", expert.getDepartment());
                    setProperty("expert.phoneNumber", expert.getPhoneNumber());
                    setProperty("expert.techType", expert.getTechType());
                }
            });
        }

    }

    /**
     * 获得登录用户的信息
     */
    public UserEntity getLoginUser() {
        if (loginType==null)
            return null;
        if (loginType.equals(EXPERT)){
            Expert expert = new Expert();
            expert.setId(StringUtils.toInt(getProperty("expert.uid"), 0));
            expert.setLoginName(getProperty("expert.loginName"));
            expert.setRealName(getProperty("expert.realName"));
            expert.setDepartment(getProperty("expert.department"));
            expert.setPhoneNumber(getProperty("expert.phoneNumber"));
            expert.setTechType(getProperty("expert.techType"));
            expert.setRememberMe(StringUtils.toBool(getProperty("expert.isRememberMe")));
            return expert;
        }
        return  null;
    }

    /**
     * 清除用户登录信息
     */
    public void cleanLoginInfo() {
        this.loginUid = 0;
        this.login = false;
        if (loginType.equals(EXPERT)){
            removeProperty("expert.uid", "expert.loginName", "expert.realName", "expert.department",
                    "expert.phoneNumber", "expert.techType", "expert.isRememberMe");
        }

    }

    public int getLoginUid() {
        return loginUid;
    }

    public boolean isLogin() {
        return login;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    /**
     * 用户注销
     */
    public void Logout() {
        cleanLoginInfo();
        ApiHttpClient.cleanCookie();
        this.cleanCookie();
        this.login = false;
        this.loginUid = 0;

        Intent intent = new Intent(Constants.INTENT_ACTION_LOGOUT);
        sendBroadcast(intent);
    }

    /**
     * 清除保存的缓存
     */
    public void cleanCookie() {
        removeProperty(AppConfig.CONF_COOKIE);
    }


    public void removeProperty(String... key) {
        AppConfig.getAppConfig(this).remove(key);
    }

}