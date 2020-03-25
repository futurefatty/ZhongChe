package com.neusoft.zcapplication.entity;

import java.io.Serializable;

/**
 * Author: TenzLiu
 * Time: 2018/7/10 15:42
 * Desc:  公务用车类型
 */

public class GetAllCarType implements Serializable {

    private String typeCode;
    private String typeName;

    public GetAllCarType() {
    }

    public GetAllCarType(String typeCode, String typeName) {
        this.typeCode = typeCode;
        this.typeName = typeName;
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

}
