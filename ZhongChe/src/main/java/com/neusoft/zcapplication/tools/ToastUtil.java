package com.neusoft.zcapplication.tools;

import android.content.Context;
import android.widget.Toast;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.base.ZcApplication;

/**
 * Created by Administrator on 2017/11/9.
 * 吐司工具类
 */

public class ToastUtil {
    private static Toast toast;
    /**
     * 获取数据成功
     * @param context
     */
    public static void toastSuccess(Context context){
        String str = context.getString(R.string.tst_get_success);
        if(null == toast){
            toast = Toast.makeText(context,str,Toast.LENGTH_SHORT);
        }else{
            toast.setText(str);
        }
        toast.show();
    }
    /**
     * 获取数据失败
     * @param context
     */
    public static void toastFail(Context context){
        String str = context.getString(R.string.tst_get_fail);
        if(null == toast){
            toast = Toast.makeText(context,str,Toast.LENGTH_SHORT);
        }else{
            toast.setText(str);
        }
        toast.show();
    }
    /**
     * 获取到的数据为空
     * @param context
     */
    public static void toastNoData(Context context){
        String str = context.getString(R.string.tst_get_no_data);
        if(null == toast){
            toast = Toast.makeText(context,str,Toast.LENGTH_SHORT);
        }else{
            toast.setText(str);
        }
        toast.show();
    }

    /**
     *
     */
    public static void toast(String msg){
        if(null == toast){
            toast = Toast.makeText(ZcApplication.getAppContext(),msg,Toast.LENGTH_SHORT);
        }else{
            toast.setText(msg);
        }
        toast.show();
    }

    /**
     *
     * @param context
     */
    public static void toastError(Context context){
        String str = context.getString(R.string.tst_get_error);
        if(null == toast){
            toast = Toast.makeText(context,str,Toast.LENGTH_SHORT);
        }else{
            toast.setText(str);
        }
        toast.show();
    }
    /**
     * 操作成功
     * @param context
     */
    public static void toastHandleSuccess(Context context){
        String str = context.getString(R.string.tst_handle_success);
        if(null == toast){
            toast = Toast.makeText(context,str,Toast.LENGTH_SHORT);
        }else{
            toast.setText(str);
        }
        toast.show();
    }
    /**
     * 操作失败
     * @param context
     */
    public static void toastHandleFail(Context context){
        String str = context.getString(R.string.tst_handle_fail);
        if(null == toast){
            toast = Toast.makeText(context,str,Toast.LENGTH_SHORT);
        }else{
            toast.setText(str);
        }
        toast.show();
    }
    /**
     * 操作出错
     * @param context
     */
    public static void toastHandleError(Context context){
        String str = context.getString(R.string.tst_handle_error);
        if(null == toast){
            toast = Toast.makeText(context,str,Toast.LENGTH_SHORT);
        }else{
            toast.setText(str);
        }
        toast.show();
    }
    /**
     * 提示需要填写某些数据
     * @param context
     * @param text
     */
    public static void toastNeedData(Context context,String text){
        if(null == toast){
            toast = Toast.makeText(context,text,Toast.LENGTH_SHORT);
        }else{
            toast.setText(text);
        }
        toast.show();
    }
}
