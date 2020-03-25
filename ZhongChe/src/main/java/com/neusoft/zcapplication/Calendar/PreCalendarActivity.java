package com.neusoft.zcapplication.Calendar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.DateUtils;
import com.neusoft.zcapplication.tools.ToastUtil;
import com.neusoft.zcapplication.widget.MyCalendarLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日历选择
 */

public class PreCalendarActivity extends BaseActivity implements View.OnClickListener,MyCalendarLayout.OnDaySelectListener{
    private static final  int  MAX_MONTH_NUM = 12;
    SimpleDateFormat simpleDateFormat;//,sd1,sd2;
    private LinearLayout calendarLayout;//日历部分布局
    //    private Date date;
    private String nowday;//今天的日期字符串
    //    private long nd = 1000*24L*60L*60L;//一天的毫秒数
    private String goDay,backDay;
    private int canSelectDays = 1;//可以选择的天数
    private String selectFirstDay;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initView();
        generalWeekLayout();//生成顶部星期
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        nowday = simpleDateFormat.format(new Date());
//        sd1 = new SimpleDateFormat("yyyy");
//        sd2 = new SimpleDateFormat("dd");
        calendarLayout = (LinearLayout) findViewById(R.id.activity_calendar_layout);
        //去程、返程日期
        Intent intent = getIntent();
        goDay = intent.getStringExtra("firstDay");
        backDay = intent.getStringExtra("secondDay");
        initCalendarLayout(goDay,backDay);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
        }
    }
    private void initView(){
        findViewById(R.id.btn_back).setOnClickListener(this);
        AppUtils.setStateBar(PreCalendarActivity.this,findViewById(R.id.frg_status_bar));
        canSelectDays = getIntent().getIntExtra("days",1);
    }

    /**
     * 生成星期视图
     */
    private void generalWeekLayout(){
        LinearLayout layout = (LinearLayout)findViewById(R.id.calendar_week_layout);
        String[] ary = getResources().getStringArray(R.array.weekAry);
        layout.removeAllViews();
        for(int i = 0 ; i < ary.length ; i++){
            TextView tv = new TextView(PreCalendarActivity.this);

            tv.setText(ary[i]);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
            tv.setTextColor(Color.parseColor("#999999"));
            tv.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.weight = 1;
            tv.setLayoutParams(layoutParams);
            layout.addView(tv);
        }
    }


    private void initCalendarLayout(String goDay,String backDay){
        List<String> listDate = getDateList();
        calendarLayout.removeAllViews();
        int dateTextType = getIntent().getIntExtra("selectType",1);
        boolean hideDateText = getIntent().getBooleanExtra("hideDateText",false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        for(int i = 0;i < listDate.size();i++){
            MyCalendarLayout c1 = new MyCalendarLayout(this);
            c1.setId(i);//设置id
            c1.setLayoutParams(params);
            Date date = null;
            try {
                date = simpleDateFormat.parse(listDate.get(i));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(null != goDay && !"".equals(goDay)){
                c1.setGoDay(goDay);
            }
            if(null != backDay && !"".equals(backDay)){
                c1.setBackDay(backDay);
            }
            c1.setTheDay(date,dateTextType,hideDateText);
            c1.setOnDaySelectListener(this);
            calendarLayout.addView(c1);
        }
    }

    @Override
    public void onDaySelectListener(View view, String date) {
        //若日历日期小于当前日期，则不响应点击事件点击
        try {
            long time1 = simpleDateFormat.parse(date).getTime();
            long time2 = simpleDateFormat.parse(nowday).getTime();
            if(time1 < time2){
                return;
            }
//			long dayxc=(simpleDateFormat.parse(date).getTime()-simpleDateFormat.parse(nowday).getTime())/nd;
//			if(dayxc>10){
//				return;
//			}
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //若以前已经选择了日期，则在进入日历后会显示以选择的日期，该部分作用则是重新点击日历时，清空以前选择的数据（包括背景图案）
//		if(!"".equals(sp_inday)){
//			c1.viewIn.setBackgroundColor(Color.WHITE);
//			((TextView) c1.viewIn.findViewById(R.id.tv_calendar_day)).setTextColor(Color.BLACK);
//			((TextView) c1.viewIn.findViewById(R.id.tv_calendar)).setText("");
//		}
        /*  if(!"".equals(sp_outday)){
        	  c1.viewOut.setBackgroundColor(Color.WHITE);
        	 ((TextView) c1.viewOut.findViewById(R.id.tv_calendar_day)).setTextColor(Color.BLACK);
             ((TextView) c1.viewOut.findViewById(R.id.tv_calendar)).setText("");
          }*/

//        String dateDay = date.split("-")[2];
//        if(Integer.parseInt(dateDay)<10){
//            dateDay = date.split("-")[2].replace("0", "");
//        }
        int needDays = getIntent().getIntExtra("days",1);//需要选择的日期
        if(needDays == 1){
            initCalendarLayout(date,"");
            Intent intent = new Intent();
            intent.putExtra("firstDay",date);
            int position = getIntent().getIntExtra("position",-1);
            intent.putExtra("firstDay",date);
            intent.putExtra("position",position);
            setResult(RESULT_OK,intent);
            finish();
        }else if(needDays == 2){
            if(canSelectDays == 2){
                canSelectDays--;
                //选择了第一个日期
                selectFirstDay = date;
//                TextView inDayTv = (TextView)view.findViewById(R.id.tv_calendar_day);
//                TextView textTv = (TextView)view.findViewById(R.id.tv_calendar);
//                inDayTv.setTextColor(Color.WHITE);
//                textTv.setText("入住");
//                textTv.setTextColor(Color.WHITE);
                initCalendarLayout(date,"");
            }else if(canSelectDays == 1){
                //判断选择的离店日期是否在入住日期之前
                boolean bool = DateUtils.isFirstDayBeforeSecondDay(selectFirstDay,date);
                int position = getIntent().getIntExtra("position",-1);
                if(bool){
                    canSelectDays--;
                    initCalendarLayout(selectFirstDay,date);
                    Intent intent = new Intent();
                    intent.putExtra("firstDay",selectFirstDay);
                    intent.putExtra("secondDay",date);
                    intent.putExtra("position",position);
                    setResult(RESULT_OK,intent);
                    finish();
                }else{
                    ToastUtil.toastNeedData(PreCalendarActivity.this,"请选择" + selectFirstDay + "之后的日期");
                }
            }

        }
        //修改选中日期的显示状态
//        TextView textDayView =(TextView) view.findViewById(R.id.tv_calendar_day);
//        TextView textView =(TextView) view.findViewById(R.id.tv_calendar);
//        view.setBackgroundColor(Color.parseColor("#40a8ff"));
//        textDayView.setTextColor(Color.WHITE);
//        Intent intent = new Intent();
//        intent.putExtra("date",date);
//        setResult(RESULT_OK,intent);
//        finish();
    }

    /**
     * 获取近3个月的日历
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public List<String> getDateList(){
        List<String> list = new ArrayList<String>();
        Date date = new Date();
        list.add(simpleDateFormat.format(date));//添加当前月
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH) + 1;
//        int d = calendar.get(Calendar.DATE);

        for(int i = 1 ; i < MAX_MONTH_NUM ; i++){
//            String str = y + "-" + getMon(m + i)+ "-" + d ;
            //下一个月的日期都设为开始日期
            String str = y + "-" + getMon(m + i)+ "-01" ;
            try {
                Date newDate = simpleDateFormat.parse(str);
                Calendar cld1 = Calendar.getInstance();
                cld1.setTime(newDate);
//                if(i == 1){
//                    int m1 = cld1.get(Calendar.MONTH) + 1;
//                    int y1 = cld1.get(Calendar.YEAR);
//                    int d1 = cld1.get(Calendar.DATE);
//                    String resultStr = y1 + "-" + m1 + "-" + d1;
//                    list.add(resultStr);
//                }else{
                int m1 = cld1.get(Calendar.MONTH) + 1;
                int y1 = cld1.get(Calendar.YEAR);
//                    int d1 = cld1.get(Calendar.DATE);
                String resultStr = y1 + "-" + m1 + "-01" ;
//                    String resultStr = y1 + "-01-01";
                list.add(resultStr);
//                }
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("calendar--error");
            }
        }
        return list;
    }

    /**
     * 格式化日期
     * @param mon
     * @return
     */
    private String getMon(int mon){
        if(mon < 10){
            return "0"+ mon;
        }else{
            return "" + mon;
        }
    }

    /**
     * 获取某个月份有多少天数
     * @param dateStr
     * @return
     */
    private int getMonthDay(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            calendar.set(Calendar.DATE, 1);
//            calendar.roll(Calendar.DATE, -1);
            int maxDate = calendar.get(Calendar.DATE);
            return maxDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }finally {
            return 0;
        }
    }

    /**
     * 计算出日期总共多少天，当前日期是多少号，当前月第一天的星期下标
     * @param dateStr
     * @return
     */
    private int[] numbersOfDate(String dateStr){
        int[] ary = new int[3];
        try {
            Date date  = simpleDateFormat.parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int days = calendar.get(Calendar.DATE);//当天是多少号
            //获取当月第一天的星期下标
            calendar.set(Calendar.DATE, 1);
            int firstDayInWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            //获取当月总共的天数
            calendar.roll(Calendar.DATE, -1);
            int maxDays = calendar.get(Calendar.DATE);

//            //当前日期离月末剩余多少天
//            int leftDays = maxDays - days + 1;
            ary[0] = maxDays;
            ary[1] = days;
            ary[2] = firstDayInWeek;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ary;
    }
}
