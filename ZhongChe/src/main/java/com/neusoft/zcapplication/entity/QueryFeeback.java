package com.neusoft.zcapplication.entity;

import java.io.Serializable;

/**
 * Author: TenzLiu
 * Time: 2018/10/8 10:10
 * Desc:建议_获取当前用户反馈
 */

public class QueryFeeback implements Serializable {


    /**
     * suggestionType : 建议
     * supperType : 美亚
     * contractsInfo : 19976932809
     * createTime : 2018-09-30 16:18:09
     * pid : null
     * detail : wwww
     * id : 338
     * contracts : 陈岚君
     */

    private String suggestionType;
    private String supperType;
    private String contractsInfo;
    private String createTime;
    private String pid;
    private String detail;
    private int id;
    private String contracts;

    public String getSuggestionType() {
        return suggestionType;
    }

    public void setSuggestionType(String suggestionType) {
        this.suggestionType = suggestionType;
    }

    public String getSupperType() {
        return supperType;
    }

    public void setSupperType(String supperType) {
        this.supperType = supperType;
    }

    public String getContractsInfo() {
        return contractsInfo;
    }

    public void setContractsInfo(String contractsInfo) {
        this.contractsInfo = contractsInfo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContracts() {
        return contracts;
    }

    public void setContracts(String contracts) {
        this.contracts = contracts;
    }
}
