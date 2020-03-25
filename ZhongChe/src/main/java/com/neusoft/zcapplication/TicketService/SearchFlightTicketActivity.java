package com.neusoft.zcapplication.TicketService;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.Calendar.CalendarActivity;
import com.neusoft.zcapplication.city.AirportActivity;
import com.neusoft.zcapplication.Constant.Constant;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.ShowViewActivity;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.entity.SearchFlightTicketCity;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.SPAppUtil;
import com.neusoft.zcapplication.tools.StringUtil;
import com.neusoft.zcapplication.tools.ToastUtil;

/**
 * 搜索机票信息入口界面
 */

public class SearchFlightTicketActivity extends BaseActivity implements View.OnClickListener {

    private String toCityCode, fromCityCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_flight_ticket);
        initView();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.city_from:
                intent = new Intent(SearchFlightTicketActivity.this, AirportActivity.class);
                intent.putExtra("inCity", "yes");
                startActivityForResult(intent, 3001);
                break;
            case R.id.city_to:
                intent = new Intent(SearchFlightTicketActivity.this, AirportActivity.class);
                intent.putExtra("inCity", "yes");
                startActivityForResult(intent, 3002);
                break;
            case R.id.time_start:
                String checkIn = ((TextView) v).getText().toString();
                intent = new Intent(SearchFlightTicketActivity.this, CalendarActivity.class);
                intent.putExtra("selectType", 1);//1选择国际机票日期
                intent.putExtra("days", 1);
                intent.putExtra("firstDay", checkIn);
                startActivityForResult(intent, 3003);
                break;
            case R.id.btn_search:
                //查询跳转
                skip();
                break;
            case R.id.btn_exchange:
                //交换城市信息
                TextView fromCityTv = (TextView) findViewById(R.id.city_from);
                TextView toCityTv = (TextView) findViewById(R.id.city_to);
                String fromCityName = fromCityTv.getText().toString();
                String toCityName = toCityTv.getText().toString();
                fromCityTv.setText(toCityName);
                toCityTv.setText(fromCityName);
                String tempCode = fromCityCode;
                fromCityCode = toCityCode;
                toCityCode = tempCode;
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != data) {
            if (requestCode == 3001) {
                TextView fromCityTv = (TextView) findViewById(R.id.city_from);
                fromCityCode = data.getStringExtra("code").toString();
                fromCityTv.setText(data.getStringExtra("name").toString());
                SPAppUtil.setSearchFlightTicketStartCity(mContext, new SearchFlightTicketCity(
                        data.getStringExtra("code").toString(),
                        data.getStringExtra("name").toString()));
            } else if (requestCode == 3002) {
                //到达城市
                TextView toCityTv = (TextView) findViewById(R.id.city_to);
                toCityCode = data.getStringExtra("code").toString();
                toCityTv.setText(data.getStringExtra("name").toString());
                SPAppUtil.setSearchFlightTicketEndCity(mContext, new SearchFlightTicketCity(
                        data.getStringExtra("code").toString(),
                        data.getStringExtra("name").toString()));
            } else if (requestCode == 3003) {
                //选择时间的回调方法
                String firstDay = data.getStringExtra("firstDay");
                TextView timeTv = (TextView) findViewById(R.id.time_start);
                timeTv.setText(firstDay);
                SPAppUtil.setSearchFlightTicketTime(mContext, firstDay);
            }
        }
    }

    private void initView() {
        AppUtils.setStateBar(SearchFlightTicketActivity.this, findViewById(R.id.frg_status_bar));
        findViewById(R.id.btn_back).setOnClickListener(this);//返回按钮
        findViewById(R.id.city_from).setOnClickListener(this);//出发城市
        findViewById(R.id.city_to).setOnClickListener(this);//到达城市
        findViewById(R.id.time_start).setOnClickListener(this);//出发日期
        findViewById(R.id.btn_search).setOnClickListener(this);//查询按钮
        findViewById(R.id.btn_exchange).setOnClickListener(this);//交换出发到达城市
        //出发城市
        SearchFlightTicketCity searchFlightTicketStartCity = SPAppUtil.getSearchFlightTicketStartCity(mContext);
        if (!StringUtil.isEmpty(searchFlightTicketStartCity.getCityCode()) && !StringUtil.isEmpty(searchFlightTicketStartCity.getCityName())) {
            TextView fromCityTv = (TextView) findViewById(R.id.city_from);
            fromCityCode = searchFlightTicketStartCity.getCityCode();
            fromCityTv.setText(searchFlightTicketStartCity.getCityName());
        }
        //到达城市
        SearchFlightTicketCity searchFlightTicketEndCity = SPAppUtil.getSearchFlightTicketEndCity(mContext);
        if (!StringUtil.isEmpty(searchFlightTicketEndCity.getCityCode()) && !StringUtil.isEmpty(searchFlightTicketEndCity.getCityName())) {
            TextView toCityTv = (TextView) findViewById(R.id.city_to);
            toCityCode = searchFlightTicketEndCity.getCityCode();
            toCityTv.setText(searchFlightTicketEndCity.getCityName());
        }

        String seachTime = SPAppUtil.getSearchFlightTicketTime(mContext);
        if (!StringUtil.isEmpty(seachTime)) {
            TextView tv_time = (TextView) findViewById(R.id.time_start);
            tv_time.setText(seachTime);
        }
    }

    private void skip() {
        TextView fromCityTv = (TextView) findViewById(R.id.city_from);
        TextView toCityTv = (TextView) findViewById(R.id.city_to);
        TextView timeTv = (TextView) findViewById(R.id.time_start);

        String fromCityName = fromCityTv.getText().toString();
        String toCityName = toCityTv.getText().toString();
        String timeStart = timeTv.getText().toString();
        if (fromCityName.equals("")) {
            ToastUtil.toastNeedData(SearchFlightTicketActivity.this, "请选择出发城市~");
        } else if (toCityName.equals("")) {
            ToastUtil.toastNeedData(SearchFlightTicketActivity.this, "请选择到达城市~");
        } else if (timeStart.equals("")) {
            ToastUtil.toastNeedData(SearchFlightTicketActivity.this, "请选择出发时间~");
        } else {
            User user = AppUtils.getUserInfo(SearchFlightTicketActivity.this);
            String url = Constant.ZHONGCHE_H5 + "oneWayFlightList.html?departCity=" + fromCityName
                    + "&departCityCode=" + fromCityCode + "&reachCity=" + toCityName
                    + "&reachCityCode=" + toCityCode + "&startDate=" + timeStart
                    + "&billNo=&tripType=0&cptxt=string&userNo=" + user.getEmployeeCode() + "&userName=" + user.getEmployeeName()
                    + "&loginUserNo=" + user.getEmployeeCode() + "&loginUserName="
                    + user.getEmployeeName() +
                    "&seatType=1" +
                    "&bookApplyHiding=true";
            Intent it = new Intent(SearchFlightTicketActivity.this, ShowViewActivity.class);
            it.putExtra("url", url);
            it.putExtra("showMoreBtn", true);
            startActivity(it);
        }
    }
}
