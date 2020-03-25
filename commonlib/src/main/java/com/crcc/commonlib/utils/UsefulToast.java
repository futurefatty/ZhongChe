package com.crcc.commonlib.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;


/**
 * Created by Administrator on 2016/6/17.
 */
public class UsefulToast {


    private static Toast mToast;

    public static void showToast(Context context, String content) {
        showToast(context, "", content, 0);
    }

    public static void showToast(Context context, String code, String content, int time) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(context, content, time).show();
        } else {
            Toast.makeText(context, "(" + code + ")" + content, time).show();
        }
    }







}
