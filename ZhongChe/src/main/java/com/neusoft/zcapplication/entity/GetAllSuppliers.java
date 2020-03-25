package com.neusoft.zcapplication.entity;

import java.io.Serializable;

/**
 * Author: TenzLiu
 * Time: 2018/7/6 11:24
 * Desc: 公务用车供应商
 */

public class GetAllSuppliers implements Serializable {

    /**
     * id : 1
     * supplierName : 用车供应商001
     * concactName : 陈先生
     * mobile : 15692014047
     * email : su_yh@neusoft.com
     * createDate : 1530857571000
     * modifyDate : 1530857571000
     * deleteFlag : N
     */

    private int id;
    private String supplierName;
    private String contactName;
    private String mobile;
    private String email;
    private long createDate;
    private long modifyDate;
    private String deleteFlag;
    private boolean isCheck;
    private GetAllCarTypeBySupplierId mGetAllCarTypeBySupplierId;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
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

    public GetAllCarTypeBySupplierId getGetAllCarTypeBySupplierId() {
        return mGetAllCarTypeBySupplierId;
    }

    public void setGetAllCarTypeBySupplierId(GetAllCarTypeBySupplierId getAllCarTypeBySupplierId) {
        mGetAllCarTypeBySupplierId = getAllCarTypeBySupplierId;
    }

    public static class GetAllCarTypeBySupplierId implements Serializable{

        /**
         * id : 1
         * supplierId : 21
         * typeCode : 1001
         * typeName : 5座小车
         * createDate : 1533196669000
         * modifyDate : 1533196669000
         * deleteFlag : N
         */

        private int id;
        private int supplierId;
        private String typeCode;
        private String typeName;
        private long createDate;
        private long modifyDate;
        private String deleteFlag;
        private boolean isCheck;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSupplierId() {
            return supplierId;
        }

        public void setSupplierId(int supplierId) {
            this.supplierId = supplierId;
        }

        public String getTypeCode() {
            return typeCode;
        }

        public void setTypeCode(String typeCode) {
            this.typeCode = typeCode;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
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

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }
    }

}
