package com.neusoft.zcapplication.tools;

import android.content.Context;
import android.view.View.OnClickListener;

import com.neusoft.zcapplication.widget.CustomDialog;

/**
 * 弹窗工具类
 */
public class AlertUtil {

	public static void show(Context context, String message, String ok,
			OnClickListener listener1) {
		CustomDialog alert = new CustomDialog(context);
		alert.setNegativeButton(ok, listener1);
		alert.setMessage(message);
		alert.show();
	}

	public static void show2(Context context, String title, String ok,
							OnClickListener listener1) {
		CustomDialog alert = new CustomDialog(context);
		alert.setNegativeButton(ok, listener1);
		alert.setTitle(title);
		alert.show();
	}
	public static void showAlert(Context context, String title,String msg, String ok,
							 OnClickListener listener1) {
		CustomDialog alert = new CustomDialog(context);
		alert.setNegativeButton(ok, listener1);
		alert.setTitle(title);
		alert.setMessage(msg);
		alert.show();
	}
	public static void show(Context context, String message, String ok,
			OnClickListener listener1, String cancel, OnClickListener listener2,String titleText) {
		CustomDialog alert = new CustomDialog(context);
		alert.setTitle(titleText);
		alert.setNegativeButton(ok, listener1);
		alert.setPositiveButton(cancel, listener2);
		alert.setMessage(message);
		alert.show();
	}

    /**
     * 不可取消的
     * @param context
     * @param message
     * @param ok
     * @param listener1
     * @param cancel
     * @param listener2
     * @param titleText
     */
    public static void unCancelableDialog(Context context, String message, String ok,
                            OnClickListener listener1, String cancel, OnClickListener listener2,String titleText) {
        CustomDialog alert = new CustomDialog(context);
        alert.setTitle(titleText);
        alert.setNegativeButton(ok, listener1);
        alert.setPositiveButton(cancel, listener2);
        alert.setMessage(message);
        alert.setCancelable(false);
        alert.show();
    }
	public static CustomDialog showDialog(Context context, String message,
			String ok, OnClickListener listener1, String cancel,
			OnClickListener listener2) {
		CustomDialog alert = new CustomDialog(context).setNegativeButton(ok,
				listener1).setPositiveButton(cancel, listener2);
		alert.setMessage(message);
		alert.setCancelable(false);
		alert.setCanceledOnTouchOutside(false);
		alert.show();
		return alert;
	}

	public static void showDialog(Context context, String message, String ok,
			OnClickListener listener1) {
		CustomDialog alert = new CustomDialog(context).setNegativeButton(ok,
				listener1);
		alert.setMessage(message);
		alert.setCancelable(false);
		alert.setCanceledOnTouchOutside(false);
		alert.show();

	}

}
