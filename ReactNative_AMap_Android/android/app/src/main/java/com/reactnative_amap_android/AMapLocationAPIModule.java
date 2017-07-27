package com.reactnative_amap_android;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.JsonWriter;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hongming.wang on 2017/7/26.
 */

public class AMapLocationAPIModule extends ReactContextBaseJavaModule implements AMapLocationListener {
    Context mContext = null;
    AMapLocationClient amapLocationClient = null;
    AMapLocationClientOption amapLocationOption = null;
    Callback mCallback = null;
    public AMapLocationAPIModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mContext = reactContext.getApplicationContext();
        initAMapLocation();
    }

    @Override
    public String getName() {
        return "AMapLocationAPI";
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        JSONObject json = new JSONObject();
        try {
            int errorCode = -1;
            if (null != aMapLocation) {
                errorCode = aMapLocation.getErrorCode();
                if (errorCode == AMapLocation.LOCATION_SUCCESS) {
                    json.put("latitude", aMapLocation.getLatitude());
                    json.put("longitude", aMapLocation.getLongitude());
                    json.put("address", aMapLocation.getAddress());
                    json.put("lcotime", aMapLocation.getTime());
                } else {
                    json.put("errorInfo", aMapLocation.getErrorInfo());
                }
            } else {
                json.put("errorInfo", "location is null");
            }
            mCallback.invoke(errorCode, json.toString());
        }catch (Throwable e){
        }
    }

    /**
     * 获取位置
     * @param locationParameters 定位参数
     *                           <p>
     *                               onceLocation：是否单次定位
     *                                needAddress：是否需要地址信息
     *                                interval：定位间隔(ms)
     *                               {'onceLocation':false,'needAddress':true,'interval':2000}
     *                           </p>
     * @param callback
     */
    @ReactMethod
    public void getLocation(String locationParameters, Callback callback){
        boolean isOnceLocation = false;
        boolean isNeedAddress = true;
        long interval = 2000;
        try {
            JSONObject json = new JSONObject(locationParameters);
            isOnceLocation = json.optBoolean("onceLocation");
            isNeedAddress = json.optBoolean("needAddress");
            interval = json.optLong("interval");
        }catch(Throwable e){
        }

        if(null == amapLocationClient){
            initAMapLocation();
        }
        mCallback = callback;
        amapLocationOption.setInterval(interval);
        amapLocationOption.setOnceLocation(isOnceLocation);
        amapLocationOption.setNeedAddress(isNeedAddress);
        amapLocationClient.startLocation();
    }

    @ReactMethod
    public void stopLocation(){
        if(null != amapLocationClient){
            amapLocationClient.stopLocation();
            amapLocationClient.onDestroy();
            amapLocationClient = null;
        }
    }


    private void initAMapLocation(){
        amapLocationClient = new AMapLocationClient(mContext);
        amapLocationOption = new AMapLocationClientOption();
        //设置使用高精度定位模式
        amapLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        amapLocationClient.setLocationListener(this);
    }


}
