package com.crcc.commonlib.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

/**
 * Created by Administrator on 2016/10/18.
 */
public class UtilDialPhone {

    private static class SingletonHolder {
        private static final UtilDialPhone INSTANCE = new UtilDialPhone();
    }

    public static UtilDialPhone getInstance() {
        return SingletonHolder.INSTANCE;
    }


    public void dial(final Context context, final String phone) {
        if (TextUtils.isEmpty(phone)) {
            return;
        }
        new AlertDialog.Builder(context)
                .setTitle("拨打电话")
                .setMessage(phone)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialPhono(context, phone);
                    }
                })
                .setNegativeButton("取消", null)
                .show();

    }

    //打电话
    @SuppressLint("MissingPermission")
    private void dialPhono(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        context.startActivity(intent);
    }
}
