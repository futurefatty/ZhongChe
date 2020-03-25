package com.crcc.commonlib.utils;

import android.text.TextUtils;

/**
 * Created  2018/3/9.
 *
 * @author six
 */

public class StringUtils {


    public static String trim(String text) {
        if (TextUtils.isEmpty(text)) {
            return "";
        }
        return text.replace(" ", "");
    }

    public static String getString(String text) {
        if (TextUtils.isEmpty(text)) {
            return "";
        } else {
            return text;
        }
    }


    public static String getString(String first, String two) {
        return getString(first) + getString(two);
    }

    /**
     * 格式化字符串，每三位用逗号隔开
     *
     * @param str
     * @return
     */
    public static String addComma(String str) {
        str = new StringBuilder(str).reverse().toString();     //先将字符串颠倒顺序
        if (str.equals("0")) {
            return str;
        }
        String str2 = "";
        for (int i = 0; i < str.length(); i++) {
            if (i * 3 + 3 > str.length()) {
                str2 += str.substring(i * 3, str.length());
                break;
            }
            str2 += str.substring(i * 3, i * 3 + 3) + ",";
        }
        if (str2.endsWith(",")) {
            str2 = str2.substring(0, str2.length() - 1);
        }
        //最后再将顺序反转过来
        String temp = new StringBuilder(str2).reverse().toString();
        //将最后的,去掉
        return temp.substring(0, temp.lastIndexOf(",")) + temp.substring(temp.lastIndexOf(",") + 1, temp.length());
    }


    public static boolean isEmpty(String data) {
        return !(data != null
                && data.length() > 0
                && !"[]".equals(data)
                && !"null".equals(data)
                && !"{}".equals(data));
    }
}
