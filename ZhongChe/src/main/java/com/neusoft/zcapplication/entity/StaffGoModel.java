package com.neusoft.zcapplication.entity;

public class StaffGoModel implements IStaffGoModel {


    /**
     * fromDate : 2018-10-07 00:00:00
     * companyCode : 1000
     * toCityName : 长沙
     * employeeName : 陈岚君
     * carrierName : 南方航空
     * phone : 28494745-803
     * toDate : 2018-10-07 00:00:00
     * fromCityName : 广州
     * fromCityId : CAN
     * accountEntiyt : 株洲中车时代电气股份有限公司
     * toCityId : CSX
     */

    private String fromPlanDate;
    private String toPlanDate;
    private String companyCode;
    private String toCityName;
    private String employeeName;
    private String carrierName;
    private String mobil;
    private String fromCityName;
    private String fromCityId;
    private String accountEntiyt;
    private String toCityId;
    private String flightNo;

    @Override
    public String getFromPlanDate() {
        return fromPlanDate;
    }

    @Override
    public void setFromPlanDate(String fromPlanDate) {
        this.fromPlanDate = fromPlanDate;
    }

    @Override
    public String getToPlanDate() {
        return toPlanDate;
    }

    @Override
    public void setToPlanDate(String toPlanDate) {
        this.toPlanDate = toPlanDate;
    }

    @Override
    public String getCompanyCode() {
        return companyCode;
    }

    @Override
    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    @Override
    public String getToCityName() {
        return toCityName;
    }

    @Override
    public void setToCityName(String toCityName) {
        this.toCityName = toCityName;
    }

    @Override
    public String getEmployeeName() {
        return employeeName;
    }

    @Override
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    @Override
    public String getCarrierName() {
        return carrierName;
    }

    @Override
    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    @Override
    public String getMobil() {
        return mobil;
    }

    @Override
    public void setMobil(String mobil) {
        this.mobil = mobil;
    }

    @Override
    public String getFromCityName() {
        return fromCityName;
    }

    @Override
    public void setFromCityName(String fromCityName) {
        this.fromCityName = fromCityName;
    }

    @Override
    public String getFromCityId() {
        return fromCityId;
    }

    @Override
    public void setFromCityId(String fromCityId) {
        this.fromCityId = fromCityId;
    }

    @Override
    public String getAccountEntiyt() {
        return accountEntiyt;
    }

    @Override
    public void setAccountEntiyt(String accountEntiyt) {
        this.accountEntiyt = accountEntiyt;
    }

    @Override
    public String getToCityId() {
        return toCityId;
    }

    @Override
    public void setToCityId(String toCityId) {
        this.toCityId = toCityId;
    }

    @Override
    public String getFlightNo() {
        return flightNo;
    }

    @Override
    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }
}