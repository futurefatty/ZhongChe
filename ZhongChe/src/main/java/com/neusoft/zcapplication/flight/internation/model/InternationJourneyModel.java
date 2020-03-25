package com.neusoft.zcapplication.flight.internation.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author:Six
 * Date:2019/5/27
 */
public class InternationJourneyModel implements Serializable {
    private String fromCity;
    private String toCity;
    private String journeyDateStr;
    private Date journeyDate;
    private String week;
    private int position;
    private String fromThreeCode;
    private String toThreeCode;
    //出发城市是否是国际
    private boolean fromCityIsInternation;
    //到达城市是否是国际
    private boolean toCityIsInternation;

    public boolean isFromCityIsInternation() {
        return fromCityIsInternation;
    }

    public void setFromCityIsInternation(boolean fromCityIsInternation) {
        this.fromCityIsInternation = fromCityIsInternation;
    }

    public boolean isToCityIsInternation() {
        return toCityIsInternation;
    }

    public void setToCityIsInternation(boolean toCityIsInternation) {
        this.toCityIsInternation = toCityIsInternation;
    }

    public String getFromThreeCode() {
        return fromThreeCode;
    }

    public void setFromThreeCode(String fromThreeCode) {
        this.fromThreeCode = fromThreeCode;
    }

    public String getToThreeCode() {
        return toThreeCode;
    }

    public void setToThreeCode(String toThreeCode) {
        this.toThreeCode = toThreeCode;
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getFromCity() {
        return fromCity;
    }

    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    public String getToCity() {
        return toCity;
    }

    public void setToCity(String toCity) {
        this.toCity = toCity;
    }

    public String getJourneyDateStr() {
        return journeyDateStr;
    }

    public void setJourneyDateStr(String journeyDateStr) {
        this.journeyDateStr = journeyDateStr;
    }

    public Date getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(Date journeyDate) {
        this.journeyDate = journeyDate;
    }


    public List<Map<String, Object>> obtainRequestParams() {
        List<Map<String, Object>> cities = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        //到达城市
        params.put("DstCity", getToCity());
        //到达地三字码
        params.put("DstCityCode", getToThreeCode());
        //用来标识到达地三字码是城市三字码还是机场三字码，城市三字码：1, 机场三字码：2
        params.put("DstCityCodeType", 1);
        //出发日期
        params.put("DepDate", getJourneyDateStr());
        //出发城市
        params.put("OrgCity", getFromCity());
        //出发地三字码
        params.put("OrgCityCode", getFromThreeCode());
        //用来标识出发地三字码是城市三字码还是机场三字码，城市三字码：1, 机场三字码：2
        params.put("OrgCityCodeType", 1);
        cities.add(params);
        return cities;
    }


}
