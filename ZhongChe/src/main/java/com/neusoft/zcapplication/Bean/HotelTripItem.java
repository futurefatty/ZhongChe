package com.neusoft.zcapplication.Bean;

/**
 * 酒店预定申请单bean
 */

public class HotelTripItem {
    private String cityName;//目的地城市名
    private String cityCode;//目的地城市编码
    private String checkInDate;//入住时间
    private String checkOutDate;//离店时间

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }
}
