package com.crcc.commonlib.utils;

import java.lang.reflect.Method;

/**
 * author: Six
 * Created by on 2018/3/31
 */

public class DevicesUtils {

    /**
     * 获取设备mac地址
     *
     * @return
     */
    public static String getMac() {
        String serial = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
        } catch (Exception e) {
            e.printStackTrace();

        }
        return serial;
    }
}
