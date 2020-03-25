package com.neusoft.zcapplication.entity;

/**
 * Author: TenzLiu
 * Time: 2018/6/19 11:15
 * Desc:员工获取信息
 */

public class GetEmployeeInfo {


    /**
     * unitName : 北京重工技术中心电气技术开发部
     * EMPLOYEE_NAME : 苏冲
     * applicateId : 20110883
     * state : 0
     * EMPLOYEE_CODE : 20110833
     */

    private String unitName;
    private String EMPLOYEE_NAME;
    private String applicateId;
    private int state;
    private String EMPLOYEE_CODE;
    private boolean isCheck;

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getEMPLOYEE_NAME() {
        return EMPLOYEE_NAME;
    }

    public void setEMPLOYEE_NAME(String EMPLOYEE_NAME) {
        this.EMPLOYEE_NAME = EMPLOYEE_NAME;
    }

    public String getApplicateId() {
        return applicateId;
    }

    public void setApplicateId(String applicateId) {
        this.applicateId = applicateId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getEMPLOYEE_CODE() {
        return EMPLOYEE_CODE;
    }

    public void setEMPLOYEE_CODE(String EMPLOYEE_CODE) {
        this.EMPLOYEE_CODE = EMPLOYEE_CODE;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
