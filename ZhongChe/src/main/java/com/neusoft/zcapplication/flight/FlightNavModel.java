package com.neusoft.zcapplication.flight;

import java.io.Serializable;
import java.util.Comparator;

/**
 * author:Six
 * Date:2019/5/28
 * 底部tab 导航model
 */
public class FlightNavModel implements Serializable {
    private int unResId;
    private int selectResId;
    private String nomalText;
    private String[] selectTexts;
    private int changePositionStatus;
    private String requestKey;
    private Object requestValue;
    private Comparator[] comparators;

    public Comparator[] getComparators() {
        return comparators;
    }

    public void setComparators(Comparator[] comparators) {
        this.comparators = comparators;
    }

    public String getNomalText() {
        return nomalText;
    }

    public void setNomalText(String nomalText) {
        this.nomalText = nomalText;
    }

    public String[] getSelectTexts() {
        return selectTexts;
    }

    public void setSelectTexts(String[] selectTexts) {
        this.selectTexts = selectTexts;
    }


    public int getUnResId() {
        return unResId;
    }

    public void setUnResId(int unResId) {
        this.unResId = unResId;
    }

    public int getSelectResId() {
        return selectResId;
    }

    public void setSelectResId(int selectResId) {
        this.selectResId = selectResId;
    }

    public String getRequestKey() {
        return requestKey;
    }

    public void setRequestKey(String requestKey) {
        this.requestKey = requestKey;
    }

    public Object getRequestValue() {
        return requestValue;
    }

    public void setRequestValue(Object requestValue) {
        this.requestValue = requestValue;
    }

    public int getChangePositionStatus() {
        return changePositionStatus;
    }

    public void setChangePositionStatus(int changePositionStatus) {
        this.changePositionStatus = changePositionStatus;
    }
}
