package com.neusoft.zcapplication.flight.internation.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.crcc.commonlib.utils.StringUtils;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.flight.internation.model.InternationJourneyModel;

/**
 * author:Six
 * Date:2019/5/27
 */

public class InternationJouneyAdapter extends BaseQuickAdapter<InternationJourneyModel, BaseViewHolder> {
    public InternationJouneyAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, InternationJourneyModel item) {
        TextView tvJourneyCount = helper.getView(R.id.tv_journey_count);
        TextView tvFromCity = helper.getView(R.id.tv_from_city);
        TextView tvToCity = helper.getView(R.id.tv_to_city);
        TextView tvJourneyDate = helper.getView(R.id.tv_journey_date);
        TextView tvJourneyWeek = helper.getView(R.id.tv_journey_week);
        int position = helper.getLayoutPosition();
        item.setPosition(position);
        tvJourneyCount.setText(String.format("%d程", position + 1));
        tvFromCity.setText(StringUtils.getString(item.getFromCity()));
        tvToCity.setText(StringUtils.getString(item.getToCity()));
        tvJourneyDate.setText(StringUtils.getString(item.getJourneyDateStr()));
        tvJourneyWeek.setText(StringUtils.getString(item.getWeek()));
        helper.addOnClickListener(R.id.iv_journey_exchange);
        helper.addOnClickListener(R.id.tv_from_city);
        helper.addOnClickListener(R.id.tv_to_city);
        helper.addOnClickListener(R.id.ll_journey_date);
        helper.addOnClickListener(R.id.iv_close);
    }


}
