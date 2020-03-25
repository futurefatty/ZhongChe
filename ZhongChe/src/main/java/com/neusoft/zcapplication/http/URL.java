package com.neusoft.zcapplication.http;

/**
 * url管理类
 */

public class URL {

    public static final int loginType = 2;//用户登录途径，微信：1；app：2；pc：3
    public static final int LOGIN_TYPE = 2;//所有接口其中一个参数，2代表APP
    /**
     * 外网地址
     */
//  public static final String IP = "https://58.20.212.74:9005/travel_app-web/";//正式环境
//    public static final String IP = "https://sl.csrzic.com:9005/travel_app-web/";//正式环境
    public static final String IP = "http://58.20.212.75:8001/travel_app-web/";///测试环境


//    public static final String IP = "http://47.103.73.217:3307/";


//    public static final String IP = "http://192.168.1.248:3307/";///测试环境

    public static final String ZHONGCHE_92 = IP.concat("member/");//8092-4000
    public static final String ZHONGCHE_93 = IP.concat("order/");//8093-4001
    public static final String ZHONGCHE_90 = IP.concat("supplier/");//8090-8002
    public static final String ZHONGCHE_94 = IP.concat("pms/");//
    public static final String ZC = IP.concat("zc/");//
    public static final String ZHONGCHE_H5 = IP.concat("pages/phone/");//H5链接
    public static final String ZHONGCHE_IMG = IP.concat("travel-web/");//供应商LOGO
    public static final String FLIGHT_LOGO = IP.concat("image/logo/");//航司LOGO
    //下载路径
    public static final String loadUrl = "https://sl.csrzic.com:9001/android/pages/crrc.apk";//下载地址
    //?opt=1&code=CRCC_21111111111922&cust=crrc&product=1&failUrl=m.baidu.com
    //product:	产品编码，1、门票首页；2、门票订单列表页；3、度假首页；4、度假订单列表页
//    public static final String CTRIP_LINK =  "https://gateway.uat.ctripqa.com/webapp/welfare/oauth?opt=1&cust=crrc";//携程服务
    public static final String CTRIP_LINK = "https://b.ctrip.com/webapp/welfare/oauth?opt=1&cust=crrc";//携程服务

}
