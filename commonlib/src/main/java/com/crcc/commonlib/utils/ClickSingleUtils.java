package com.crcc.commonlib.utils;

/**
 * author: Six
 * Created by on 2018/7/26
 */
public class ClickSingleUtils {


    private static long firstTime = 0;


    public static boolean isSingleClick(long num) {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > num) {
            firstTime = secondTime;
            return true;
        } else {
            return false;
        }
    }

}
