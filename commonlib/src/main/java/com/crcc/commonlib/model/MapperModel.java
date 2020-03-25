package com.crcc.commonlib.model;

import java.io.Serializable;

/**
 * author:Six
 * Date:2019/5/31
 */
public class MapperModel<T> implements Serializable {
    private CharSequence key;
    private CharSequence value;
    private T tag;

    public CharSequence getKey() {
        return key;
    }

    public void setKey(CharSequence key) {
        this.key = key;
    }

    public CharSequence getValue() {
        return value;
    }

    public void setValue(CharSequence value) {
        this.value = value;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public void setValue(String value) {
        this.value = value;
    }

    public T getTag() {
        return tag;
    }

    public void setTag(T tag) {
        this.tag = tag;
    }
}
