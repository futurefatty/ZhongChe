package com.neusoft.zcapplication.Bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 出行人员实体类
 */

public class PersonItem implements Serializable{
    private String name;//姓名
    private String employeeCode;//员工编号
    private String idNo;//身份证
    private String idcard;//身份证
    private String mobile;//手机号码
    private String accountEntity;//核算主体
    private boolean isShowDel;//是否显示删除图标
    private boolean isShowItem;//是否显示Item
    private List<Map<String,Object>> accountEntityList;
    private  List<Map<String,Object>> credentialsInfo ;//证件类型
    private String companyCode;
    private String unitName;
    private String unitCode;
    private String position;//人员等级
    private String projectLeaderEmployeeCode;//项目经理员工编号
    private String projectLeaderName;//项目经理员工名称

    public String getProjectLeaderName() {
        return projectLeaderName;
    }

    public void setProjectLeaderName(String projectLeaderName) {
        this.projectLeaderName = projectLeaderName;
    }



    public String getProjectLeaderEmployeeCode() {
        return projectLeaderEmployeeCode;
    }

    public void setProjectLeaderEmployeeCode(String projectLeaderEmployeeCode) {
        this.projectLeaderEmployeeCode = projectLeaderEmployeeCode;
    }



    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }



    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAccountEntity() {
        return accountEntity;
    }

    public void setAccountEntity(String accountEntity) {
        this.accountEntity = accountEntity;
    }

    public boolean isShowDel() {
        return isShowDel;
    }

    public void setShowDel(boolean showDel) {
        isShowDel = showDel;
    }

    public boolean isShowItem() {
        return isShowItem;
    }

    public void setShowItem(boolean showItem) {
        isShowItem = showItem;
    }

    public List<Map<String, Object>> getAccountEntityList() {
        return accountEntityList;
    }

    public void setAccountEntityList(List<Map<String, Object>> accountEntityList) {
        this.accountEntityList = accountEntityList;
    }

    public List<Map<String, Object>> getCredentialsInfo() {
        return credentialsInfo;
    }

    public void setCredentialsInfo(List<Map<String, Object>> credentialsInfo) {
        this.credentialsInfo = credentialsInfo;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }
}
