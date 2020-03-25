package com.neusoft.zcapplication.widget;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neusoft.zcapplication.Calendar.CalendarGridViewAdapter;
import com.neusoft.zcapplication.R;

/**
 * 日历
 */
public class MyCalendarLayout extends LinearLayout {

//    public static String positionIn;
//    public static String positionOut;
    private static String nowday = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) ;
//    private static SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");//日期格式化
    private Context context;
    private Date thegoDay;
    private String goDay = "",backDay = "";
    private OnDaySelectListener callBack;//回调函数
//    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");//日期格式化
    //    private List<Map<String,String>> dataList;
    private CalendarGridViewAdapter gridViewAdapter;
    private int dateTextType;//选择日期后，下面显示的文字类型 1 机票类型文字，2酒店类型文字
    private boolean hideDateText;
    /**
     * 构造函数
     * @param context
     */
    public MyCalendarLayout(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * 构造函数
     * @param context
     */
    public MyCalendarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    /**
     * 去程日期
     * @param goDay
     */
    public void setGoDay(String goDay){
        this.goDay = goDay;
    }

    /**
     * 返程日期
     * @param backDay
     */
    public void setBackDay(String backDay){
    	this.backDay = backDay;
    }

    public void setTheDay(Date dateIn,int dateTextType){
        this.thegoDay=dateIn;
        this.dateTextType = dateTextType;
        init();
    }
    public void setTheDay(Date dateIn,int dateTextType,boolean hideDateText){
        this.thegoDay=dateIn;
        this.dateTextType = dateTextType;
        this.hideDateText = hideDateText;
        init();
    }


    /**
     * 初始化日期以及view等控件
     */
    private void init() {
        final Calendar cal = Calendar.getInstance();//获取日历实例
        cal.setTime(thegoDay);//cal设置为当天的
        cal.set(Calendar.DATE, 1);//cal设置当前day为当前月第一天
        int tempSum = countNeedHowMuchEmpty(cal);//获取当前月第一天为星期几
        int dayNumInMonth = getDayNumInMonth(cal);//获取当前月有多少天
        //年、月拼接的字符串
        String ym = cal.get(Calendar.YEAR) + "-" + getMonth((cal.get(Calendar.MONTH) + 1));
        List<String> list = setGvListData(tempSum, dayNumInMonth,ym);

        View view = LayoutInflater.from(context).inflate(R.layout.layout_calendar_month, this, true);//获取布局，开始初始化
//        TextView tv_year= (TextView) view.findViewById(R.id.tv_year);
//        if(cal.get(Calendar.YEAR) > new Date().getYear()){
//            tv_year.setVisibility(View.VISIBLE);
//            tv_year.setText(cal.get(Calendar.YEAR)+"年");
//        }
        TextView tv_month= (TextView) view.findViewById(R.id.tv_month);
        tv_month.setText(ym);

        DefinedGridView gv = (DefinedGridView) view.findViewById(R.id.gv_calendar);
        gridViewAdapter = new CalendarGridViewAdapter(list,goDay,nowday,context,dateTextType,hideDateText);
        gridViewAdapter.setBackDay(backDay);//返程日期
        gv.setAdapter(gridViewAdapter);
        gv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View arg1,int position, long arg3) {
                //如果点击item的时间为过去的时间，则不可点击
                String choiceDay = (String) adapterView.getAdapter().getItem(position);
                String[] date=choiceDay.split(",");
                String day=date[1];
                if (!" ".equals(day)) {
                    if (Integer.parseInt(day) < 10) {
                        day = "0" + date[1];
                    }
                    choiceDay = date[0] +"-"+ day;
//                    goDay = choiceDay;
//                    init();
//                    Log.i("--->","choiceDay= "+ choiceDay);
                    if (callBack != null) {//调用回调函数回调数据
                        callBack.onDaySelectListener(arg1,choiceDay);
                    }
                }
            }
        });
    }


    /**
     * 为gridview中添加需要展示的数据
     * @param tempSum  空出来的天数
     * @param dayNumInMonth 每个月的第一天为星期几
     */
    private List<String> setGvListData(int tempSum, int dayNumInMonth,String yearAndMonth) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < tempSum; i++) {
            list.add(" , ");
        }
        for (int j = 1; j <= dayNumInMonth; j++) {
            list.add(yearAndMonth +"," + String.valueOf(j));
        }
        return  list;
    }

    private String getMonth(int month) {
        if(month<10){
            return "0" + month;
        }else{
            return "" + month;
        }
    }

    /**
     * 获取当前月的总共天数
     * @param cal
     * @return
     */
    private int getDayNumInMonth(Calendar cal) {
        return cal.getActualMaximum(Calendar.DATE);
    }

    /**
     * 获取当前月第一天在第一个礼拜的第几天，得出第一天是星期几
     * @param cal
     * @return
     */
    private int countNeedHowMuchEmpty(Calendar cal) {
        int firstDayInWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return firstDayInWeek;
    }

    /**
     * 自定义监听接口设置对象
     * @param o
     */
    public void setOnDaySelectListener(OnDaySelectListener o) {
        callBack = o;
    }
    /**
     * 自定义监听接口
     * @author Administrator
     *
     */
    public interface OnDaySelectListener {
        void onDaySelectListener(View view, String date);
    }
}