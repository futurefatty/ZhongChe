package com.neusoft.zcapplication.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Author: TenzLiu
 * Time: 2018/7/6 10:20
 * Desc: 公务用车
 */

public class GetAllApplyUseCars implements Serializable {


    /**
     * id : 46
     * carApplyId : CL2018071208325
     * applicatId : 20110883
     * applicatName : 陈岚君
     * mobile : 18373399973
     * email : null
     * unitCode : 01070200
     * applicationDate : 1531358457000
     * carType : null
     * numberPeople : 1
     * startTime : 1531358400000
     * endTime : 1531444800000
     * carRange : 长株潭外
     * applyReason : 1
     * auditPublicStatus : 0
     * auditDate : null
     * conductorId : null
     * toMessage : 0
     * supplierId : null
     * createDate : 1531358457000
     * modifyDate : null
     * deleteFlag : N
     * auditId : 36
     * auditLevel : 1
     * auditorId : 20040020
     * auditorName : 易卫华
     * auditStatus : 0
     */

    private int id;
    private String carApplyId;
    private String applicatId;
    private String applicatName;
    private String mobile;
    private String email;
    private String unitCode;
    private String unitName;
    private String accountingSubjectCode;
    private String accountingSubjectName;
    private long applicationDate;
    private String carType;
    private String numberPeople;
    private long startTime;
    private long endTime;
    private String carRange;
    private String applyReason;
    private int auditPublicStatus;
    private String auditDate;
    private String conductorId;
    private String toMessage;
    private String supplierId;
    private long createDate;
    private String modifyDate;
    private String deleteFlag;
    private int auditId;
    private String auditLevel;
    private String auditorId;
    private String auditorName;
    private String conductorName;
    private int auditStatus;//0：待审批 11:审批中（部门审批同意） 12：审批拒绝（部门审批拒绝）
    // 21：已审批（总经办审批同意） 22：审批拒绝（总经办审批拒绝） 3/31/32：已办理
    private String unitAdvice;
    private String gmAdvice;
    private String conductAdvice;
    private String typeCode;
    private String rangeCode;
    private int carNumber;
    private String userName;
    private String userMobile;
    private int importantCustomer;
    private String customerLevelJob;
    private boolean isCheck;
    private List<CarAuditEntity> carAuditEntities;

    public List<CarAuditEntity> getCarAuditEntities() {
        return carAuditEntities;
    }

    public void setCarAuditEntities(List<CarAuditEntity> carAuditEntities) {
        this.carAuditEntities = carAuditEntities;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCarApplyId() {
        return carApplyId;
    }

    public void setCarApplyId(String carApplyId) {
        this.carApplyId = carApplyId;
    }

    public String getApplicatId() {
        return applicatId;
    }

    public void setApplicatId(String applicatId) {
        this.applicatId = applicatId;
    }

    public String getApplicatName() {
        return applicatName;
    }

    public void setApplicatName(String applicatName) {
        this.applicatName = applicatName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getAccountingSubjectCode() {
        return accountingSubjectCode;
    }

    public void setAccountingSubjectCode(String accountingSubjectCode) {
        this.accountingSubjectCode = accountingSubjectCode;
    }

    public String getAccountingSubjectName() {
        return accountingSubjectName;
    }

    public void setAccountingSubjectName(String accountingSubjectName) {
        this.accountingSubjectName = accountingSubjectName;
    }

    public long getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(long applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getNumberPeople() {
        return numberPeople;
    }

    public void setNumberPeople(String numberPeople) {
        this.numberPeople = numberPeople;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getCarRange() {
        return carRange;
    }

    public void setCarRange(String carRange) {
        this.carRange = carRange;
    }

    public String getApplyReason() {
        return applyReason;
    }

    public void setApplyReason(String applyReason) {
        this.applyReason = applyReason;
    }

    public int getAuditPublicStatus() {
        return auditPublicStatus;
    }

    public void setAuditPublicStatus(int auditPublicStatus) {
        this.auditPublicStatus = auditPublicStatus;
    }

    public String getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(String auditDate) {
        this.auditDate = auditDate;
    }

    public String getConductorId() {
        return conductorId;
    }

    public void setConductorId(String conductorId) {
        this.conductorId = conductorId;
    }

    public String getToMessage() {
        return toMessage;
    }

    public void setToMessage(String toMessage) {
        this.toMessage = toMessage;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public int getAuditId() {
        return auditId;
    }

    public void setAuditId(int auditId) {
        this.auditId = auditId;
    }

    public String getAuditLevel() {
        return auditLevel;
    }

    public void setAuditLevel(String auditLevel) {
        this.auditLevel = auditLevel;
    }

    public String getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(String auditorId) {
        this.auditorId = auditorId;
    }

    public String getAuditorName() {
        return auditorName;
    }

    public void setAuditorName(String auditorName) {
        this.auditorName = auditorName;
    }

    public String getConductorName() {
        return conductorName;
    }

    public void setConductorName(String conductorName) {
        this.conductorName = conductorName;
    }

    public int getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(int auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getUnitAdvice() {
        return unitAdvice;
    }

    public void setUnitAdvice(String unitAdvice) {
        this.unitAdvice = unitAdvice;
    }

    public String getGmAdvice() {
        return gmAdvice;
    }

    public void setGmAdvice(String gmAdvice) {
        this.gmAdvice = gmAdvice;
    }

    public String getConductAdvice() {
        return conductAdvice;
    }

    public void setConductAdvice(String conductAdvice) {
        this.conductAdvice = conductAdvice;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getRangeCode() {
        return rangeCode;
    }

    public void setRangeCode(String rangeCode) {
        this.rangeCode = rangeCode;
    }

    public int getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(int carNumber) {
        this.carNumber = carNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public int getImportantCustomer() {
        return importantCustomer;
    }

    public void setImportantCustomer(int importantCustomer) {
        this.importantCustomer = importantCustomer;
    }

    public String getCustomerLevelJob() {
        return customerLevelJob;
    }

    public void setCustomerLevelJob(String customerLevelJob) {
        this.customerLevelJob = customerLevelJob;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }


    public static class CarAuditEntity implements Serializable {

        /**
         * id : 809
         * publicAuditId : 546
         * auditLevel : 1
         * auditorId : 20018074
         * auditorName : 卢飞
         * auditAdvice : 同意
         * auditStatus : 1
         * auditTime : 1538211041000
         * createDate : 1538204009000
         * modifyDate : 1538211041000
         * deleteFlag : N
         * auditFlag : Y
         */

        private int id;
        private int publicAuditId;
        private String auditLevel;
        private String auditorId;
        private String auditorName;
        private String auditAdvice;
        private int auditStatus;
        private long auditTime;
        private long createDate;
        private long modifyDate;
        private String deleteFlag;
        private String auditFlag;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPublicAuditId() {
            return publicAuditId;
        }

        public void setPublicAuditId(int publicAuditId) {
            this.publicAuditId = publicAuditId;
        }

        public String getAuditLevel() {
            return auditLevel;
        }

        public void setAuditLevel(String auditLevel) {
            this.auditLevel = auditLevel;
        }

        public String getAuditorId() {
            return auditorId;
        }

        public void setAuditorId(String auditorId) {
            this.auditorId = auditorId;
        }

        public String getAuditorName() {
            return auditorName;
        }

        public void setAuditorName(String auditorName) {
            this.auditorName = auditorName;
        }

        public String getAuditAdvice() {
            return auditAdvice;
        }

        public void setAuditAdvice(String auditAdvice) {
            this.auditAdvice = auditAdvice;
        }

        public int getAuditStatus() {
            return auditStatus;
        }

        public void setAuditStatus(int auditStatus) {
            this.auditStatus = auditStatus;
        }

        public long getAuditTime() {
            return auditTime;
        }

        public void setAuditTime(long auditTime) {
            this.auditTime = auditTime;
        }

        public long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }

        public long getModifyDate() {
            return modifyDate;
        }

        public void setModifyDate(long modifyDate) {
            this.modifyDate = modifyDate;
        }

        public String getDeleteFlag() {
            return deleteFlag;
        }

        public void setDeleteFlag(String deleteFlag) {
            this.deleteFlag = deleteFlag;
        }

        public String getAuditFlag() {
            return auditFlag;
        }

        public void setAuditFlag(String auditFlag) {
            this.auditFlag = auditFlag;
        }
    }
}
