package com.neusoft.zcapplication.entity;

import java.io.Serializable;

/**
 * Author: TenzLiu
 * Time: 2018/10/8 10:10
 * Desc:建议_获取当前用户反馈详情
 */

public class QueryFeebackDetail implements Serializable {


    /**
     * replyMail : 1047867077@qq.com
     * size : 1
     * pId : 338
     * id : 339
     * detail : 测试回复
     * replyEmployeeCode : 20110883
     * contracts : 陈岚君
     */

    private String replyMail;
    private int size;
    private String pId;
    private String id;
    private String detail;
    private String replyEmployeeCode;
    private String contracts;

    public String getReplyMail() {
        return replyMail;
    }

    public void setReplyMail(String replyMail) {
        this.replyMail = replyMail;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getPId() {
        return pId;
    }

    public void setPId(String pId) {
        this.pId = pId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getReplyEmployeeCode() {
        return replyEmployeeCode;
    }

    public void setReplyEmployeeCode(String replyEmployeeCode) {
        this.replyEmployeeCode = replyEmployeeCode;
    }

    public String getContracts() {
        return contracts;
    }

    public void setContracts(String contracts) {
        this.contracts = contracts;
    }
}
