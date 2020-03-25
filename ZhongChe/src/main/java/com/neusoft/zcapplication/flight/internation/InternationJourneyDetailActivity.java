package com.neusoft.zcapplication.flight.internation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crcc.commonlib.base.SyBaseActivity;
import com.crcc.commonlib.event.Event;
import com.crcc.commonlib.event.EventCode;
import com.crcc.commonlib.loadsir.EmptyCallBack;
import com.crcc.commonlib.loadsir.LoadingCallBack;
import com.crcc.commonlib.loadsir.NoNetWorkCallBack;
import com.crcc.commonlib.model.MapperModel;
import com.crcc.commonlib.utils.BigDecimalUtil;
import com.crcc.commonlib.utils.SpanUtils;
import com.crcc.commonlib.utils.StatusBarUtil;
import com.crcc.commonlib.utils.UtilDisplay;
import com.crcc.commonlib.view.DashView;
import com.kingja.loadsir.callback.SuccessCallback;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.flight.FlightApi;
import com.neusoft.zcapplication.flight.dialog.FlightTicketChangingDialog;
import com.neusoft.zcapplication.flight.internation.adapter.JourenyFlowDetailAdapter;
import com.neusoft.zcapplication.flight.internation.model.InterFlightJourneyDetailModel;
import com.neusoft.zcapplication.flight.internation.model.InternationCurrentJourneyModel;
import com.neusoft.zcapplication.flight.internation.model.InternationJourneyFlowModel;
import com.neusoft.zcapplication.flight.internation.model.InternationJourneyModel;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.tools.DateUtils;
import com.neusoft.zcapplication.tools.NetWorkUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InternationJourneyDetailActivity extends SyBaseActivity {
    /**
     * 标题
     */
    private static final String TITLE = "TITLE";
    /**
     * 当前行程数据模型
     */
    private static final String INTERNATION_JOURNEY_MODEL = "INTERNATION_JOURNEY_MODEL";
    /**
     * 当前航班数据模型
     */
    public static final String INTERNATION_CURRENT_JOURNEY_MODELS = "INTERNATION_CURRENT_JOURNEY_MODELS";
    /**
     * 是否是最后一段行程
     */
    public static final String IS_LAST_JOURNEY = "IS_LAST_JOURNEY";

    /**
     * 查询机票信息的SearchKey;
     */
    public static final String SEARCH_KEY = "SEARCH_KEY";

    /**
     * 查询行程的数量
     */
    public static final String JOURNEY_COUNT = "JOURNEY_COUNT";

    /**
     * 当前航班数据模型
     */
    private InternationCurrentJourneyModel currentJourneyModel;
    /**
     * 退改签规则弹窗
     */
    private FlightTicketChangingDialog flightTicketChangingDialog;
    /**
     * 当前行程数据模型
     */
    private InternationJourneyModel internationJourneyModel;


    @BindView(R.id.tv_journey_date)
    TextView tvJourneyDate;
    @BindView(R.id.recycle_joureny_flow)
    RecyclerView recycleJourenyFlow;
    @BindView(R.id.dash_view)
    DashView dashView;
    @BindView(R.id.ll_joureny_notice)
    LinearLayout llJourentNotice;
    @BindView(R.id.tv_cost_detail_title)
    TextView tvCostDetailTitle;
    @BindView(R.id.ll_fee_detail)
    LinearLayout llFeeDetail;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.tv_journey_time)
    TextView tvJourneyTime;
    @BindView(R.id.tv_select_program)
    TextView tvSelectProgram;
    private static final int ADULT_NUM = 1;
    private JourenyFlowDetailAdapter jourenyFlowDetailAdapter;
    @BindView(R.id.tv_refund_change_explain)
    TextView tvRefundChangeExplain;


    @Override
    protected void initView(Bundle savedInstanceState) {
        new Builder(this)
                .setTitle(getIntent().getStringExtra(TITLE))
                .build();
        ButterKnife.bind(this);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.colorPrimary), 0);
        initView();
        loadData();
    }


    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        if (EventCode.INNTEAR_FLIGHT_APPLY_ORDER == event.getCode()) {
            finish();
        }
    }

    private void initView() {
        recycleJourenyFlow.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        jourenyFlowDetailAdapter = new JourenyFlowDetailAdapter();
        recycleJourenyFlow.setAdapter(jourenyFlowDetailAdapter);
        boolean isLastJourney = getIntent().getBooleanExtra(IS_LAST_JOURNEY, false);
        tvSelectProgram.setTag(isLastJourney);
        tvSelectProgram.setText(isLastJourney ? "申请行程方案" : "选为参考");
    }

    private void loadData() {
        loadService.showCallback(LoadingCallBack.class);
        internationJourneyModel = (InternationJourneyModel) getIntent().getSerializableExtra(INTERNATION_JOURNEY_MODEL);
        currentJourneyModel = (InternationCurrentJourneyModel) getIntent().getSerializableExtra(INTERNATION_CURRENT_JOURNEY_MODELS);
        List<InternationJourneyFlowModel> internationJourneyFlowModels = getInternationJourneyFlowModels(currentJourneyModel.getFlightSegments());
        jourenyFlowDetailAdapter.setNewData(internationJourneyFlowModels);
        tvJourneyDate.setText(String.format("%s    %s",
                DateUtils.formatToDate(internationJourneyModel.getJourneyDate(), "MM月dd日"),
                internationJourneyModel.getWeek()));
        String minuteToHour = DateUtils.minuteToHour(currentJourneyModel.getDuration());
        if (TextUtils.isEmpty(minuteToHour)) {
            tvJourneyTime.setVisibility(View.GONE);
        } else {
            tvJourneyTime.setText(minuteToHour);
            tvJourneyTime.setVisibility(View.VISIBLE);
        }
        Map<String, Object> params = new HashMap<>();
        String searchKey = getIntent().getStringExtra(SEARCH_KEY);
        if (!TextUtils.isEmpty(searchKey)) {
            params.put("SearchKey", searchKey);
        }
        params.put("ProductKey", currentJourneyModel.getProductKey());
        params.put("DepAndArrCities", internationJourneyModel.obtainRequestParams());
        params.put("TripType", 0);
        params.put("JourneyType", 1);
        params.put("AdultNum", ADULT_NUM);
        RetrofitFactory.getInstance().createApi(FlightApi.class)
                .getInterFlightJourneyDetail(params)
                .enqueue(new CallBack<InterFlightJourneyDetailModel>() {
                    @Override
                    public void success(InterFlightJourneyDetailModel response) {
                        if (response == null) {
                            loadService.showCallback(EmptyCallBack.class);
                            return;
                        }
                        List<String> bookingTips = response.getBookingTips();
                        if (bookingTips == null || bookingTips.isEmpty()) {
                            dashView.setVisibility(View.GONE);
                            llJourentNotice.setVisibility(View.GONE);
                        } else {
                            for (String s : bookingTips) {
                                TextView textView = new TextView(InternationJourneyDetailActivity.this);
                                textView.setTextSize(11);
                                textView.setText(s);
                                textView.setTextColor(ContextCompat.getColor(InternationJourneyDetailActivity.this, R.color.C333333));
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                                layoutParams.topMargin = UtilDisplay.dip2px(InternationJourneyDetailActivity.this, 10);
                                llJourentNotice.addView(textView, layoutParams);
                            }
                        }
                        setFeeDetail(response);
                        tvRefundChangeExplain.setTag(response);
                        loadService.showCallback(SuccessCallback.class);
                    }

                    @Override
                    public void fail(String code) {
                        if (!NetWorkUtil.isConnected(InternationJourneyDetailActivity.this)) {
                            loadService.showCallback(NoNetWorkCallBack.class);
                        } else {
                            loadService.showCallback(EmptyCallBack.class);
                        }
                    }
                });
    }

    private void setFeeDetail(InterFlightJourneyDetailModel response) {
        //票面价
        BigDecimal adultTicketPrice = response.getAdultTicketPrice();
        if (adultTicketPrice != null) {
            String tadultTicketPriceStr = BigDecimalUtil.clearNoUseZeroForBigDecimal(adultTicketPrice).
                    toPlainString();
            addFeeItem("票面价", String.format("￥%s", tadultTicketPriceStr));
        }
        //税费
        BigDecimal adultTaxPrice = response.getAdultTaxPrice();
        if (adultTicketPrice != null) {
            String adultTaxPriceStr = BigDecimalUtil.clearNoUseZeroForBigDecimal(adultTaxPrice).
                    toPlainString();
            addFeeItem("税费", String.format("￥%s", adultTaxPriceStr));
        }
        //服务费
        BigDecimal serviceFee = response.getTotalServiceFee();
        if (serviceFee != null) {
            addFeeItem("服务费", String.format("￥%s", BigDecimalUtil.clearNoUseZeroForBigDecimal(serviceFee).toPlainString()));
        }
        BigDecimal totalPrice = response.getTotalPrice();
        if (totalPrice != null) {
            String totalPriceStr = BigDecimalUtil.clearNoUseZeroForBigDecimal(totalPrice).toPlainString();
            tvTotalPrice.setText(String.format("￥%s", totalPriceStr));
        }
    }

    private void addFeeItem(String key, String value) {
        int childCount = llFeeDetail.getChildCount();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        if (childCount > 0) {
            layoutParams.topMargin = UtilDisplay.dip2px(InternationJourneyDetailActivity.this, 12);
        }
        View view = View.inflate(InternationJourneyDetailActivity.this, R.layout.item_fee_detail, null);
        TextView tvFeeKey = (TextView) view.findViewById(R.id.tv_fee_key);
        TextView tvFeeValue = (TextView) view.findViewById(R.id.tv_fee_value);
        tvFeeKey.setText(key);
        tvFeeValue.setText(value);
        llFeeDetail.addView(view, layoutParams);
    }


    /**
     * 获取航段信息流
     *
     * @param flightSegmentsBeans
     */
    private List<InternationJourneyFlowModel> getInternationJourneyFlowModels(List<InternationCurrentJourneyModel.FlightSegmentsBean>
                                                                                      flightSegmentsBeans) {
        if (flightSegmentsBeans == null | flightSegmentsBeans.isEmpty()) {
            return new ArrayList<>();
        }
        int flightSegmentsSize = flightSegmentsBeans.size();
        List<InternationJourneyFlowModel> internationJourneyFlowModels = new ArrayList<>();
        //直达
        if (flightSegmentsSize == 1) {
            InternationCurrentJourneyModel.FlightSegmentsBean flightSegmentsBean = flightSegmentsBeans.get(0);
            InternationJourneyFlowModel flowModel = getFlowModel(flightSegmentsBean);
            flowModel.setItemType(InternationJourneyFlowModel.NOT_TRANSFER);
            internationJourneyFlowModels.add(flowModel);
        } else {
            //至少一次中转
            for (int index = 0; index < flightSegmentsBeans.size(); index++) {
                InternationCurrentJourneyModel.FlightSegmentsBean lastFlightSegmentsBean = flightSegmentsBeans.get(index);
                InternationJourneyFlowModel flowModel = getFlowModel(lastFlightSegmentsBean);
                flowModel.setItemType(InternationJourneyFlowModel.NOT_TRANSFER);
                String lastArrTime = lastFlightSegmentsBean.getArrTime();
                String lastArrCityName = lastFlightSegmentsBean.getArrCityName();
                internationJourneyFlowModels.add(flowModel);

                //中转
                if (index != flightSegmentsBeans.size() - 1) {
                    int nextIndex = index + 1;
                    InternationJourneyFlowModel transfer = new InternationJourneyFlowModel();
                    InternationCurrentJourneyModel.FlightSegmentsBean nextFlightSegmentsBean = flightSegmentsBeans.get(nextIndex);
                    String depTime = nextFlightSegmentsBean.getDepTime();
                    long second = DateUtils.differTimeWithMsec(lastArrTime, depTime) / 1000;
                    transfer.setTransferName(String.format("中转    %s    %s",
                            lastArrCityName, second / 3600 + "小时" + (second / 60) % 60 + "分钟"));
                    transfer.setItemType(InternationJourneyFlowModel.TRANSFER);
                    internationJourneyFlowModels.add(transfer);
                }
            }
        }
        return internationJourneyFlowModels;
    }


    private InternationJourneyFlowModel getFlowModel(InternationCurrentJourneyModel.FlightSegmentsBean flightSegmentsBean) {
        InternationJourneyFlowModel flowModel = new InternationJourneyFlowModel();
        flowModel.setDepAirportName(flightSegmentsBean.getDepAirportName());
        flowModel.setArrAirportName(flightSegmentsBean.getArrAirportName());
        flowModel.setDepTime(flightSegmentsBean.getDepTime());
        flowModel.setArrTime(flightSegmentsBean.getArrTime());
        flowModel.setMarketingCompanyLogo(flightSegmentsBean.getMarketingCompanyLogo());
        flowModel.setMarketingCompanyName(flightSegmentsBean.getMarketingCompanyName());
        flowModel.setAircraftName(flightSegmentsBean.getAircraftName());
        flowModel.setAircraftType(flightSegmentsBean.getAircraftType());
        flowModel.setDepTerm(flightSegmentsBean.getDepTerm());
        flowModel.setArrTerm(flightSegmentsBean.getArrTerm());
        flowModel.setStopCityName(flightSegmentsBean.getStopCityName());
        flowModel.setMarketingFlightNo(flightSegmentsBean.getMarketingFlightNo());
        return flowModel;
    }


    /**
     * @param activity                       被打开的activity对象
     * @param title                          打开的activity页面标题
     * @param internationJourneyModel        当前行程数据模型
     * @param internationCurrentJourneyModel 当前航班数据模型
     * @param searchKey                      查询Key，如果此值为空的情况下，会重新查询并且生成新的缓存
     * @param isLastJourney                  是否是最后一个行程
     */
    public static void startJourneyDetailActivity(Activity activity, String title, InternationJourneyModel internationJourneyModel,
                                                  InternationCurrentJourneyModel internationCurrentJourneyModel,
                                                  String searchKey,
                                                  boolean isLastJourney,
                                                  int journeyCount,
                                                  int requestCode) {
        Intent intent = new Intent(activity, InternationJourneyDetailActivity.class);
        intent.putExtra(TITLE, title);
        intent.putExtra(SEARCH_KEY, searchKey);
        intent.putExtra(INTERNATION_JOURNEY_MODEL, internationJourneyModel);
        intent.putExtra(INTERNATION_CURRENT_JOURNEY_MODELS, internationCurrentJourneyModel);
        intent.putExtra(IS_LAST_JOURNEY, isLastJourney);
        intent.putExtra(JOURNEY_COUNT, journeyCount);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_internation_journey_detailctivity;
    }


    @OnClick({R.id.tv_refund_change_explain, R.id.tv_select_program})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_refund_change_explain:
                InterFlightJourneyDetailModel journeyDetailModel = (InterFlightJourneyDetailModel) view.getTag();
                if (flightTicketChangingDialog == null) {
                    flightTicketChangingDialog = new FlightTicketChangingDialog(this);
                    flightTicketChangingDialog.setBaggagConsignRule(journeyDetailModel.getBaggageRule());
                    List<MapperModel> mapperModels = new ArrayList<>();
                    MapperModel mapperModel1 = new MapperModel();
                    mapperModel1.setKey("舱    位:");
                    mapperModel1.setValue(currentJourneyModel.getCabinClassName());
                    mapperModels.add(mapperModel1);
                    BigDecimal adultTicketPrice = journeyDetailModel.getAdultTicketPrice();
                    if (adultTicketPrice != null) {
                        MapperModel mapperModel2 = new MapperModel();
                        mapperModel2.setKey("票面价:");
                        SpanUtils spanUtils = new SpanUtils();
                        SpannableStringBuilder sb = spanUtils
                                .append(adultTicketPrice.stripTrailingZeros().toPlainString())
                                .setForegroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
                                .append("元（不含税）")
                                .setForegroundColor(ContextCompat.getColor(this, R.color.C333333))
                                .create();
                        mapperModel2.setValue(sb);
                        mapperModels.add(mapperModel2);
                    }
                    String refundTicketRule = journeyDetailModel.getRefundTicketRule();
                    if (!TextUtils.isEmpty(refundTicketRule)) {
                        MapperModel mapperModel3 = new MapperModel();
                        mapperModel3.setKey("退    票:");
                        mapperModel3.setValue(refundTicketRule);
                        mapperModels.add(mapperModel3);
                    }

                    String modifyTicketRule = journeyDetailModel.getModifyTicketRule();
                    if (!TextUtils.isEmpty(modifyTicketRule)) {
                        MapperModel mapperModel4 = new MapperModel();
                        mapperModel4.setKey("改    签:");
                        mapperModel4.setValue(modifyTicketRule);
                        mapperModels.add(mapperModel4);
                    }
                    flightTicketChangingDialog.setTicketChangingInfo(mapperModels);
                }
                flightTicketChangingDialog.show();
                break;
            case R.id.tv_select_program:
                boolean isLastJourney = (boolean) view.getTag();
                int journeyCount = getIntent().getIntExtra(JOURNEY_COUNT, 0);
                if (!InternationTripUtil.canAdd(journeyCount)) {
                    InternationTripUtil.startApprovalActivity(this);
                    return;
                }
                InternationTripUtil.addTrip(currentJourneyModel);
                if (isLastJourney) {
                    InternationTripUtil.startApprovalActivity(this);
                } else {
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
        }
    }


}
