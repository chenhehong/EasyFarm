package com.scau.easyfarm.service;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.scau.easyfarm.AppContext;


/**
 * Created by ChenHehong on 2016/10/4.
 */
public class LocationUtils {

    private Handler handler;

    private LocationService locationService;

    private String locationStr="";

    public LocationUtils(Handler handler) {
        this.handler = handler;
        initLocation();
    }

    public void start(){
        locationService.start();
    }

    private void initLocation(){
        locationService = AppContext.getInstance().locationService;
        locationService.registerListener(mListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
    }

    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                if (location.getProvince()!=null&&location.getCity()!=null&&location.getDistrict()!=null){
                    locationStr=location.getProvince()+"-"+location.getCity()+"-"+location.getDistrict()+","+location.getStreet();
                }else {
                    locationStr = "获取定位失败，请检查网络是否正常！";
                }
            }else {
                locationStr = "获取定位失败，请检查网络是否正常！";
            }
            sendMsg(locationStr);
        }
    };

    /**
     * 返回定位信息
     */
    public void sendMsg(String locationStr) {
        Message message = new Message();
        message.what = 0;
        message.obj = locationStr;
        handler.sendMessage(message);
        //停止定位服务
        locationService.stop();
    }

}
