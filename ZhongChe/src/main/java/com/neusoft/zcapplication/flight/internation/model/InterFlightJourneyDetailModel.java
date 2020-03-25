package com.neusoft.zcapplication.flight.internation.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * author:Six
 * Date:2019/5/30
 */
public class InterFlightJourneyDetailModel implements Serializable {


    /**
     * serviceFee : 0
     * serviceFeeType : 1
     * bookingTips : ["短期签证：持商务、旅游等短期签证的旅客，建议您直接购买往返程票或持有返程票，以免无法办理乘机和出境手续。","若您未乘坐前段航班，将自行承担后段航班无法登机的所有风险和责任。","中国大陆籍乘客使用港澳通行证、台湾通行证乘坐国际联程的中国境内段航班，可能需同时提供身份证或机场公安机关签发的《乘坐民航飞机临时身份证明》，具体以机场为准。 "]
     * baggageRule : 退票/改签规则以航空公司为准， 如需了解具体内容。请拨打客服热线：0571-82598555咨询
     * refundTicketRule : 退票/改签规则以航空公司为准， 如需了解具体内容。请拨打客服热线：0571-82598555咨询
     * modifyTicketRule : 退票/改签规则以航空公司为准， 如需了解具体内容。请拨打客服热线：0571-82598555咨询
     */

    private BigDecimal serviceFee;
    private int serviceFeeType;
    private String baggageRule;
    private String refundTicketRule;
    private String modifyTicketRule;
    private List<String> bookingTips;


    private BigDecimal adultTicketPrice;
    private BigDecimal totalPrice;
    private BigDecimal adultTaxPrice;
    private BigDecimal totalServiceFee;

    public BigDecimal getTotalServiceFee() {
        return totalServiceFee;
    }

    public void setTotalServiceFee(BigDecimal totalServiceFee) {
        this.totalServiceFee = totalServiceFee;
    }

    public int getServiceFeeType() {
        return serviceFeeType;
    }

    public void setServiceFeeType(int serviceFeeType) {
        this.serviceFeeType = serviceFeeType;
    }

    public String getBaggageRule() {
        return baggageRule;
    }

    public void setBaggageRule(String baggageRule) {
        this.baggageRule = baggageRule;
    }

    public String getRefundTicketRule() {
        return refundTicketRule;
    }

    public void setRefundTicketRule(String refundTicketRule) {
        this.refundTicketRule = refundTicketRule;
    }

    public String getModifyTicketRule() {
        return modifyTicketRule;
    }

    public void setModifyTicketRule(String modifyTicketRule) {
        this.modifyTicketRule = modifyTicketRule;
    }

    public List<String> getBookingTips() {
        return bookingTips;
    }

    public void setBookingTips(List<String> bookingTips) {
        this.bookingTips = bookingTips;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public BigDecimal getAdultTicketPrice() {
        return adultTicketPrice;
    }

    public void setAdultTicketPrice(BigDecimal adultTicketPrice) {
        this.adultTicketPrice = adultTicketPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getAdultTaxPrice() {
        return adultTaxPrice;
    }

    public void setAdultTaxPrice(BigDecimal adultTaxPrice) {
        this.adultTaxPrice = adultTaxPrice;
    }
}
