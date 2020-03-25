package com.neusoft.zcapplication.tools;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.neusoft.zcapplication.base.ZcApplication;

/**
 * Author: TenzLiu
 * Time: 2018/8/30 9:59
 * Desc: 地图工具类
 */

public class AMapUtil {

    private static AMapUtil instance;
    private AMapLocationClient mAMapLocationClient;
    private AMapLocationClientOption mAMapLocationClientOption;
    private AMapLocationHandler mAMapLocationHandler;
    public interface AMapLocationHandler{
        //定位成功
        void locateSuccess(double longitude, double latitude, String province, String city,
                           String cityCode, String district, String address);
        //定位失败
        void locateFailed(String errorMessage);
    }

    /**
     * 定位监听回调
     */
    private AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if(null != aMapLocation){
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if(aMapLocation.getErrorCode()==0){
                    if(null != mAMapLocationHandler){
                        //定位成功返回
                        mAMapLocationHandler.locateSuccess(aMapLocation.getLongitude(),aMapLocation.getLatitude(),
                                aMapLocation.getProvince(),aMapLocation.getCity(),aMapLocation.getCityCode(),
                                aMapLocation.getDistrict(),aMapLocation.getAddress());
                    }

                }else{
                    if(null != mAMapLocationHandler){
                        mAMapLocationHandler.locateFailed("定位失败,"+aMapLocation.getErrorInfo());
                    }
                }
            }else{
                if(null != mAMapLocationHandler){
                    mAMapLocationHandler.locateFailed("定位失败");
                }
            }
        }
    };

    /**
     * 设置handler
     * @param AMapLocationHandler
     */
    public void setAMapLocationHandler(AMapLocationHandler AMapLocationHandler) {
        mAMapLocationHandler = AMapLocationHandler;
    }

    /**
     * 获取单例
     * @return
     */
    public static AMapUtil getInstance() {
        if(null == instance){
            instance = new AMapUtil();
        }
        return instance;
    }

    /**
     * 初始化定位
     */
    public void initLocation() {
        mAMapLocationClient = new AMapLocationClient(ZcApplication.getAppContext());
        mAMapLocationClientOption = getDefaultLocationOption();
        mAMapLocationClient.setLocationOption(mAMapLocationClientOption);
        mAMapLocationClient.setLocationListener(mAMapLocationListener);
    }

    /**
     * 默认定位参数
     * @return
     */
    public AMapLocationClientOption getDefaultLocationOption() {
        AMapLocationClientOption mapLocationClientOption = new AMapLocationClientOption();
        //可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mapLocationClientOption.setGpsFirst(false);
        //可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mapLocationClientOption.setHttpTimeOut(30000);
        //可选，设置定位间隔。默认为2秒
        mapLocationClientOption.setInterval(2000);
        //可选，设置是否返回逆地理地址信息。默认是true
        mapLocationClientOption.setNeedAddress(true);
        //可选，设置是否单次定位。默认是false
        mapLocationClientOption.setOnceLocation(true);
        //可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        mapLocationClientOption.setOnceLocationLatest(false);
        //可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);
        //可选，设置是否使用传感器。默认是false
        mapLocationClientOption.setSensorEnable(false);
        //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mapLocationClientOption.setWifiScan(true);
        //可选，设置是否使用缓存定位，默认为true
        mapLocationClientOption.setLocationCacheEnable(true);
        //可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        mapLocationClientOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);
        return mapLocationClientOption;
    }

    /**
     * 开始定位
     */
    public void startLocation(){
        LogUtil.d("开始定位-------------------------------");
        mAMapLocationClient.startLocation();
    }

    /**
     * 停止定位
     */
    public void stopLocation(){
        mAMapLocationClient.stopLocation();
    }

    /**
     * 销毁服务
     */
    public void destroy(){
        mAMapLocationClient.onDestroy();
    }


}
