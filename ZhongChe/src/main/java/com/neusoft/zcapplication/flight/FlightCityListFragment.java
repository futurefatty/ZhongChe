package com.neusoft.zcapplication.flight;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.crcc.commonlib.base.LazyFragment;
import com.crcc.commonlib.event.Event;
import com.crcc.commonlib.event.EventBusUtil;
import com.crcc.commonlib.event.EventCode;
import com.crcc.commonlib.loadsir.EmptyCallBack;
import com.crcc.commonlib.loadsir.LoadingCallBack;
import com.crcc.commonlib.loadsir.NoNetWorkCallBack;
import com.crcc.commonlib.loadsir.RetryCallBack;
import com.crcc.commonlib.utils.UsefulToast;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.callback.SuccessCallback;
import com.kingja.loadsir.core.LoadLayout;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.city.CityAdapter;
import com.neusoft.zcapplication.city.CityListModel;
import com.neusoft.zcapplication.city.CityModel;
import com.neusoft.zcapplication.city.CityUtil;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.tools.NetWorkUtil;
import com.neusoft.zcapplication.tools.SPAppUtil;
import com.neusoft.zcapplication.widget.SideBar;
import com.neusoft.zcapplication.widget.recycleview.expand.ExpandGroupItemEntity;
import com.neusoft.zcapplication.widget.recycleview.pinned.PinnedHeaderItemDecoration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * author:Six
 * Date:2019/5/16
 */
public class FlightCityListFragment extends LazyFragment {

    public static final String FLIGHT_CITY_TYPE = "FLIGHT_CITY_TYPE";
    /**
     * 国内
     */
    public static final int INLAND = 0;
    /**
     * 国际
     */
    public static final int INTERNATION = 1;


    private int flightCityType;


    @BindView(R.id.recycle_city_list)
    RecyclerView recycleCityList;
    @BindView(R.id.tv_dialog)
    TextView tvDialog;
    @BindView(R.id.side_bar)
    SideBar sideBar;


    @IntDef({INLAND, INTERNATION})

    @Target({
            ElementType.FIELD,
            ElementType.METHOD,
            ElementType.PARAMETER,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface FlightCityType {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            flightCityType = arguments.getInt(FLIGHT_CITY_TYPE);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public static FlightCityListFragment newInstance(@FlightCityType int flightCityType) {
        FlightCityListFragment flightCityListFragment = new FlightCityListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FLIGHT_CITY_TYPE, flightCityType);
        flightCityListFragment.setArguments(bundle);
        return flightCityListFragment;
    }


    @Override
    public void fetchData() {
        switch (flightCityType) {
            case INLAND:
                loadCity(1);
                break;
            case INTERNATION:
                loadCity(2);
                break;
        }
    }

    @Override
    protected boolean isNeedLoadSir() {
        return true;
    }

    private void loadCity(int Country) {
        loadService.setCallBack(LoadingCallBack.class, (context, view) -> {
            TextView tvLoadingText = (TextView) view.findViewById(R.id.tv_loading_text);
            tvLoadingText.setText("城市信息获取中，请稍候...");
        });
        loadService.showCallback(LoadingCallBack.class);
        Map<String, Object> params = new HashMap<>();
        params.put("Country", Country);
        RetrofitFactory.getInstance().createApi(FlightApi.class)
                .getInternationFlightCity(params).enqueue(new CallBack<List<CityModel>>() {
            @Override
            public void success(List<CityModel> response) {
                if (response == null || response.isEmpty()) {
                    loadService.showCallback(EmptyCallBack.class);
                } else {
                    handleCityModels(response);
                }
            }

            @Override
            public void fail(String code) {
                if (!NetWorkUtil.isConnected(mContext)) {
                    loadService.showCallback(NoNetWorkCallBack.class);
                } else {
                    loadService.showCallback(RetryCallBack.class);
                }
            }
        });
    }


    @Override
    protected void onReload(View view) {
        fetchData();
    }

    private void handleCityModels(List<CityModel> response) {
        Observable.just(response)
                .map(cityModels -> {
                    CityListModel cityListModel = new CityListModel();
                    List<CityModel> cityModelHostorys = null;
                    switch (flightCityType) {
                        case INLAND:
                            cityModelHostorys = SPAppUtil.getFlightCityHistory(mContext);
                            break;
                        case INTERNATION:
                            cityModelHostorys = SPAppUtil.getInternationFlightCityHistory(mContext);
                            break;
                    }
                    if (!cityModelHostorys.isEmpty()) {
                        cityListModel.setHistoryCityModels(cityModelHostorys);
                    }
                    List<CityModel> cityModelHots = new ArrayList<>();
                    for (CityModel cityModel : cityModels) {
                        if (1 == cityModel.getIsHot()) {
                            cityModelHots.add(cityModel);
                        }
                    }
                    cityListModel.setHotCityModels(cityModelHots);
                    cityModels.remove(cityModelHots);
                    cityListModel.setExpandGroupItemEntities(CityUtil.sort(cityModels));
                    return cityListModel;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CityListModel>() {


                    @Override
                    public void onSubscribe(Disposable d) {
                        addSubscription(d);
                    }

                    @Override
                    public void onNext(CityListModel cityListModel) {
                        CityAdapter cityAdapter = new CityAdapter(cityListModel);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                        recycleCityList.setLayoutManager(linearLayoutManager);
                        recycleCityList.addItemDecoration(new PinnedHeaderItemDecoration());
                        sideBar.setOnTouchingLetterChangedListener((s, pos) -> linearLayoutManager.scrollToPositionWithOffset(cityAdapter.getGroupOfPosition(pos), 0));
                        List<ExpandGroupItemEntity<String, CityModel>> expandGroupItemEntities = cityListModel.getExpandGroupItemEntities();
                        List<CityModel> historyCityModels = cityListModel.getHistoryCityModels();
                        List<CityModel> hotCityModels = cityListModel.getHotCityModels();
                        List<String> sideBarDatas = new ArrayList<>();
                        if (historyCityModels != null && !historyCityModels.isEmpty()) {
                            sideBarDatas.add("历史");
                        }
                        if (hotCityModels != null && !hotCityModels.isEmpty()) {
                            sideBarDatas.add("热门");
                        }
                        for (int index = 0; index < expandGroupItemEntities.size(); index++) {
                            sideBarDatas.add(expandGroupItemEntities.get(index).getParent());
                        }
                        sideBar.setsideBarDatas(sideBarDatas.toArray(new String[sideBarDatas.size()]));
                        cityAdapter.setOnItemClickListener((cityModel, pos) -> {
                            int countryTag = cityModel.getCountryTag();
                            List<CityModel> cityModels = new ArrayList<>();
                            cityModels.add(cityModel);
                            if (1 == countryTag) {
                                SPAppUtil.setFlightCityHistory(mContext, cityModels);
                            } else {
                                SPAppUtil.setInternationFlightCityHistory(mContext, cityModels);
                            }
                            EventBusUtil.sendEvent(new Event(EventCode.INTERNATION_SELECT_CITY, cityModel));
                        });
                        recycleCityList.setAdapter(cityAdapter);
                        loadService.showCallback(SuccessCallback.class);
                    }

                    @Override
                    public void onError(Throwable e) {
                        UsefulToast.showToast(mContext, "数据处理错误");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_city_list;
    }
}
