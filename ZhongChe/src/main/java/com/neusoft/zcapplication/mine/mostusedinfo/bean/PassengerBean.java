package com.neusoft.zcapplication.mine.mostusedinfo.bean;

import java.util.List;

/**
 * 常用联系人信息类
 */

public class PassengerBean {
    private String nameCn;//中文名
    private String familyName;//英文姓
    private String lastName;//英文名
    private List<PsgCardBean> cardList;//证件类型列表

    public PassengerBean() {
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<PsgCardBean> getCardList() {
        return cardList;
    }

    public void setCardList(List<PsgCardBean> cardList) {
        this.cardList = cardList;
    }
}
