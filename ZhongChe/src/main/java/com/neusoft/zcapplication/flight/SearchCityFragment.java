package com.neusoft.zcapplication.flight;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.crcc.commonlib.base.BaseFragment;
import com.crcc.commonlib.event.Event;
import com.crcc.commonlib.event.EventBusUtil;
import com.crcc.commonlib.event.EventCode;
import com.crcc.commonlib.inter.Action;
import com.crcc.commonlib.inter.OnItemClickListener;
import com.crcc.commonlib.utils.InputTool;
import com.crcc.commonlib.utils.StringUtils;
import com.crcc.commonlib.view.ClearEditText;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.city.CityModel;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.tools.LoadingUtil;
import com.neusoft.zcapplication.tools.LogUtil;
import com.neusoft.zcapplication.tools.SPAppUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * author:Six
 * Date:2019/5/28
 */
public class SearchCityFragment extends BaseFragment {
    public static final String TAG = SearchCityFragment.class.getSimpleName();
    @BindView(R.id.et_base_search_input)
    ClearEditText etBaseSearchInput;
    @BindView(R.id.tv_search_right)
    TextView tvSearchRight;
    @BindView(R.id.recycle_seach_result)
    RecyclerView recycleSeachResult;
    @BindView(R.id.layout_empty_no_search)
    View layoutEmptyNoSearch;
    @BindView(R.id.tv_city_name)
    TextView tvCityName;
    private BaseQuickAdapter<CityModel, BaseViewHolder> mAdapter;

    private boolean isViewCreated;

    public static SearchCityFragment newInstance() {
        return new SearchCityFragment();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (isViewCreated) {
            if (hidden) {
                etBaseSearchInput.setText("");
                layoutEmptyNoSearch.setVisibility(View.GONE);
                mAdapter.getData().clear();
                mAdapter.notifyDataSetChanged();
                InputTool.HideKeyboard(etBaseSearchInput);
            } else {
                etBaseSearchInput.requestFocus();
                InputTool.ShowKeyboard(etBaseSearchInput);
            }
        }
    }

    private Action action;

    public void setOnCancelListener(Action action) {
        this.action = action;
    }


    @Override
    public void onStop() {
        super.onStop();
        InputTool.HideKeyboard(etBaseSearchInput);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_city_result;
    }

    Observer observer = new Observer<String>() {
        @Override
        public void onSubscribe(Disposable d) {
            addSubscription(d);
        }

        @Override
        public void onNext(String s) {
            s = s.replace(" ", "");
            if (TextUtils.isEmpty(s)) {
                layoutEmptyNoSearch.setVisibility(View.GONE);
                mAdapter.getData().clear();
                mAdapter.notifyDataSetChanged();
            } else {
                doSearch(s);
            }
        }

        @Override
        public void onError(Throwable e) {
            LogUtil.d(e.getMessage());
        }

        @Override
        public void onComplete() {

        }
    };

    private void doSearch(String s) {
        Map<String, Object> params = new HashMap<>();
        params.put("SearchKey", s);
        params.put("Country", 0);
        RetrofitFactory.getInstance().createApi(FlightApi.class)
                .getInternationFlightCity(params)
                .enqueue(new CallBack<List<CityModel>>() {
                    @Override
                    public void success(List<CityModel> response) {
                        if (response == null || response.isEmpty()) {
                            layoutEmptyNoSearch.setVisibility(View.VISIBLE);
                        } else {
                            layoutEmptyNoSearch.setVisibility(View.GONE);
                        }
                        mAdapter.setNewData(response);
                    }

                    @Override
                    public void fail(String code) {

                    }
                });
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvCityName.setText("抱歉,未搜索到结果");
        tvSearchRight.setOnClickListener(v -> action.action());
        recycleSeachResult.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new BaseQuickAdapter<CityModel, BaseViewHolder>(R.layout.item_linear_city) {
            @Override
            protected void convert(BaseViewHolder helper, CityModel item) {
                int adapterPosition = helper.getAdapterPosition();
                View viewLine = helper.getView(R.id.view_line);
                if (adapterPosition == getItemCount() - 1) {
                    viewLine.setVisibility(View.GONE);
                } else {
                    viewLine.setVisibility(View.VISIBLE);
                }
                TextView tvCityName = helper.getView(R.id.tv_city_name);
                TextView tvCitySanZiMa = helper.getView(R.id.tv_city_san_zi_ma);
                TextView tvCountry = helper.getView(R.id.tv_country);
                tvCityName.setText(StringUtils.getString(item.getCityName()));
                tvCitySanZiMa.setText(StringUtils.getString(item.getThreeCode()));
                tvCountry.setText(StringUtils.getString(item.getCountryName()));
            }
        };
        mAdapter.setOnItemClickListener((adapter, view1, position) -> {
            CityModel cityModel = mAdapter.getData().get(position);
            int countryTag = cityModel.getCountryTag();
            ArrayList<CityModel> cityModels = new ArrayList<>();
            cityModels.add(cityModel);
            if (1 == countryTag) {
                SPAppUtil.setFlightCityHistory(mContext, cityModels);
            } else {
                SPAppUtil.setInternationFlightCityHistory(mContext, cityModels);
            }
            EventBusUtil.sendEvent(new Event(EventCode.INTERNATION_SELECT_CITY, cityModel));
        });
        recycleSeachResult.setAdapter(mAdapter);
        Observable.create((ObservableOnSubscribe<String>) emitter -> etBaseSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                emitter.onNext(s.toString());
            }
        })).debounce(250, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        isViewCreated = true;
        etBaseSearchInput.requestFocus();
        InputTool.ShowKeyboard(etBaseSearchInput);
    }


}
