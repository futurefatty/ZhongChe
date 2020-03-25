package com.neusoft.zcapplication.entity;

/**
 * Author: TenzLiu
 * Time: 2018/6/25 16:31
 * Desc: 机票
 */

public class SearchFlightTicketCity {

    private String cityCode;
    private String cityName;

    public SearchFlightTicketCity() {
    }

    public SearchFlightTicketCity(String cityCode, String cityName) {
        this.cityCode = cityCode;
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
