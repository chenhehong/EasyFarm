package com.scau.easyfarm;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.scau.easyfarm.api.ApiHttpClient;
import com.scau.easyfarm.base.BaseApplication;
import com.scau.easyfarm.bean.Constants;
import com.scau.easyfarm.bean.User;
import com.scau.easyfarm.cache.DataCleanManager;
import com.scau.easyfarm.util.CyptoUtils;
import com.scau.easyfarm.util.MethodsCompat;
import com.scau.easyfarm.util.TLog;
import com.scau.easyfarm.util.UIHelper;

import org.kymjs.kjframe.Core;
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

    public static final String ACCESS = "?access_token=ACCESS_TOKEN";

    public static final int PAGE_SIZE = 20;// 默认分页大小
//  定义用户登录的角色类型
    public static final String MEMBER = "member";
    public static final String USER="user";
    public static final String ADMIN="admin";

    private static AppContext instance;

    private int loginUid;

    private boolean login=false;

    private String roleName=" ";

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
        HttpConfig.CACHEPATH = "EasyFarm/imagecache";
    }

    //  初始化登录,利用AppConfig类读取Properties文件，获得用户的配置信息.登录与注销就是用的Properties来保存的
    private void initLogin() {
        User user = getLoginUser();
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
     * 保存用户登录信息
     */
    @SuppressWarnings("serial")
    public void saveUserInfo(final User user) {
        this.loginUid = user.getId();
        this.login = true;
        setProperties(new Properties() {
            {
                setProperty("user.uid", String.valueOf(user.getId()));
                setProperty("user.loginName", user.getLoginName());
                setProperty("user.roleName", user.getRoleName());
                setProperty("user.password",
                        CyptoUtils.encode("EasyFarm", user.getPassword()));
                setProperty("user.realName", user.getRealName());
                setProperty("user.organization", user.getOrganization());
                setProperty("user.phoneNumber", user.getPhoneNumber());
                setProperty("user.techType", user.getTechType());
                setProperty("user.description", user.getDescription());
                setProperty("user.sex", user.getSex());
                setProperty("user.age", user.getAge() + "");
                setProperty("user.email", user.getEmail());
                setProperty("user.address", user.getAddress());
                setProperty("user.isRememberMe",
                        String.valueOf(user.isRememberMe()));// 是否记住我的信息
            }
        });
    }

    /**
     * 更新用户信息
     */
    @SuppressWarnings("serial")
    public void updateUserInfo(final User user) {
        setProperties(new Properties() {
            {
                setProperty("user.realName", user.getRealName());
                setProperty("user.organization", user.getOrganization());
                setProperty("user.phoneNumber", user.getPhoneNumber());
                setProperty("user.techType", user.getTechType());
                setProperty("user.description", user.getDescription());
                setProperty("user.sex", user.getSex());
                setProperty("user.age", user.getAge() + "");
                setProperty("user.email", user.getEmail());
                setProperty("user.address", user.getAddress());
            }
        });
    }

    /**
     * 获得登录用户的信息
     */
    public User getLoginUser() {
        User user = new User();
        user.setId(StringUtils.toInt(getProperty("user.uid"), 0));
        user.setLoginName(getProperty("user.loginName"));
        user.setRealName(getProperty("user.realName"));
        user.setRoleName(getProperty("user.roleName"));
        user.setOrganization(getProperty("user.organization"));
        user.setPhoneNumber(getProperty("user.phoneNumber"));
        user.setTechType(getProperty("user.techType"));
        user.setDescription(getProperty("user.description"));
        user.setSex(getProperty("user.sex"));
        user.setAge(StringUtils.toInt(getProperty("user.age")));
        user.setEmail(getProperty("user.email"));
        user.setAddress(getProperty("user.address"));
        user.setRememberMe(StringUtils.toBool(getProperty("user.isRememberMe")));
        return user;
    }

    /**
     * 清除用户登录信息
     */
    public void cleanLoginInfo() {
        this.loginUid = 0;
        this.login = false;
        removeProperty("user.uid","user.roleName", "user.realName", "user.organization",
                    "user.phoneNumber", "user.techType", "user.description", "user.sex", "user.age", "user.email", "user.address", "user.isRememberMe");
    }

    public int getLoginUid() {
        return loginUid;
    }

    public boolean isLogin() {
        return login;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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
        this.roleName = "";
        Intent intent = new Intent(Constants.INTENT_ACTION_LOGOUT);
        sendBroadcast(intent);
    }

    /**
     * 清除保存的缓存
     */
    public void cleanCookie() {
        removeProperty(AppConfig.CONF_COOKIE);
    }

    public Properties getProperties() {
        return AppConfig.getAppConfig(this).get();
    }



    public void removeProperty(String... key) {
        AppConfig.getAppConfig(this).remove(key);
    }

//  设置是否加载图片
    public static void setLoadImage(boolean flag) {
        set(AppConfig.KEY_LOAD_IMAGE, flag);
    }

    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param VersionCode
     * @return
     */
    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }

    /**
     * 清除app缓存
     */
    public void clearAppCache() {
        DataCleanManager.cleanDatabases(this);
        // 清除数据缓存
        DataCleanManager.cleanInternalCache(this);
        // 2.2版本才有将应用缓存转移到sd卡的功能
        if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            DataCleanManager.cleanCustomCache(MethodsCompat
                    .getExternalCacheDir(this));
        }
        // 清除编辑器保存的临时内容
        Properties props = getProperties();
        for (Object key : props.keySet()) {
            String _key = key.toString();
            if (_key.startsWith("temp"))
                removeProperty(_key);
        }
        Core.getKJBitmap().cleanCache();
    }

//  获取问答草稿
    public static String getTweetDraft() {
        return getPreferences().getString(
                AppConfig.KEY_TWEET_DRAFT + getInstance().getLoginUid(), "");
    }

//  保存问答内容为草稿
    public static void setTweetDraft(String draft) {
        set(AppConfig.KEY_TWEET_DRAFT + getInstance().getLoginUid(), draft);
    }


}