package com.neusoft.zcapplication.entity;

/**
 * Author: TenzLiu
 * Time: 2018/6/26 10:46
 * Desc:证件类型
 */

public class GetDocumentType {

    /**
     * name : 外交护照
     * id : 10
     */

    private String name;
    private int id;
    private boolean isCheck;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
