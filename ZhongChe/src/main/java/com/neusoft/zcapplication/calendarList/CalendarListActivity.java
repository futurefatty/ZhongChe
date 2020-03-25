package com.neusoft.zcapplication.calendarList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.neusoft.zcapplication.Calendar.CalendarActivity;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.ToastUtil;

/**
 * 思路
 * 1、生成日历数据
 * 生成每个月的每一天的数据
 */

public class CalendarListActivity  extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_calendar);
        initView();
        CalendarList calendarList= (CalendarList) findViewById(R.id.calendarList);
//        calendarList.setOnDateSelected(new CalendarList.OnDateSelected() {
//            @Override
//            public void selected(String startDate, String endDate) {
//                Toast.makeText(getApplicationContext(),"s:"+startDate+"e:"+endDate,Toast.LENGTH_LONG).show();
//            }
//        });
        calendarList.setOnDateSelected(new CalendarList.OnDateSelected() {
            @Override
            public void selected(String startDate) {
                Intent intent = new Intent();
                intent.putExtra("firstDay",startDate);
                int position = getIntent().getIntExtra("position",-1);
                intent.putExtra("position",position);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
    private void initView(){
        findViewById(R.id.btn_back).setOnClickListener(this);
        AppUtils.setStateBar(CalendarListActivity.this,findViewById(R.id.frg_status_bar));
//        canSelectDays = getIntent().getIntExtra("days",1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
        }
    }
}
