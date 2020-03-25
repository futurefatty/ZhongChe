package com.neusoft.zcapplication.flight.internation.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.crcc.commonlib.base.BaseFragment;
import com.crcc.commonlib.utils.UsefulToast;
import com.neusoft.zcapplication.Calendar.CalendarActivity;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.city.CityModel;
import com.neusoft.zcapplication.flight.FlightCityActivity;
import com.neusoft.zcapplication.flight.inland.InlandFlightOrderDetailActivity;
import com.neusoft.zcapplication.flight.internation.InternationFlightListActivity;
import com.neusoft.zcapplication.flight.internation.adapter.InternationJouneyAdapter;
import com.neusoft.zcapplication.flight.internation.model.InternationJourneyModel;
import com.neusoft.zcapplication.tools.DateUtils;
import com.neusoft.zcapplication.tools.SPAppUtil;

import java.util.Date;
import java.util.Locale;

import butterknife.BindView;

import static com.neusoft.zcapplication.flight.inland.InlandFlightOrderDetailActivity.CREATE_ORDER_WHEN;

/**
 * author:Six
 * Date:2019/5/27
 */
public class HomeInternationJourneyFragment extends BaseFragment {
    public static final int START_CITY_CODE = 200;
    public static final int END_CITY_CODE = 201;
    public static final int SELECT_CALENDAR_REQUEST_CODE = 202;
    public static final String TAG = HomeInternationJourneyFragment.class.getSimpleName();
    @BindView(R.id.rec_internation_flight_journey)
    RecyclerView recFlightJourney;
    private InternationJouneyAdapter internationJouneyAdapter;
    private int currentClickPosition;


    public static HomeInternationJourneyFragment newInstance() {
        return new HomeInternationJourneyFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recFlightJourney.setLayoutManager(new LinearLayoutManager(mContext));
        //默认展示缓存的第一段航程
        InternationJourneyModel internationFlightJourney = SPAppUtil.getInternationFlightJourney(mContext);
        if (internationFlightJourney != null) {
            String journeyDateStr = internationFlightJourney.getJourneyDateStr();
            String currentDateStr = DateUtils.formatToDate(new Date(), "yyyy-MM-dd");
            if (!TextUtils.isEmpty(journeyDateStr) && DateUtils.isFirstDayBeforeSecondDay(journeyDateStr, currentDateStr)) {
                internationFlightJourney.setJourneyDate(new Date());
                internationFlightJourney.setJourneyDateStr(currentDateStr);
                internationFlightJourney.setWeek(DateUtils.dateToWeek(currentDateStr));
            }
        } else {
            internationFlightJourney = new InternationJourneyModel();
        }
        internationJouneyAdapter = new InternationJouneyAdapter(R.layout.item_home_internation_flight);
        internationJouneyAdapter.getData().add(internationFlightJourney);
        internationJouneyAdapter.setOnItemChildClickListener((adapter, view1, position) -> {
            InternationJourneyModel journeyModel = internationJouneyAdapter.getData().get(position);
            currentClickPosition = position;
            int id = view1.getId();
            switch (id) {
                case R.id.tv_from_city:
                    Intent intent = new Intent(mContext, FlightCityActivity.class);
                    intent.putExtra(FlightCityActivity.TAB_POSITION, 1);
                    startActivityForResult(intent, START_CITY_CODE);
                    break;
                case R.id.tv_to_city:
                    intent = new Intent(mContext, FlightCityActivity.class);
                    intent.putExtra(FlightCityActivity.TAB_POSITION, 1);
                    startActivityForResult(intent, END_CITY_CODE);
                    break;
                case R.id.iv_journey_exchange:
                    String toCity = journeyModel.getToCity();
                    String toCityCode = journeyModel.getToThreeCode();
                    String fromCity = journeyModel.getFromCity();
                    String fromCityCode = journeyModel.getFromThreeCode();
                    journeyModel.setFromCity(toCity);
                    journeyModel.setFromThreeCode(toCityCode);
                    journeyModel.setToCity(fromCity);
                    journeyModel.setToThreeCode(fromCityCode);
                    internationJouneyAdapter.notifyItemChanged(position);
                    break;
                case R.id.ll_journey_date:
                    String journeyDateStr = journeyModel.getJourneyDateStr();
                    intent = new Intent(mContext, CalendarActivity.class);
                    intent.putExtra("selectType", 1);
                    intent.putExtra("days", 1);
                    if (!TextUtils.isEmpty(journeyDateStr)) {
                        intent.putExtra("firstDay", journeyDateStr);
                    }
                    startActivityForResult(intent, SELECT_CALENDAR_REQUEST_CODE);
                    break;
                case R.id.iv_close:
                    if (internationJouneyAdapter.getData().size() == 1) {
                        UsefulToast.showToast(mContext, "至少需保留一段航程");
                        return;
                    }
                    internationJouneyAdapter.getData().remove(position);
                    internationJouneyAdapter.notifyDataSetChanged();
                    break;
            }
        });
        View footView = View.inflate(mContext, R.layout.item_home_internation_flight_bottom, null);
        footView.findViewById(R.id.tv_add_journey).setOnClickListener(v -> {
            if (internationJouneyAdapter.getData().size() == 8) {
                UsefulToast.showToast(mContext, "最多只能添加8段行程哦");
                return;
            }
            internationJouneyAdapter.addData(new InternationJourneyModel());
        });
        internationJouneyAdapter.addFooterView(footView);
        recFlightJourney.setAdapter(internationJouneyAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            switch (requestCode) {
                case START_CITY_CODE:
                    CityModel cityModel = (CityModel) data.getSerializableExtra(FlightCityActivity.SELECT_CITY_MODEL);
                    InternationJourneyModel internationJourneyModel = internationJouneyAdapter.getData().get(currentClickPosition);
                    internationJourneyModel.setFromCity(cityModel.getCityName());
                    internationJourneyModel.setFromThreeCode(cityModel.getThreeCode());
                    internationJourneyModel.setFromCityIsInternation(2 == cityModel.getCountryTag());
                    internationJouneyAdapter.notifyItemChanged(currentClickPosition);
                    break;
                case END_CITY_CODE:
                    cityModel = (CityModel) data.getSerializableExtra(FlightCityActivity.SELECT_CITY_MODEL);
                    internationJourneyModel = internationJouneyAdapter.getData().get(currentClickPosition);
                    internationJourneyModel.setToCity(cityModel.getCityName());
                    internationJourneyModel.setToThreeCode(cityModel.getThreeCode());
                    internationJourneyModel.setToCityIsInternation(2 == cityModel.getCountryTag());
                    internationJouneyAdapter.notifyItemChanged(currentClickPosition);
                    break;
                case SELECT_CALENDAR_REQUEST_CODE:
                    String firstDay = data.getStringExtra("firstDay");
                    Date date = DateUtils.parseDate(firstDay, "yyyy-MM-dd");
                    internationJourneyModel = internationJouneyAdapter.getData().get(currentClickPosition);
                    internationJourneyModel.setJourneyDate(date);
                    internationJourneyModel.setWeek(DateUtils.dateToWeek(firstDay));
                    internationJourneyModel.setJourneyDateStr(firstDay);
                    internationJouneyAdapter.notifyItemChanged(internationJourneyModel.getPosition());
                    break;
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_home_internation_flight;
    }


    /**
     * 查询国际机票航程
     */
    public void queryInternationFlightJourney() {
        if (checkQueryParamsValidity()) {
            SPAppUtil.setInternationFlightJourney(mContext, internationJouneyAdapter.getData().get(0));
            InternationFlightListActivity.startActivity(mContext, internationJouneyAdapter.getData());
        }
    }


    private boolean checkQueryParamsValidity() {
        for (InternationJourneyModel internationJourneyModel : internationJouneyAdapter.getData()) {
            if (TextUtils.isEmpty(internationJourneyModel.getFromCity())) {
                UsefulToast.showToast(mContext, String.format(Locale.CHINA, "请选择第%d程出发城市",
                        internationJourneyModel.getPosition() + 1));
                return false;
            }
            if (TextUtils.isEmpty(internationJourneyModel.getToCity())) {
                UsefulToast.showToast(mContext, String.format(Locale.CHINA, "请选择第%d程到达城市",
                        internationJourneyModel.getPosition() + 1));
                return false;
            }
            if (TextUtils.isEmpty(internationJourneyModel.getJourneyDateStr())) {
                UsefulToast.showToast(mContext, String.format(Locale.CHINA, "请选择第%d程出发日期",
                        internationJourneyModel.getPosition() + 1));
                return false;
            }
        }

        boolean hasInternationCity = false;
        for (InternationJourneyModel internationJourneyModel : internationJouneyAdapter.getData()) {
            if (internationJourneyModel.isFromCityIsInternation() || internationJourneyModel.isToCityIsInternation()) {
                hasInternationCity = true;
                break;
            }
        }

        if (!hasInternationCity) {
            UsefulToast.showToast(mContext, "需至少选择一个国际/港澳台城市");
            return false;
        }
        return true;
    }
}
