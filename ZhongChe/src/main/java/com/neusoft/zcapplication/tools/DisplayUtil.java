package com.neusoft.zcapplication.tools;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by Administrator on 2017/3/7.
 * 屏幕工具类
 */

public class DisplayUtil {
    /**
     * 获取当前设备的屏幕宽度
     * @param activity
     * @return
     */
    public static int getDeviceWidth(Activity activity){
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
     /*   int height = metric.heightPixels;   // 屏幕高度（像素）
        float density = metric.density;      // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）*/
        return width;
    }
    /**
     * 获取当前设备的屏幕高度
     * @param activity
     * @return 屏幕的高度
     */
    public static int getDeviceHeight(Activity activity){
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int height = metric.heightPixels;   // 屏幕高度（像素）
        return height;
    }
    public static int getScreenWidth (Context context){
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        return  dm.widthPixels;
    }
    /**
     * 获取屏幕密度
     * @param activity
     * @return
     */
    public static float getDeviceDensity(Activity activity){
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        float density = metric.density;
        return density;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dpTopx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int pxTodp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    public static int spTopx(Context context, float spValue) {
//        final float scale = context.getResources().getDisplayMetrics().density;
//        return (int) (spValue / scale + 0.5f);

        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
    /**
     * 计算图片绘制后的宽、高尺寸
     * @param resourceId
     * @param resources
     * @param scale
     * @return
     */
    public static float[] getPictureDrawedSize(int resourceId,Resources resources,float scale){
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 只获取边框信息，可以减少内存的开销
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inScaled = false;
        //获取的bitmap为null
        BitmapFactory.decodeResource(resources,resourceId,options);
        float w = options.outWidth * scale;
        float h = options.outHeight * scale;
        float[] size = new float[2];
        size[0] = w;
        size[1] = h;
       return size;
    }

    /**
     * 测量View的宽高
     *
     * @param view View
     */
    public static void measureWidthAndHeight(View view) {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 设置view paddings
     *
     * @param v
     * @param l
     * @param t
     * @param r
     * @param b
     */
    public static void setPaddings(View v, int l, int t, int r, int b) {
        v.setPadding(l, t, r, b);
        v.requestLayout();
    }

    /**
     * 设置view margin
     *
     * @param v
     * @param l
     * @param t
     * @param r
     * @param b
     */
    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

}
