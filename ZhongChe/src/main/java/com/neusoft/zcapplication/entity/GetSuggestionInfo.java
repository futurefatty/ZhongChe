package com.neusoft.zcapplication.entity;

/**
 * Author: TenzLiu
 * Time: 2018/6/22 9:52
 * Desc:获取问题分类
 */

public class GetSuggestionInfo {

    /**
     * name : 页面出错
     * id : 1
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
