package com.neusoft.zcapplication.mine.mostusedinfo.bean;

/**
 * 常用联系人证件类型
 */

public class PsgCardBean {
    private int id;//证件类型id
    private String cardType;//证件类型
    private String cardNum;//号码
    private String cardDate;//证件有效期

    public PsgCardBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCardType() {
        return null == cardType ? "" :cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardNum() {
        return null == cardNum ? "" :cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getCardDate() {
        return null == cardDate ? "" :cardDate;
    }

    public void setCardDate(String cardDate) {
        this.cardDate = cardDate;
    }
}
