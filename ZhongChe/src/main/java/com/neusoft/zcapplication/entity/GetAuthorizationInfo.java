package com.neusoft.zcapplication.entity;

/**
 * Author: TenzLiu
 * Time: 2018/6/19 10:46
 * Desc: 授权人员
 */

public class GetAuthorizationInfo {


    /**
     * employeeName : 贺静颖
     * unitName : 株洲所研究院办公室
     * idCard : null
     * expiryTime : 2019-06-04
     * id : 829
     * employeeCode : 20010066
     */

    private String employeeName;
    private String unitName;
    private Object idCard;
    private String expiryTime;
    private int id;
    private String employeeCode;

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Object getIdCard() {
        return idCard;
    }

    public void setIdCard(Object idCard) {
        this.idCard = idCard;
    }

    public String getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(String expiryTime) {
        this.expiryTime = expiryTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }
}
