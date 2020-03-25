package com.neusoft.zcapplication.tools;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * Created by Administrator on 2018/3/15.
 * 地图工具类
 */

public class MapUtil {

    /**
     * 判断是否有安装高德地图应用
     * @param context
     * @return
     */
    public static boolean checkGaoDeApkExist(Context context) {
        String packageName = "com.autonavi.minimap";
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    /**
     * 判断是否有安装百度地图应用
     * @param context
     * @return
     */
    public static boolean checkBaiDuApkExist(Context context) {
        String packageName = "com.baidu.BaiduMap";
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 百度坐标转高德坐标
     * @param bd_lat 纬度
     * @param bd_lon 经度
     * @return
     */
    public static double[] bd_gdecrypt(double bd_lat, double bd_lon) {
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * Math.PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * Math.PI);
        double gd_lon = z * Math.cos(theta);
        double gd_lat = z * Math.sin(theta);
        double[] ary = new double[2];
        ary[0] = gd_lat;
        ary[1] = gd_lon;
        return ary;
    }
    /**
     * 跳转高德地图
     * @param context
     * @param lat 百度坐标纬度
     * @param lng 百度坐标经度
     * @param address 目的地名称
     */
    public static void start2Gaode(Context context, String lat,String lng,String address) {
        boolean bool = checkGaoDeApkExist(context);
        if(bool){
            try {
                //地理编码
                double[] ary = bd_gdecrypt(Double.parseDouble(lat),Double.parseDouble(lng));
                StringBuffer stringBuffer = new StringBuffer("androidamap://route?sourceApplication=").append("amap");

                stringBuffer.append("&dlat=").append(ary[0])
                        .append("&dlon=").append(ary[1])
                        .append("&dname=").append(address)
                        .append("&dev=").append(0)
                        .append("&t=").append(0);

                Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse(stringBuffer.toString()));
                intent.setPackage("com.autonavi.minimap");
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.toastNeedData(context, "地址解析错误");
            }
        }else{
            ToastUtil.toastNeedData(context, "您的手未安装高德地图App~");
        }
    }
    /**
     * 跳转百度地图
     * @param context
     * @param lat 百度坐标纬度
     * @param lng 百度坐标经度
     * @param address 目的地名称
     */
    public static void start2Baidu(Context context, String lat,String lng,String address) {
        boolean bool = checkBaiDuApkExist(context);
        if(bool){
            try {
                String latLng = lat + "," + lng;
                Intent intent = Intent.getIntent("intent://map/direction?destination=latlng:" + latLng + "|name:" + address + "&mode=driving&src=#Intent;" + "scheme=bdapp;package=com.baidu.BaiduMap;end");
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.toastNeedData(context, "地址解析错误");
            }
        }else{
            ToastUtil.toastNeedData(context, "您的手机未安装百度地图App~");
        }
    }
}
