package com.neusoft.zcapplication.entity;

/**
 * Author: TenzLiu
 * Time: 2018/6/7 16:07
 * Desc: 获取App版本更新
 */

public class GetVersion {


    /**
     * dateTime : 2017-12-26 11:00:00
     * id : 1
     * type : 1
     * version : 2
     */

    private String dateTime;
    private String id;
    private String type;
    private String version;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
