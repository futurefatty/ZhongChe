package com.neusoft.zcapplication.Bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/9/27.
 * 航班实体
 */

public class FlightItem implements IFlightItem, Serializable {
    private String fromCity;//出发城市
    private String fromCityCode;//出发城市三字码
    private String toCity;//到达城市
    private String toCityCode;//到达城市三字码
    private String startTime;//出发时间
    private String tripMode;//出行方式
    private int tripType;//出行方式（id）
    private int isBookHotel;//酒店预订 1：是；0：否
    private String checkinTime;//入住酒店日期
    private String checkoutTime;//离店日期
    private boolean isShowDel;//是否显示删除图标
    private boolean isShowItem;//是否显示Item

    @Override
    public String getFromCity() {
        return null == fromCity ? "" : fromCity;
    }

    @Override
    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    @Override
    public String getToCity() {
        return null == toCity ? "" : toCity;
    }

    @Override
    public void setToCity(String toCity) {
        this.toCity = toCity;
    }

    @Override
    public String getStartTime() {
        return null == startTime ? "" : startTime;
    }

    @Override
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Override
    public String getTripMode() {
        return tripMode;
    }

    @Override
    public void setTripMode(String tripMode) {
        this.tripMode = tripMode;
    }

    @Override
    public int getTripType() {
        return tripType;
    }

    @Override
    public void setTripType(int tripType) {
        this.tripType = tripType;
    }

    @Override
    public int isBookHotel() {
        return isBookHotel;
    }

    @Override
    public void setBookHotel(int bookHotel) {
        isBookHotel = bookHotel;
    }

    @Override
    public String getCheckinTime() {
        return null == checkinTime ? "" : checkinTime;
    }

    @Override
    public void setCheckinTime(String checkinTime) {
        this.checkinTime = checkinTime;
    }

    @Override
    public String getCheckoutTime() {
        return null == checkoutTime ? "" : checkoutTime;
    }

    @Override
    public void setCheckoutTime(String checkoutTime) {
        this.checkoutTime = checkoutTime;
    }

    @Override
    public boolean isShowDel() {
        return isShowDel;
    }

    @Override
    public void setShowDel(boolean showDel) {
        isShowDel = showDel;
    }

    @Override
    public boolean isShowItem() {
        return isShowItem;
    }

    @Override
    public void setShowItem(boolean showItem) {
        isShowItem = showItem;
    }

    @Override
    public String getFromCityCode() {
        return null == fromCityCode ? "" : fromCityCode;
    }

    @Override
    public void setFromCityCode(String fromCityCode) {
        this.fromCityCode = fromCityCode;
    }

    @Override
    public String getToCityCode() {
        return null == toCityCode ? "" : toCityCode;
    }

    @Override
    public void setToCityCode(String toCityCode) {
        this.toCityCode = toCityCode;
    }
}
