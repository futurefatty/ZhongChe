package com.neusoft.zcapplication.flight.internation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crcc.commonlib.base.SyBaseActivity;
import com.crcc.commonlib.event.Event;
import com.crcc.commonlib.event.EventCode;
import com.crcc.commonlib.loadsir.LoadingCallBack;
import com.crcc.commonlib.utils.StatusBarUtil;
import com.crcc.commonlib.utils.UtilDisplay;
import com.kingja.loadsir.callback.SuccessCallback;
import com.kingja.loadsir.core.LoadSir;
import com.neusoft.zcapplication.Calendar.CalendarActivity;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.flight.FlightApi;
import com.neusoft.zcapplication.flight.FlightNavModel;
import com.neusoft.zcapplication.flight.internation.adapter.InternationFlightListAdapter;
import com.neusoft.zcapplication.flight.internation.model.InternationCurrentJourneyModel;
import com.neusoft.zcapplication.flight.internation.model.InternationJourneyModel;
import com.neusoft.zcapplication.flight.internation.model.InternationSeachModel;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.tools.DateUtils;
import com.neusoft.zcapplication.tools.LoadingUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InternationFlightListActivity extends SyBaseActivity {
    /**
     * 日期选择requestCode
     */
    public static final int SELECT_CALENDAR_REQUEST_CODE = 200;
    /**
     * 跳转到行程详情选择行程方案requestCode
     */
    public static final int SELECT_JOURNEY_PRODUCT_REQUEST_CODE = 300;

    /**
     * 要查询的行程列表
     */
    public static final String INTERNATION_JOURNEY_MODELS = "INTERNATION_JOURNEY_MODELS";

    /**
     * 存储国际机票已添加的行程 key
     */
    public static final String INTERNATION_TRIP_KEY = "INTERNATION_TRIP_KEY";

    /**
     * 当前所查询行程列表索引
     */
    private int currentJoureyIndex;
    /**
     * 当前选择的tab索引
     */
    private int currentTabIndex = -1;

    /**
     * 查询Key，如果此值为空的情况下，会重新查询并且生成新的缓存
     */
    private String searchKey;


    @BindView(R.id.tv_pre_day)
    TextView tvPreDay;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_next_day)
    TextView tvNextDay;
    @BindView(R.id.recycle_list)
    RecyclerView recycleList;
    @BindView(R.id.tabs)
    LinearLayout tabs;
    @BindView(R.id.ll_place_content)
    LinearLayout llPlaceContent;
    private Calendar calendar = Calendar.getInstance();
    private ArrayList<InternationJourneyModel> internationJourneyModels;
    private List<FlightNavModel> flightNavModels;
    private InternationFlightListAdapter internationFlightListAdapter;


    @Override
    protected void initView(Bundle savedInstanceState) {
        new Builder(this)
                .isMuliteView(false)
                .setTitle("").build();
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.colorPrimary), 0);
        ButterKnife.bind(this);
        loadService = LoadSir.getDefault().register(llPlaceContent);
        loadService.getLoadLayout().setupCallback(new JourneyCallBack());
        initView();
    }


    private void showJourneyCallBack() {
        loadService.setCallBack(JourneyCallBack.class, (context, view) -> {
            TextView btnJourney = (TextView) view.findViewById(R.id.tv_continue_query_next_journey);
            if (isLastJourney()) {
                btnJourney.setText("继续申请行程方案");
            } else {
                btnJourney.setText("继续查询下一行程");
            }
            btnJourney.setOnClickListener(v -> {
                boolean lastJourney = isLastJourney();
                int journeyCount = internationJourneyModels.size();
                if (!InternationTripUtil.canAdd(journeyCount)) {
                    InternationTripUtil.startApprovalActivity(InternationFlightListActivity.this);
                    return;
                }
                InternationTripUtil.addTrip(internationJourneyModels.get(currentJoureyIndex));
                if (lastJourney) {
                    //申请行程方案
                    InternationTripUtil.startApprovalActivity(InternationFlightListActivity.this);
                } else {
                    this.currentJoureyIndex++;
                    setJourneyData();
                    //查询下一航班
                    loadData(false);
                }
            });
        });
    }


    private void loadData(boolean isFirstLoad) {
        //默认时间排序
//        changeTabSelectIndex(1);
        clearTabSelectStatus();
        String currentDateStr = DateUtils.formatToDate(new Date(), "yyyy-MM-dd");
        if (currentDateStr.equals(internationJourneyModels.get(currentJoureyIndex).getJourneyDateStr())) {
            tvPreDay.setEnabled(false);
        } else {
            tvPreDay.setEnabled(true);
        }
        if (isFirstLoad) {
            loadService.showCallback(LoadingCallBack.class);
        } else {
            LoadingUtil.showLoading(this);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("TripType", 0);
        params.put("JourneyType", 1);
        params.put("NeedLowestPrice", true);
        params.put("JourneyCities", internationJourneyModels.get(currentJoureyIndex).obtainRequestParams());
        params.put("AdultNum", 1);
        for (int tabIndex = 0; tabIndex < flightNavModels.size(); tabIndex++) {
            FlightNavModel flightNavModel = flightNavModels.get(tabIndex);
            params.put(flightNavModel.getRequestKey(), flightNavModel.getRequestValue());
        }
        RetrofitFactory.getInstance().createApi(FlightApi.class)
                .getInterFlightAirlineSearch(params)
                .enqueue(new CallBack<InternationSeachModel>() {


                    @Override
                    public void success(InternationSeachModel response) {
                        if (!isFirstLoad) {
                            LoadingUtil.dismissLoading();
                        }
                        if (response == null || response.getSchedules() == null || response.getSchedules().isEmpty()) {
                            showJourneyCallBack();
                            loadService.showCallback(JourneyCallBack.class);
                            return;
                        }
                        searchKey = response.getSearchKey();
                        String adultTicketAddedTaxPrice = response.getAdultTicketAddedTaxPrice();
                        if (!TextUtils.isEmpty(adultTicketAddedTaxPrice)) {
                            tvMoney.setText(String.format("￥%s", adultTicketAddedTaxPrice));
                        }
                        List<InternationSeachModel.Schedule> schedules = response.getSchedules();
                        int headerLayoutCount = internationFlightListAdapter.getHeaderLayoutCount();
                        if (0 == headerLayoutCount) {
                            internationFlightListAdapter.addHeaderView(View.inflate(InternationFlightListActivity.this,
                                    R.layout.layout_internation_flight_list_top_hint, null));
                        }
                        internationFlightListAdapter.setNewData(schedules);
                        loadService.showCallback(SuccessCallback.class);
                    }

                    @Override
                    public void fail(String code) {
                        if (!isFirstLoad) {
                            LoadingUtil.dismissLoading();
                        }
                        showJourneyCallBack();
                        loadService.showCallback(JourneyCallBack.class);
                    }
                });
    }

    private void clearTabSelectStatus() {
        currentTabIndex = -1;
        for (int tabIndex = 0; tabIndex < tabs.getChildCount(); tabIndex++) {
            FlightNavModel flightNavModel = flightNavModels.get(tabIndex);
            flightNavModel.setChangePositionStatus(0);
            View customLayout = tabs.getChildAt(tabIndex);
            ImageView ivImg = (ImageView) customLayout.findViewById(R.id.iv_img);
            TextView tvText = (TextView) customLayout.findViewById(R.id.tv_text);
            FlightNavModel indexModel = flightNavModels.get(tabIndex);
            tvText.setText(indexModel.getNomalText());
            ivImg.setImageResource(indexModel.getUnResId());
            tvText.setTextColor(ContextCompat.getColor(InternationFlightListActivity.this, R.color.C727881));
        }
    }

    /**
     * 是否是最后一个行程
     *
     * @return
     */
    private boolean isLastJourney() {
        int currentJoureyIndex = this.currentJoureyIndex + 1;
        if (currentJoureyIndex > internationJourneyModels.size() - 1) {
            return true;
        }
        return false;
    }

    private String getTitle(InternationJourneyModel internationJourneyModel) {
        return String.format("行程%d : %s-%s", internationJourneyModel.getPosition() + 1, internationJourneyModel.getFromCity(), internationJourneyModel.getToCity());
    }

    private void initView() {
        internationJourneyModels = (ArrayList<InternationJourneyModel>) getIntent().getSerializableExtra(INTERNATION_JOURNEY_MODELS);
        setJourneyData();

        flightNavModels = new ArrayList<>();
        FlightNavModel flightNavModel1 = new FlightNavModel();
        flightNavModel1.setUnResId(R.mipmap.icon_direct_fly);
        flightNavModel1.setSelectResId(R.mipmap.icon_select_direct_fly);
        flightNavModel1.setNomalText("直飞优先");
        flightNavModel1.setSelectTexts(new String[]{"直飞优先", "直飞优先"});
        flightNavModel1.setChangePositionStatus(0);
        flightNavModel1.setRequestKey("FilterTransferAndDirect");
        flightNavModel1.setComparators(new Comparator[]{InternationSeachModel.Schedule.TIMEASC_DIRECT, InternationSeachModel.Schedule.TIMEASC_DIRECT});
        flightNavModel1.setRequestValue(0);
        flightNavModels.add(flightNavModel1);


        FlightNavModel flightNavModel2 = new FlightNavModel();
        flightNavModel2.setUnResId(R.mipmap.icon_flight_list_nav_time);
        flightNavModel2.setSelectResId(R.mipmap.icon_flight_list_nav_select_time);
        flightNavModel2.setNomalText("时间排序");
        flightNavModel2.setSelectTexts(new String[]{"从早到晚", "从晚到早"});
        flightNavModel2.setChangePositionStatus(0);
        flightNavModel2.setRequestKey("FilterOrderTime");
        flightNavModel2.setRequestValue(1);
        flightNavModel2.setComparators(new Comparator[]{InternationSeachModel.Schedule.TIMEASC,
                InternationSeachModel.Schedule.TIMEDES});
        flightNavModels.add(flightNavModel2);

        FlightNavModel flightNavModel3 = new FlightNavModel();
        flightNavModel3.setUnResId(R.mipmap.icon_flight_list_nav_price);
        flightNavModel3.setSelectResId(R.mipmap.icon_flight_list_nav_select_price);
        flightNavModel3.setNomalText("价格排序");
        flightNavModel3.setSelectTexts(new String[]{"从低到高", "从高到低"});
        flightNavModel3.setChangePositionStatus(0);
        flightNavModel3.setRequestKey("FilterOrderPrice");
        flightNavModel3.setRequestValue(0);
        flightNavModel3.setComparators(new Comparator[]{InternationSeachModel.Schedule.TIMEASC_PRICEASC,
                InternationSeachModel.Schedule.TIMEASC_PRICEDES});
        flightNavModels.add(flightNavModel3);

        for (int tabIndex = 0; tabIndex < flightNavModels.size(); tabIndex++) {
            View customLayout = LayoutInflater.from(this).inflate(R.layout.layout_flight_list_nav, null);
            ImageView ivImg = (ImageView) customLayout.findViewById(R.id.iv_img);
            ivImg.setImageResource(flightNavModels.get(tabIndex).getUnResId());
            TextView tvText = (TextView) customLayout.findViewById(R.id.tv_text);
            tvText.setText(flightNavModels.get(tabIndex).getNomalText());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            layoutParams.gravity = Gravity.CENTER;
            customLayout.setLayoutParams(layoutParams);
            tabs.addView(customLayout);
            customLayout.setTag(tabIndex);
            customLayout.setOnClickListener(v -> {
                int index = (int) v.getTag();
                changeTabSelectIndex(index);
                //排序
                List<InternationSeachModel.Schedule> schedules = internationFlightListAdapter.getData();
                FlightNavModel flightNavModel = flightNavModels.get(index);
                Comparator comparator = flightNavModel.getComparators()[flightNavModel.getChangePositionStatus()];
                Collections.sort(schedules, comparator);
                internationFlightListAdapter.notifyDataSetChanged();
            });
        }

        internationFlightListAdapter = new InternationFlightListAdapter
                (R.layout.item_internation_flight_list);
        internationFlightListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            int id = view.getId();
            switch (id) {
                case R.id.tv_look:
                    InternationJourneyModel currentJourneyModel = internationJourneyModels.get(currentJoureyIndex);
                    InternationSeachModel.Schedule schedule = internationFlightListAdapter.getData().get(position);
                    InternationCurrentJourneyModel currentJourney = schedule.getCurrentJourney();
                    InternationJourneyDetailActivity.startJourneyDetailActivity(
                            InternationFlightListActivity.this,
                            getTitle(currentJourneyModel),
                            currentJourneyModel,
                            currentJourney,
                            searchKey,
                            isLastJourney(),
                            internationJourneyModels.size(),
                            SELECT_JOURNEY_PRODUCT_REQUEST_CODE);
                    break;
            }
        });
        int bottom = UtilDisplay.dip2px(this, 8);
        recycleList.setLayoutManager(new LinearLayoutManager(this));
        recycleList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int adapterPosition = parent.getChildAdapterPosition(view);
                if (InternationFlightListAdapter.HEADER_VIEW != recycleList.getAdapter().getItemViewType(adapterPosition)) {
                    outRect.set(0, 0, 0, bottom);
                }
            }
        });
        recycleList.setAdapter(internationFlightListAdapter);

        //请求获取数据 默认从早到晚
        loadData(true);
    }

    private void setJourneyData() {
        InternationJourneyModel internationJourneyModel = internationJourneyModels.get(currentJoureyIndex);
        String monthAndDay = DateUtils.formatToDate(internationJourneyModel.getJourneyDate(), "MM-dd");
        commTitleTitle.setText(getTitle(internationJourneyModel));
        tvDate.setText(monthAndDay);
        tvDate.setTag(internationJourneyModel);
    }

    private void changeTabSelectIndex(int index) {
        FlightNavModel flightNavModel = flightNavModels.get(index);
        if (index == currentTabIndex) {
            flightNavModel.setChangePositionStatus(flightNavModel.getChangePositionStatus() == 0 ? 1 : 0);
        } else {
            flightNavModel.setChangePositionStatus(0);
        }
        for (int tabIndex = 0; tabIndex < tabs.getChildCount(); tabIndex++) {
            View customLayout = tabs.getChildAt(tabIndex);
            ImageView ivImg = (ImageView) customLayout.findViewById(R.id.iv_img);
            TextView tvText = (TextView) customLayout.findViewById(R.id.tv_text);
            FlightNavModel indexModel = flightNavModels.get(tabIndex);
            if (index == tabIndex) {
                String text = indexModel.getSelectTexts()[indexModel.getChangePositionStatus()];
                tvText.setText(text);
                ivImg.setImageResource(flightNavModels.get(index).getSelectResId());
                tvText.setTextColor(ContextCompat.getColor(InternationFlightListActivity.this, R.color.colorPrimary));
            } else {
                tvText.setText(indexModel.getNomalText());
                ivImg.setImageResource(indexModel.getUnResId());
                tvText.setTextColor(ContextCompat.getColor(InternationFlightListActivity.this, R.color.C727881));
            }
        }
        currentTabIndex = index;
    }


    public static void startActivity(Context context, List<InternationJourneyModel> internationJourneyModels) {
        Intent intent = new Intent(context, InternationFlightListActivity.class);
        intent.putExtra(INTERNATION_JOURNEY_MODELS, (ArrayList<InternationJourneyModel>) internationJourneyModels);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_internation_flight_list;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case SELECT_CALENDAR_REQUEST_CODE:
                    String firstDay = data.getStringExtra("firstDay");
                    InternationJourneyModel internationJourneyModel = (InternationJourneyModel) tvDate.getTag();
                    internationJourneyModel.setJourneyDateStr(firstDay);
                    Date date = DateUtils.parseDate(firstDay, "yyyy-MM-dd");
                    internationJourneyModel.setJourneyDate(date);
                    internationJourneyModel.setWeek(DateUtils.dateToWeek(firstDay));
                    tvDate.setText(DateUtils.formatToDate(date, "MM-dd"));
                    tvDate.setTag(internationJourneyModel);
                    loadData(false);
                    break;
                case SELECT_JOURNEY_PRODUCT_REQUEST_CODE:
                    //在航班信息页面选择参考完后查询下一行程
                    this.currentJoureyIndex++;
                    setJourneyData();
                    loadData(false);
                    break;
            }
        }
    }


    @OnClick({R.id.tv_pre_day, R.id.tv_next_day, R.id.ll_select_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_pre_day:
                InternationJourneyModel internationJourneyModel = updateJourneyModelOfDate(-1);
                internationJourneyModels.set(currentJoureyIndex, internationJourneyModel);
                loadData(false);
                break;
            case R.id.tv_next_day:
                internationJourneyModel = updateJourneyModelOfDate(1);
                internationJourneyModels.set(internationJourneyModel.getPosition(), internationJourneyModel);
                loadData(false);
                break;
            case R.id.ll_select_date:
                internationJourneyModel = (InternationJourneyModel) tvDate.getTag();
                String journeyDateStr = internationJourneyModel.getJourneyDateStr();
                Intent intent = new Intent(this, CalendarActivity.class);
                intent.putExtra("selectType", 1);
                intent.putExtra("days", 1);
                intent.putExtra("firstDay", journeyDateStr);
                startActivityForResult(intent, SELECT_CALENDAR_REQUEST_CODE);
                break;
        }
    }

    /**
     * 修改数据模型中日期
     *
     * @param position -1 前一天 1后一天
     * @return 修改后的数据模型
     */
    private InternationJourneyModel updateJourneyModelOfDate(int position) {
        InternationJourneyModel internationJourneyModel = (InternationJourneyModel) tvDate.getTag();
        Date handleBeforeDate = internationJourneyModel.getJourneyDate();
        calendar.setTime(handleBeforeDate);
        int day = calendar.get(Calendar.DATE);
        calendar.set(Calendar.DATE, day + position);
        internationJourneyModel.setJourneyDate(calendar.getTime());
        String journeyDateStr = DateUtils.formatToDate(internationJourneyModel.getJourneyDate(),
                "yyyy-MM-dd");
        internationJourneyModel.setJourneyDateStr(journeyDateStr);
        internationJourneyModel.setWeek(DateUtils.dateToWeek(journeyDateStr));
        tvDate.setText(DateUtils.formatToDate(calendar.getTime(), "MM-dd"));
        tvDate.setTag(internationJourneyModel);
        return internationJourneyModel;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        InternationTripUtil.reset();
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
}
