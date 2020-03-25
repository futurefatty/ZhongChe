package com.neusoft.zcapplication.entity;

import java.io.Serializable;

/**
 * Author: TenzLiu
 * Time: 2018/6/26 14:09
 * Desc:用户的证件类型
 */

public class GetCredentialsData implements Serializable{

    /**
     * employeeName : 陈岚君
     * beginDate : null
     * documentInfo : 22222
     * endDate : 2018-08-01
     * familyName : null
     * documentId : 9
     * documentName : 公务护照
     * id : 51415
     * employeeCode : 20110883
     * secondName : 22
     */

    private String employeeName;
    private String beginDate;
    private String documentInfo;
    private String endDate;
    private String familyName;
    private int documentId;
    private String documentName;
    private int id;
    private String employeeCode;
    private String secondName;

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getDocumentInfo() {
        return documentInfo;
    }

    public void setDocumentInfo(String documentInfo) {
        this.documentInfo = documentInfo;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
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

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }
}
