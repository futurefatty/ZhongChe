package com.crcc.commonlib.utils;

import android.content.Context;
import android.util.DisplayMetrics;

import java.lang.reflect.Field;

/**
 * @Description:px/dp杞崲
 */
public class UtilDisplay {
    public static final int SMALL = 0;
    public static final int MIDDLE = 1;
    public static final int LARGE = 2;
    public static final int XLARGE = 3;

    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        context = null;
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        context = null;
        return dm.heightPixels;
    }

    /**
     * 鏍规嵁鎵嬫満鐨勫垎杈ㄧ巼浠�dp 鐨勫崟浣�杞垚涓�px(鍍忕礌)
     */
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        context = null;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context,float pxValue){
        final float scale = context.getResources ().getDisplayMetrics ().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 灏唖p鍊艰浆鎹负px鍊硷紝淇濊瘉鏂囧瓧澶у皬涓嶅彉
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int getDisplayType(Context context) {
        int screenWidth = getScreenWidth(context);
        if (screenWidth >= 320 && screenWidth < 480) {
            return SMALL;
        } else if (screenWidth >= 480 && screenWidth < 720) {
            return MIDDLE;
        } else if (screenWidth >= 720 && screenWidth < 1080) {
            return LARGE;
        } else {
            return XLARGE;
        }
    }
}