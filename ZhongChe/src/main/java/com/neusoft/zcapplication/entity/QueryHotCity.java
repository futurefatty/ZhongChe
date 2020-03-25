package com.neusoft.zcapplication.entity;

/**
 * Author: TenzLiu
 * Time: 2018/6/13 16:38
 * Desc: 热门城市
 */

public class QueryHotCity {

    /**
     * googleLon : 116.5083695
     * cityName : 北京
     * googleLat : 39.9890085
     * hotareaName : 铁科院环铁试验基地
     * tkHotAreaId : 30
     * cityId : 156110100
     */

    private String googleLon;
    private String cityName;
    private String googleLat;
    private String hotareaName;
    private String tkHotAreaId;
    private String cityId;

    public String getGoogleLon() {
        return googleLon;
    }

    public void setGoogleLon(String googleLon) {
        this.googleLon = googleLon;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getGoogleLat() {
        return googleLat;
    }

    public void setGoogleLat(String googleLat) {
        this.googleLat = googleLat;
    }

    public String getHotareaName() {
        return hotareaName;
    }

    public void setHotareaName(String hotareaName) {
        this.hotareaName = hotareaName;
    }

    public String getTkHotAreaId() {
        return tkHotAreaId;
    }

    public void setTkHotAreaId(String tkHotAreaId) {
        this.tkHotAreaId = tkHotAreaId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

}
