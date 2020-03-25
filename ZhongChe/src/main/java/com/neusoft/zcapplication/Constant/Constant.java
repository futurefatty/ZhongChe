package com.neusoft.zcapplication.Constant;

import com.neusoft.zcapplication.http.URL;

/**
 * 常量类
 */

public class Constant {

    public static final int APP_TYPE = 2;//所有接口其中一个参数，微信：1；APP：2；PC：3

    public static final String ZHONGCHE_BASE = URL.IP;//Base链接
    //    public static final String ZHONGCHE_H5 = "http://192.168.1.105:3312/".concat("pages/phone/");//H5链接
    public static final String ZHONGCHE_H5 = URL.IP.concat("pages/phone/");//H5链接


    public static final String ZHONGCHE_IMG = URL.IP.concat("travel-web/");//供应商LOGO
    public static final String FLIGHT_LOGO = URL.IP.concat("image/logo/");//航司LOGO

    //apk下载路径
    public static final String DOWNLOADURL = "https://sl.csrzic.com:9001/android/pages/crrc.apk";//下载地址


    //?opt=1&code=CRCC_21111111111922&cust=crrc&product=1&failUrl=m.baidu.com
    //product:	产品编码，1、门票首页；2、门票订单列表页；3、度假首页；4、度假订单列表页
//    public static final String CTRIP_LINK =  "https://gateway.uat.ctripqa.com/webapp/welfare/oauth?opt=1&cust=crrc";//携程服务
    public static final String CTRIP_LINK = "https://b.ctrip.com/webapp/welfare/oauth?opt=1&cust=crrc";//携程服务

    // 手势密码点的状态
    public static final int POINT_STATE_NORMAL = 0; // 正常状态
    public static final int POINT_STATE_SELECTED = 1; // 按下状态
    public static final int POINT_STATE_WRONG = 2; // 错误状态


    //获取机票查询出发城市编码
    public static final String SEARCHFLIGHTTICKETCITYCODESTART = "search_flight_ticket_city_code_start";
    //获取机票查询出发城市名称
    public static final String SEARCHFLIGHTTICKETCITYNAMESTART = "search_flight_ticket_city_name_start";
    //获取机票查询到达城市编码
    public static final String SEARCHFLIGHTTICKETCITYCODEEND = "search_flight_ticket_city_code_end";
    //获取机票查询到达城市名称
    public static final String SEARCHFLIGHTTICKETCITYNAMEEND = "search_flight_ticket_city_name_end";    //获取机票查询到达城市名称
    //获取机票查询时间
    public static final String SEARCHFLIGHTTICKETCITYTIME = "search_flight_ticket_city_time";
    //获取国际机票行程
    public static final String INTERNATION_FLIGHT_JOURENY = "INTERNATION_FLIGHT_JOURENY";
    //获取国际机票城市历史
    public static final String INTERNATION_FLIGHT_CITY_HISTORY = "INTERNATION_FLIGHT_CITY_HISTORY";
    //获取国内机票城市历史
    public static final String FLIGHT_CITY_HISTORY = "FLIGHT_CITY_HISTORY";

}
