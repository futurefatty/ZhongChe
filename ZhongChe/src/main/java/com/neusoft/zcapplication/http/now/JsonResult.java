package com.neusoft.zcapplication.http.now;

import com.google.gson.reflect.TypeToken;

import java.io.Serializable;

/**
 * json解析
 *
 * @param <T>
 */
public class JsonResult<T> implements Serializable {

    public String code;
    public String codeMsg;
    public T data;

    public JsonResult() {
    }

    public JsonResult(String code, String codeMsg, T data) {
        this.code = code;
        this.codeMsg = codeMsg;
        this.data = data;
    }

    /**
     * 由字符串构造
     *
     * @param value
     * @return
     */
    public static JsonResult fromString(String value) {
        if (value == null) {
            return null;
        }
        JsonResult result = JsonUtil.fromJson(value, JsonResult.class);
        return result;
    }

    public <T> T get(Class<T> clazz) {
        return JsonUtil.fromObject(data, clazz);
    }

    public <T> T get(TypeToken<T> token) {
        return JsonUtil.fromObject(data, token);
    }

    public <T> T getDecrypt(Class<T> clazz) {
        if (data == null) {
            return null;
        }
        return get(clazz);
        //return JsonUtil.fromJson(String.valueOf(data), clazz);
    }

    public <T> T getDecrypt(TypeToken<T> token) {
        if (data == null) {
            return null;
        }
        return get(token);
        //return JsonUtil.fromJson(String.valueOf(data), token);
    }

    public boolean isSuccess() {
        if (code.equals("00000")) return true;
        return false;
    }

    public String toString() {
        return JsonUtil.toJson(this);
    }

}
