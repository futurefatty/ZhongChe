package com.neusoft.zcapplication.flight.internation.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * author:Six
 * Date:2019/5/31
 */
public class InternationJourneyFlowModel implements Serializable, MultiItemEntity {
    //非中转
    public static final int NOT_TRANSFER = 1;
    //中转布局
    public static final int TRANSFER = 2;

    private String marketingAirline;
    private String cabinClass;
    private String depTerm;
    private String marketingFlightNo;
    private String arrCity;
    private String depCity;
    private String cabin;
    private String duration;
    private String aircraftType;


    private String cabinClassName;
    private String depCityName;
    private String arrAirport;
    private String arrAirportName;
    private String arrTerm;
    private String marketingCompanyName;
    private String operatingFlightNo;
    private String aircraftName;
    private String stopCity;
    private String arrCityName;
    private String depTime;
    private String segmentCode;
    private String cabinNumber;
    private String depAirport;
    private String stopCityName;
    private String meal;
    private String operatingCompanyLogo;
    private String depAirportName;
    private String arrTime;
    private String marketingCompanyLogo;

    //格式->中转    ${城市}   ${x小时x分}
    private String transferName;


    public String getTransferName() {
        return transferName;
    }

    public void setTransferName(String transferName) {
        this.transferName = transferName;
    }

    public String getMarketingAirline() {
        return marketingAirline;
    }

    public void setMarketingAirline(String marketingAirline) {
        this.marketingAirline = marketingAirline;
    }

    public String getCabinClass() {
        return cabinClass;
    }

    public void setCabinClass(String cabinClass) {
        this.cabinClass = cabinClass;
    }

    public String getDepTerm() {
        return depTerm;
    }

    public void setDepTerm(String depTerm) {
        this.depTerm = depTerm;
    }

    public String getMarketingFlightNo() {
        return marketingFlightNo;
    }

    public void setMarketingFlightNo(String marketingFlightNo) {
        this.marketingFlightNo = marketingFlightNo;
    }

    public String getArrCity() {
        return arrCity;
    }

    public void setArrCity(String arrCity) {
        this.arrCity = arrCity;
    }

    public String getDepCity() {
        return depCity;
    }

    public void setDepCity(String depCity) {
        this.depCity = depCity;
    }

    public String getCabin() {
        return cabin;
    }

    public void setCabin(String cabin) {
        this.cabin = cabin;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(String aircraftType) {
        this.aircraftType = aircraftType;
    }

    public String getCabinClassName() {
        return cabinClassName;
    }

    public void setCabinClassName(String cabinClassName) {
        this.cabinClassName = cabinClassName;
    }

    public String getDepCityName() {
        return depCityName;
    }

    public void setDepCityName(String depCityName) {
        this.depCityName = depCityName;
    }

    public String getArrAirport() {
        return arrAirport;
    }

    public void setArrAirport(String arrAirport) {
        this.arrAirport = arrAirport;
    }

    public String getArrAirportName() {
        return arrAirportName;
    }

    public void setArrAirportName(String arrAirportName) {
        this.arrAirportName = arrAirportName;
    }

    public String getArrTerm() {
        return arrTerm;
    }

    public void setArrTerm(String arrTerm) {
        this.arrTerm = arrTerm;
    }

    public String getMarketingCompanyName() {
        return marketingCompanyName;
    }

    public void setMarketingCompanyName(String marketingCompanyName) {
        this.marketingCompanyName = marketingCompanyName;
    }

    public String getOperatingFlightNo() {
        return operatingFlightNo;
    }

    public void setOperatingFlightNo(String operatingFlightNo) {
        this.operatingFlightNo = operatingFlightNo;
    }

    public String getAircraftName() {
        return aircraftName;
    }

    public void setAircraftName(String aircraftName) {
        this.aircraftName = aircraftName;
    }

    public String getStopCity() {
        return stopCity;
    }

    public void setStopCity(String stopCity) {
        this.stopCity = stopCity;
    }

    public String getArrCityName() {
        return arrCityName;
    }

    public void setArrCityName(String arrCityName) {
        this.arrCityName = arrCityName;
    }

    public String getDepTime() {
        return depTime;
    }

    public void setDepTime(String depTime) {
        this.depTime = depTime;
    }

    public String getSegmentCode() {
        return segmentCode;
    }

    public void setSegmentCode(String segmentCode) {
        this.segmentCode = segmentCode;
    }

    public String getCabinNumber() {
        return cabinNumber;
    }

    public void setCabinNumber(String cabinNumber) {
        this.cabinNumber = cabinNumber;
    }

    public String getDepAirport() {
        return depAirport;
    }

    public void setDepAirport(String depAirport) {
        this.depAirport = depAirport;
    }

    public String getStopCityName() {
        return stopCityName;
    }

    public void setStopCityName(String stopCityName) {
        this.stopCityName = stopCityName;
    }

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public String getOperatingCompanyLogo() {
        return operatingCompanyLogo;
    }

    public void setOperatingCompanyLogo(String operatingCompanyLogo) {
        this.operatingCompanyLogo = operatingCompanyLogo;
    }

    public String getDepAirportName() {
        return depAirportName;
    }

    public void setDepAirportName(String depAirportName) {
        this.depAirportName = depAirportName;
    }

    public String getArrTime() {
        return arrTime;
    }

    public void setArrTime(String arrTime) {
        this.arrTime = arrTime;
    }

    public String getMarketingCompanyLogo() {
        return marketingCompanyLogo;
    }

    public void setMarketingCompanyLogo(String marketingCompanyLogo) {
        this.marketingCompanyLogo = marketingCompanyLogo;
    }

    private int itemType;

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
