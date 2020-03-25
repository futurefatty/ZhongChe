package com.neusoft.zcapplication.flight.internation.model;

import java.io.Serializable;

/**
 * author:Six
 * Date:2019/5/30
 */
public class InternationLowerPriceModel implements Serializable {


    /**
     * orgChildTicketPrice : 0
     * adultTicketPrice : 2350
     * childTicketPrice : 0
     * orgAdultTicketPrice : 2350
     * childTaxPrice : 0
     * adultTaxPrice : 245
     * orgChildTaxPrice : 0
     * orgAdultTaxPrice : 245
     */

    private int orgChildTicketPrice;
    private int adultTicketPrice;
    private int childTicketPrice;
    private int orgAdultTicketPrice;
    private int childTaxPrice;
    private int adultTaxPrice;
    private int orgChildTaxPrice;
    private int orgAdultTaxPrice;

    public int getOrgChildTicketPrice() {
        return orgChildTicketPrice;
    }

    public void setOrgChildTicketPrice(int orgChildTicketPrice) {
        this.orgChildTicketPrice = orgChildTicketPrice;
    }

    public int getAdultTicketPrice() {
        return adultTicketPrice;
    }

    public void setAdultTicketPrice(int adultTicketPrice) {
        this.adultTicketPrice = adultTicketPrice;
    }

    public int getChildTicketPrice() {
        return childTicketPrice;
    }

    public void setChildTicketPrice(int childTicketPrice) {
        this.childTicketPrice = childTicketPrice;
    }

    public int getOrgAdultTicketPrice() {
        return orgAdultTicketPrice;
    }

    public void setOrgAdultTicketPrice(int orgAdultTicketPrice) {
        this.orgAdultTicketPrice = orgAdultTicketPrice;
    }

    public int getChildTaxPrice() {
        return childTaxPrice;
    }

    public void setChildTaxPrice(int childTaxPrice) {
        this.childTaxPrice = childTaxPrice;
    }

    public int getAdultTaxPrice() {
        return adultTaxPrice;
    }

    public void setAdultTaxPrice(int adultTaxPrice) {
        this.adultTaxPrice = adultTaxPrice;
    }

    public int getOrgChildTaxPrice() {
        return orgChildTaxPrice;
    }

    public void setOrgChildTaxPrice(int orgChildTaxPrice) {
        this.orgChildTaxPrice = orgChildTaxPrice;
    }

    public int getOrgAdultTaxPrice() {
        return orgAdultTaxPrice;
    }

    public void setOrgAdultTaxPrice(int orgAdultTaxPrice) {
        this.orgAdultTaxPrice = orgAdultTaxPrice;
    }
}
