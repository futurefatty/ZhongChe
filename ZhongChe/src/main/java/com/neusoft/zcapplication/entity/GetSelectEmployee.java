package com.neusoft.zcapplication.entity;

import java.util.List;

/**
 * Author: TenzLiu
 * Time: 2018/8/15 14:35
 * Desc:已授权人员列表
 */

public class GetSelectEmployee {


    /**
     * mobil : 15918049008
     * birthday : 1987-01-28 00:00:00.0
     * employeeName : 陈岚君
     * documentInfo : [{"DOCUMENTID":1,"DOCUMENTINFO":"450595198808018420","SECONDNAME":"岚君","EMPLOYEECODE":"20110883","ID":51452,"DOCUMENTNAME":"身份证","FAMILYNAME":"22","EMPLOYEENAME":"陈岚君","ENDDATE":"2022-01-01"},{"DOCUMENTID":9,"DOCUMENTINFO":"13888","SECONDNAME":"lanjun","EMPLOYEECODE":"20110883","BEGINDATE":"2018-08-05","ID":51451,"DOCUMENTNAME":"公务护照","FAMILYNAME":"chen","EMPLOYEENAME":"陈蓝军","ENDDATE":"2018-08-31"},{"DOCUMENTID":6,"DOCUMENTINFO":"6464146641","SECONDNAME":"解决","EMPLOYEECODE":"20110883","ID":51459,"DOCUMENTNAME":"回乡证","FAMILYNAME":"号","EMPLOYEENAME":"陈岚君","ENDDATE":"2018-08-16"}]
     * gender : 2
     * mail : 1047867077@qq.com
     * nation : A15600
     * idCard : 140181198701284722
     * company : [{"companyCode":"01070200","companyName":"株洲中车时代电气股份有限公司"}]
     * employeeCode : 20110883
     */

    private String mobil;
    private String birthday;
    private String employeeName;
    private String gender;
    private String mail;
    private String nation;
    private String idCard;
    private String employeeCode;
    private List<DocumentInfoBean> documentInfo;
    private List<CompanyBean> company;

    public String getMobil() {
        return mobil;
    }

    public void setMobil(String mobil) {
        this.mobil = mobil;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public List<DocumentInfoBean> getDocumentInfo() {
        return documentInfo;
    }

    public void setDocumentInfo(List<DocumentInfoBean> documentInfo) {
        this.documentInfo = documentInfo;
    }

    public List<CompanyBean> getCompany() {
        return company;
    }

    public void setCompany(List<CompanyBean> company) {
        this.company = company;
    }

    public static class DocumentInfoBean {
        /**
         * DOCUMENTID : 1
         * DOCUMENTINFO : 450595198808018420
         * SECONDNAME : 岚君
         * EMPLOYEECODE : 20110883
         * ID : 51452
         * DOCUMENTNAME : 身份证
         * FAMILYNAME : 22
         * EMPLOYEENAME : 陈岚君
         * ENDDATE : 2022-01-01
         * BEGINDATE : 2018-08-05
         */

        private int DOCUMENTID;
        private String DOCUMENTINFO;
        private String SECONDNAME;
        private String EMPLOYEECODE;
        private int ID;
        private String DOCUMENTNAME;
        private String FAMILYNAME;
        private String EMPLOYEENAME;
        private String ENDDATE;
        private String BEGINDATE;

        public int getDOCUMENTID() {
            return DOCUMENTID;
        }

        public void setDOCUMENTID(int DOCUMENTID) {
            this.DOCUMENTID = DOCUMENTID;
        }

        public String getDOCUMENTINFO() {
            return DOCUMENTINFO;
        }

        public void setDOCUMENTINFO(String DOCUMENTINFO) {
            this.DOCUMENTINFO = DOCUMENTINFO;
        }

        public String getSECONDNAME() {
            return SECONDNAME;
        }

        public void setSECONDNAME(String SECONDNAME) {
            this.SECONDNAME = SECONDNAME;
        }

        public String getEMPLOYEECODE() {
            return EMPLOYEECODE;
        }

        public void setEMPLOYEECODE(String EMPLOYEECODE) {
            this.EMPLOYEECODE = EMPLOYEECODE;
        }

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getDOCUMENTNAME() {
            return DOCUMENTNAME;
        }

        public void setDOCUMENTNAME(String DOCUMENTNAME) {
            this.DOCUMENTNAME = DOCUMENTNAME;
        }

        public String getFAMILYNAME() {
            return FAMILYNAME;
        }

        public void setFAMILYNAME(String FAMILYNAME) {
            this.FAMILYNAME = FAMILYNAME;
        }

        public String getEMPLOYEENAME() {
            return EMPLOYEENAME;
        }

        public void setEMPLOYEENAME(String EMPLOYEENAME) {
            this.EMPLOYEENAME = EMPLOYEENAME;
        }

        public String getENDDATE() {
            return ENDDATE;
        }

        public void setENDDATE(String ENDDATE) {
            this.ENDDATE = ENDDATE;
        }

        public String getBEGINDATE() {
            return BEGINDATE;
        }

        public void setBEGINDATE(String BEGINDATE) {
            this.BEGINDATE = BEGINDATE;
        }
    }

    public static class CompanyBean {
        /**
         * companyCode : 01070200
         * companyName : 株洲中车时代电气股份有限公司
         */

        private String companyCode;
        private String companyName;
        private String unitName;
        private boolean isCheck;

        public String getCompanyCode() {
            return companyCode;
        }

        public void setCompanyCode(String companyCode) {
            this.companyCode = companyCode;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }
    }
}
