package com.neusoft.zcapplication.entity;

import java.io.Serializable;

/**
 * Author: TenzLiu
 * Time: 2018/7/10 15:41
 * Desc: 公务用车范围
 */

public class GetAllCarRange implements Serializable {

    private String rangeCode;
    private String rangeName;

    public GetAllCarRange() {
    }

    public GetAllCarRange(String rangeCode, String rangeName) {
        this.rangeCode = rangeCode;
        this.rangeName = rangeName;
    }

    public String getRangeCode() {
        return rangeCode;
    }

    public void setRangeCode(String rangeCode) {
        this.rangeCode = rangeCode;
    }

    public String getRangeName() {
        return rangeName;
    }

    public void setRangeName(String rangeName) {
        this.rangeName = rangeName;
    }
}
