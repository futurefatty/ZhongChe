package com.neusoft.zcapplication.approval;

/**
 * 选择日期
 */

public interface TripItemClickListener {
    void selectDepartCityByType(int position,int type);//选择出发城市
    void selectDesCityByType(int position,int type);//选择目的地
    void selectDateByType(int position,int type);//选择日期
    void selectFlightDateByType(int position,int type,int dayType);//选择日期
    void delTripByType(int position,int type);//删除日期
}
