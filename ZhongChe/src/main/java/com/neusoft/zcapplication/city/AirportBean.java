package com.neusoft.zcapplication.city;

/**
 * Created by Administrator on 2017/5/16.
 * 机场类
 */

public class AirportBean {
    private int id;
    private String airportName;//机场中文名称
    private String airportNameEn;//机场英文名称
    private String airportCode;//机场3字码
    private int countryId;//所属城市id
    private String sortLetters;//机场名称的首字母
    private boolean isChecked;//是否为选中的机场
    private boolean isAbroadCity;//true 国际机场，false 国内机场
    public AirportBean() {
    }

    public AirportBean(int id, String airportName, String airportNameEn, String airportCode, int countryId,String sortLetters) {
        this.id = id;
        this.airportName = airportName;
        this.airportNameEn = airportNameEn;
        this.airportCode = airportCode;
        this.countryId = countryId;
        this.sortLetters = sortLetters;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public String getAirportNameEn() {
        return airportNameEn;
    }

    public void setAirportNameEn(String airportNameEn) {
        this.airportNameEn = airportNameEn;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isAbroadCity() {
        return isAbroadCity;
    }

    public void setAbroadCity(boolean abroadCity) {
        isAbroadCity = abroadCity;
    }
}
