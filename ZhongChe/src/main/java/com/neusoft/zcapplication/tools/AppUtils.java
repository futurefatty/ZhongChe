package com.neusoft.zcapplication.tools;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.base.ZcApplication;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/5/19.
 * App
 */

public class AppUtils {
    public static final String SETTING = "setting";
    public static final String SETTING_NOTICE = "notice";
    public static final String SETTING_VOICE = "voice";
    public static final String SETTING_MESSAGE = "msg";
    /**
     * 获取用户id
     */
//    private static final String USER_ID = "login_userId";
//    private static final String USER_NAME = "login_userName";
//    private static final String USER_ICON = "login_userIcon";
//    private static final String USER_INFO = "login_info";
    public static final String NAME_CODE_SPLIT = "@@";//城市名、城市三字码的分割符号
    public static final String CITIES_SPLIT = "&#";//各个城市组间的分割符号
    public static final String DOUBLE_CITIES_SPLIT = "&#&#";//各个城市组间的分割符号
    private static final String DEVICE_SIZE = "divice_size";
    private static final String DEVICE_HEIGHT = "divice_height";
    private static final String DEVICE_WIDTH = "divice_width";
    /**
     * 语言设置
     */
    private static final String LANGUAGE = "language";
    private static final String LANGUAGE_TYPE = "language_type";
    private static final String LANGUAGE_CN = "CN";
    private static final String LANGUAGE_EN = "EN";
    private static final String USER_STR = "user_info";//保存登录用户数据

    /**
     * 获取上下文对象
     *
     * @return
     */
    public static Context getContext() {
        return ZcApplication.getAppContext();
    }

    public static final int getDeviceHeight(Context ctx) {
        WindowManager manager = ((Activity) ctx).getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        return height;
    }

    public static final int getDeviceWidth(Context ctx) {
        WindowManager manager = ((Activity) ctx).getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        return width;
    }

    /**
     * 获取状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStateHeight(Activity context) {
        Rect frame = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int stateH = frame.top;
        return stateH;
    }

    //电话号码验证
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    //邮箱格式验证
    public static boolean isEmail(String email) {
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(email);
        boolean isMatched = matcher.matches();
        return isMatched;
    }

    /**
     * @param context
     * @return true 用户接收通知栏消息
     */
    public static boolean isNoticeOpen(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        boolean bool = sp.getBoolean(SETTING_NOTICE, true);
        return bool;
    }

    public static void setNotice(Context context, boolean bool) {
        SharedPreferences sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(SETTING_NOTICE, bool);
        editor.commit();
    }

    /**
     * @param context
     * @return true 打开了声音开关
     */
    public static boolean isVoiceOpen(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        boolean bool = sp.getBoolean(SETTING_VOICE, false);
        return bool;
    }

    public static void setVoice(Context context, boolean bool) {
        SharedPreferences sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(SETTING_VOICE, bool);
        editor.commit();
    }

    /**
     * @param context
     * @return true 打开了接收消息开关
     */
    public static boolean isMsgOpen(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        boolean bool = sp.getBoolean(SETTING_MESSAGE, true);
        return bool;
    }

    public static void setMsg(Context context, boolean bool) {
        SharedPreferences sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(SETTING_MESSAGE, bool);
        editor.commit();
    }

    /**
     * 如果用户有设置app语言，则返回用户设置的语言；否则判断系统的语言是否为中文，若为中文则返回true，反之返回false
     *
     * @param context
     * @return true 中文
     */
    public static boolean isCN(Context context) {
        SharedPreferences sp = context.getSharedPreferences(LANGUAGE, Context.MODE_PRIVATE);
        String str = sp.getString(LANGUAGE_TYPE, "");
        if (str.equals("")) {
            //获取系统的语言
            Resources res = context.getResources();
            Configuration config = res.getConfiguration();
            String locale = config.locale.getCountry();
            if (locale.equals(LANGUAGE_CN)) {
                return true;
            } else {
                return false;
            }
        } else {
            if (str.equals(LANGUAGE_CN)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static void setLanguage(Context context, boolean isCn) {
        SharedPreferences sp = context.getSharedPreferences(LANGUAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (isCn) {
            editor.putString(LANGUAGE_TYPE, LANGUAGE_CN);
        } else {
            editor.putString(LANGUAGE_TYPE, LANGUAGE_EN);
        }
        editor.commit();
    }

    public static void setAppLanguage(Context context) {
        boolean isCN = isCN(context);
        //如果缓存中的语言与要修改的语言不一致，则修改app语言
        Configuration configuration = context.getResources().getConfiguration();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        if (isCN) {
            configuration.locale = Locale.SIMPLIFIED_CHINESE;
        } else {
            configuration.locale = Locale.ENGLISH;
        }
        context.getResources().updateConfiguration(configuration, dm);
    }

    /**
     * 数字转千分位字符串
     *
     * @param number
     * @return
     */
    public static String fmtNumberWidthMeter(int number) {
        DecimalFormat df = null;
        String text = String.valueOf(number);
        if (text.indexOf(".") > 0) {
            if (text.length() - text.indexOf(".") - 1 == 0) {
                df = new DecimalFormat("###,##0.");
            } else if (text.length() - text.indexOf(".") - 1 == 1) {
                df = new DecimalFormat("###,##0.0");
            } else {
                df = new DecimalFormat("###,##0.00");
            }
        } else {
            df = new DecimalFormat("###,##0");
        }
        return df.format(number);
    }

    /**
     * 使用用户格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return
     */

    public static Date parse(String strDate, String pattern) {
        if (TextUtils.isEmpty(strDate)) {
            return null;
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用用户格式格式化日期
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return
     */

    public static String format(Date date, String pattern) {
        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            returnValue = df.format(date);
        }
        return (returnValue);
    }

    /**
     * 隐藏键盘
     *
     * @param ctx
     * @param view
     */
    public static void closeInputMethod(Context ctx, EditText view) {
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        if (isOpen) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

//    public static String getUserId(Context ctx){
//        SharedPreferences sp = ctx.getSharedPreferences(USER_INFO,Context.MODE_PRIVATE);
//        String userId = sp.getString(USER_ID,"123");
//        return userId;
//    }
//
//    /**
//     * 保存用户信息
//     * @param context
//     * @param nickName
//     * @param userId
//     * @param headImgUrl
//     */
//    public static void saveUserInfo(Context context,String nickName,String userId,String headImgUrl){
//        SharedPreferences sp = context.getSharedPreferences(USER_INFO,Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString(USER_ID,userId);
//        editor.putString(USER_NAME,nickName);
//        editor.putString(USER_ICON,headImgUrl);
//        editor.commit();
//    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = -1;
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 设置状态栏高度
     *
     * @param context
     * @param mStatusBar
     */
    public static void setStateBar(Context context, View mStatusBar) {
        int statueBarHeight = AppUtils.getStatusBarHeight(context);//状态栏高度
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mStatusBar.setVisibility(View.VISIBLE);
            mStatusBar.getLayoutParams().height = statueBarHeight;
            mStatusBar.setLayoutParams(mStatusBar.getLayoutParams());
        } else {
            mStatusBar.setVisibility(View.GONE);
        }
    }

    // 网络状态
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断用户是否处在wifi环境下
     *
     * @param context
     * @return
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            //2.获取当前网络连接的类型信息
            int networkType = networkInfo.getType();
            if (ConnectivityManager.TYPE_WIFI == networkType) {
                //当前为wifi网络
                return true;
            } else if (ConnectivityManager.TYPE_MOBILE == networkType) {
                //当前为mobile网络
                return false;
            }
        }
        return false;
    }

    /**
     * @param context
     * @return true 有待办工作
     */
    public static boolean hasJobToDo(Context context) {
        SharedPreferences sp = context.getSharedPreferences("JOB_TO_DO_SP", Context.MODE_PRIVATE);
        boolean bool = sp.getBoolean("hasJob", false);
        return bool;
    }

    /**
     * 保存是否有待办工作的标识
     *
     * @param context
     * @param hasJob  有待办工作，没有待办工作
     */
    public static void saveJobToDoStatus(Context context, boolean hasJob) {
        SharedPreferences sp = context.getSharedPreferences("JOB_TO_DO_SP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("hasJob", hasJob);
        editor.commit();
    }

    /**
     * 获取国际机票需待选方案的个数
     *
     * @param context
     * @return
     */
    public static int getInterTicketCount(Context context) {
        SharedPreferences sp = context.getSharedPreferences("INTER_COUNT_SP", Context.MODE_PRIVATE);
        int count = sp.getInt("inter_count", 0);
        return count;
    }

    /**
     * 保存国际机票需待选方案的个数
     *
     * @param context
     * @param count
     */
    public static void saveInterTicketCount(Context context, int count) {
        SharedPreferences sp = context.getSharedPreferences("INTER_COUNT_SP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("inter_count", count);
        editor.commit();
    }

    /**
     * 保存搜索的酒店城市列表数据,最多存8个
     *
     * @param context
     * @param fromNameCode 出发城市名称和编码 格式 name-code
     * @param toNameCode   到达城市名称和编码 格式 name-code
     */
    public static void saveHotelCityHistory(Context context, String fromNameCode, String toNameCode) {
        String spkey = "crrc_hotel_sp";
        String key = "crrc_hotel_key";
        SharedPreferences sp = context.getSharedPreferences(spkey, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        String history = getHotelCityHistory(context);
        String newStr;
        if (toNameCode.equals("")) {
            newStr = fromNameCode;
        } else {
            newStr = fromNameCode + CITIES_SPLIT + toNameCode;
        }
        if (history.length() > 0) {
            String oldStr = history.replace(fromNameCode, "").replace(toNameCode, "");// 去掉原来的历史记录里面的历史数据
            String str = (newStr + CITIES_SPLIT + oldStr).replace(DOUBLE_CITIES_SPLIT, CITIES_SPLIT);
            //若字符串的结尾是以分隔符结尾则将其去掉
//            if(str.lastIndexOf(",") == str.length() - 1){
//                str = str.substring(0,str.length() - 1);
//            }
            if (str.endsWith(CITIES_SPLIT)) {
                str = str.substring(0, str.length() - 2);
            }
            String[] ary = str.split(CITIES_SPLIT);
            if (ary.length > 8) {
                String saveStr = str.substring(0, str.lastIndexOf(CITIES_SPLIT));
                editor.putString(key, saveStr);
                editor.commit();
            } else {
                editor.putString(key, str);
                editor.commit();
            }
        } else {
            editor.putString(key, newStr);
            editor.commit();
        }
    }

    /**
     * 获取酒店城市历史数据
     *
     * @param context
     * @return
     */
    public static String getHotelCityHistory(Context context) {
        String spkey = "crrc_hotel_sp";
        String key = "crrc_hotel_key";
        SharedPreferences sp = context.getSharedPreferences(spkey, Context.MODE_PRIVATE);
        String data = sp.getString(key, "");
        return data;
    }

    /**
     * 保存搜索的机场城市列表数据,最多存8个
     *
     * @param context
     * @param fromNameCode 出发城市名称和编码 格式 name-code
     * @param toNameCode   到达城市名称和编码 格式 name-code
     */
    public static void saveFlightCityHistory(Context context, String fromNameCode, String toNameCode) {
        String spkey = "crrc_flight_sp";
        String key = "crrc_flight_key";
        SharedPreferences sp = context.getSharedPreferences(spkey, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        String history = getFlightCityHistory(context);
        String newStr;
        if (toNameCode.equals("")) {
            newStr = fromNameCode;
        } else {
            newStr = fromNameCode + "," + toNameCode;
        }
        if (history.length() > 0) {
            String oldStr = history.replace(fromNameCode, "").replace(toNameCode, "");// 去掉原来的历史记录里面的历史数据
            String str = (newStr + CITIES_SPLIT + oldStr).replace(DOUBLE_CITIES_SPLIT, CITIES_SPLIT);
            //若字符串的结尾是以分隔符结尾则将其去掉
//            if(str.lastIndexOf(",") == str.length() - 1){
//                str = str.substring(0,str.length() - 1);
//            }
            if (str.endsWith(CITIES_SPLIT)) {
                str = str.substring(0, str.length() - 2);
            }
            String[] ary = str.split(DOUBLE_CITIES_SPLIT);
            if (ary.length > 8) {
                String saveStr = str.substring(0, str.lastIndexOf(CITIES_SPLIT));
                editor.putString(key, saveStr);
                editor.commit();
            } else {
                editor.putString(key, str);
                editor.commit();
            }
        } else {
            editor.putString(key, newStr);
            editor.commit();
        }
    }

    /**
     * 获取酒店城市历史数据
     *
     * @param context
     * @return
     */
    public static String getFlightCityHistory(Context context) {
        String spkey = "crrc_flight_sp";
        String key = "crrc_flight_key";
        SharedPreferences sp = context.getSharedPreferences(spkey, Context.MODE_PRIVATE);
        String data = sp.getString(key, "");
        return data;
    }

    /**
     * 清除缓存的城市历史记录
     */
    public static void clearCityHistory(Context context) {
        String clearSpkey = "crrc_clear_sp";
        SharedPreferences clearSp = context.getSharedPreferences(clearSpkey, Context.MODE_PRIVATE);
        int hasClear = clearSp.getInt("crr_has_clear1", 0);
        if (hasClear == 0) {

            String flightSpkey = "crrc_flight_sp";
            SharedPreferences flightSp = context.getSharedPreferences(flightSpkey, Context.MODE_PRIVATE);
            SharedPreferences.Editor flightEditor = flightSp.edit();
            flightEditor.clear();
            flightEditor.commit();

            String hotelKey = "crrc_hotel_sp";
            SharedPreferences hotelSp = context.getSharedPreferences(hotelKey, Context.MODE_PRIVATE);
            SharedPreferences.Editor hotelEditor = hotelSp.edit();
            hotelEditor.clear();
            hotelEditor.commit();

            SharedPreferences.Editor clearEditor = clearSp.edit();
//            clearEditor.putBoolean("crr_has_clear",true);
            clearEditor.putInt("crr_has_clear1", 1);
            clearEditor.commit();
        }


    }

    public static int[] getScreenDispaly(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();// �ֻ���Ļ�Ŀ��
        int height = windowManager.getDefaultDisplay().getHeight();// �ֻ���Ļ�ĸ߶�
        int result[] = {width, height};
        return result;
    }

    /**
     * 保存手势密码
     *
     * @param context
     * @param gstStr  手势密码
     */
    public static void saveGestureData(Context context, String gstStr, String employeeCode) {
        SharedPreferences sp = context.getSharedPreferences("gesture_sp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        String key = "gesture_pass" + employeeCode;
        editor.putString(key, gstStr);
        editor.commit();
    }

    /**
     * 获取手势密码
     *
     * @param context
     * @return
     */
    public static String getGestureData(Context context, String employeeCode) {
        SharedPreferences sp = context.getSharedPreferences("gesture_sp", Context.MODE_PRIVATE);
        String key = "gesture_pass" + employeeCode;
        String gstStr = sp.getString(key, "");
        return gstStr;
    }

    /**
     * 保存用户的信息
     *
     * @param context
     * @param user
     */
    public static void saveUserInfo(Context context, User user) {
        SharedPreferences sp = context.getSharedPreferences(USER_STR, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString("id", user.getId());
        editor.putString("idCard", user.getIdCard());
        editor.putString("employeeName", user.getEmployeeName());
        editor.putString("employeeCode", user.getEmployeeCode());
        editor.putString("birthday", user.getBirthday());
        editor.putString("gender", user.getGender());
        editor.putString("mail", user.getMail());
        editor.putString("mobil", user.getMobil());
        editor.putString("phone", user.getPhone());
        editor.putString("address", user.getAddress());
        editor.putString("password", user.getPassword());
        editor.putString("payCode", user.getPayCode());
        editor.putString("unitCode", user.getUnitCode());
        editor.putString("unitName", user.getUnitName());
        editor.commit();
    }

    /**
     * 获取保存的用户数据
     *
     * @param context
     * @return
     */
    public static User getUserInfo(Context context) {
        SharedPreferences sp = context.getSharedPreferences(USER_STR, Context.MODE_PRIVATE);
        User user = new User();
        String id = sp.getString("id", "");
        String idCard = sp.getString("idCard", "");
        String employeeName = sp.getString("employeeName", "");
        String employeeCode = sp.getString("employeeCode", "");
        String birthday = sp.getString("birthday", "");
        String gender = sp.getString("gender", "");
        String mail = sp.getString("mail", "");
        String mobil = sp.getString("mobil", "");
        String phone = sp.getString("phone", "");
        String address = sp.getString("address", "");
        String password = sp.getString("password", "");
        String payCode = sp.getString("payCode", "");
        String unitCode = sp.getString("unitCode", "");
        String unitName = sp.getString("unitName", "");

        user.setId(id);
        user.setIdCard(idCard);
        user.setEmployeeName(employeeName);
        user.setEmployeeCode(employeeCode);
        user.setBirthday(birthday);
        user.setGender(gender);
        user.setMail(mail);
        user.setMobil(mobil);
        user.setPhone(phone);
        user.setAddress(address);
        user.setPassword(password);
        user.setPayCode(payCode);
        user.setUnitCode(unitCode);
        user.setUnitName(unitName);
        return user;
    }

    /**
     * 获取版本名称
     *
     * @return
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager
                    .getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            LogUtil.d("Exception:---" + e);
        }
        return versionName;
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int versionCode = -1;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager
                    .getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (Exception e) {
            LogUtil.d("Exception:---" + e);
        }
        return versionCode;
    }

    /**
     * 是否是泛嘉订单
     *
     * @param map
     * @return
     */
    public static boolean isFanJiaOrder(Map<String, Object> map) {
        String supplerIdStr = map.get("SUPPLIERID") == null ? "" : map.get("SUPPLIERID").toString();
        if (TextUtils.isEmpty(supplerIdStr)) {
            return false;
        }
        if (supplerIdStr.contains(".")) {
            supplerIdStr = supplerIdStr.substring(0, supplerIdStr.indexOf("."));
        }
        return 8 == Integer.parseInt(supplerIdStr);
    }

}
