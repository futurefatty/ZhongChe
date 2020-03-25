package com.neusoft.zcapplication.tools;

import android.content.Context;

import com.neusoft.zcapplication.widget.LoadingDialog;

/**
 * author:Six
 * Date:2019/5/28
 */
public class LoadingUtil {

    private static LoadingDialog loadDialog;

    /**
     * 显示加载框
     */
    public static void showLoading(Context context) {
        showLoading(context, "正在加载...", false);
    }

    /**
     * 显示加载框
     *
     * @param str  提示文字
     * @param bool true可取消
     */
    public static void showLoading(Context context, String str, boolean bool) {
        if (loadDialog != null) {
            if (loadDialog.isShowing()) {
                loadDialog.dismiss();
            }
            loadDialog = null;
        }
        loadDialog = LoadingDialog.createDialog(context);
        loadDialog.setMessage(str);
        loadDialog.setCancelable(bool);
        loadDialog.show();
    }

    /**
     * 关闭加载框
     */
    public static void dismissLoading() {
        if (loadDialog != null) {
            if (loadDialog.isShowing()) {
                loadDialog.dismiss();
            }
            loadDialog = null;
        }
    }
}
