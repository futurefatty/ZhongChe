package com.neusoft.zcapplication.flight.internation.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.crcc.commonlib.utils.StringUtils;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.flight.internation.model.InternationJourneyFlowModel;
import com.neusoft.zcapplication.tools.DateUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * author:Six
 * Date:2019/5/30
 */
public class JourenyFlowDetailAdapter extends BaseMultiItemQuickAdapter<InternationJourneyFlowModel, BaseViewHolder> {
    //非中转
    public static final int NOT_TRANSFER = 1;
    //中转布局
    public static final int TRANSFER = 2;


    public JourenyFlowDetailAdapter() {
        super(new ArrayList<>());
        addItemType(NOT_TRANSFER, R.layout.item_journey_detail_flow_1);
        addItemType(TRANSFER, R.layout.item_journey_detail_flow_2);
    }

    @Override
    protected void convert(BaseViewHolder helper, InternationJourneyFlowModel item) {
        int defItemViewType = getDefItemViewType(helper.getLayoutPosition());
        switch (defItemViewType) {
            case NOT_TRANSFER:
                TextView tvDepartTime = helper.getView(R.id.tv_depart_time);
                TextView tvDepartAirport = helper.getView(R.id.tv_depart_airport);
                TextView tvDepDate = helper.getView(R.id.tv_dep_date);
                TextView tvArriveTime = helper.getView(R.id.tv_arrive_time);
                TextView tvArriveAirport = helper.getView(R.id.tv_arrive_airport);
                TextView tvAirline = helper.getView(R.id.tv_airline);
                ImageView ivAirLineLogo = helper.getView(R.id.iv_airline_logo);
                TextView tvMenelipsis = helper.getView(R.id.tv_menelipsis);
                View llMenelipsis = helper.getView(R.id.ll_menelipsis);
                View dashViewTopPlaceHolder = helper.getView(R.id.dash_view_top_placeholder);
                String depTime = item.getDepTime();
                String arrTime = item.getArrTime();
                String[] depTimeArr = depTime.split(" ");
                String[] arrTimeArr = arrTime.split(" ");
                String depTimeFormat = DateUtils.formatToDate(DateUtils.parseDate(depTimeArr[1], "HH:mm:ss"), "HH:mm");
                String arrTimeFormat = DateUtils.formatToDate(DateUtils.parseDate(arrTimeArr[1], "HH:mm:ss"), "HH:mm");
                tvDepartTime.setText(depTimeFormat);
                tvArriveTime.setText(arrTimeFormat);

                if (0 == helper.getLayoutPosition()) {
                    tvDepDate.setText("");
                    dashViewTopPlaceHolder.setVisibility(View.GONE);
                } else {
                    dashViewTopPlaceHolder.setVisibility(View.VISIBLE);
                    String depTimeDate = DateUtils.formatToDate(DateUtils.parseDate(depTimeArr[0], "yyyy-MM-dd"), "MM-dd");
                    tvDepDate.setText(depTimeDate);
                }
                tvDepartAirport.setText(String.format("%s    %s",
                        StringUtils.getString(item.getDepAirportName()), StringUtils.getString(item.getDepTerm())));

                tvArriveAirport.setText(String.format("%s    %s",
                        StringUtils.getString(item.getArrAirportName()), StringUtils.getString(item.getArrTerm())));
                String marketingCompanyName = item.getMarketingCompanyName();
                String marketingFlightNo = item.getMarketingFlightNo();
                String marketingCompanyLogo = item.getMarketingCompanyLogo();
                String aircraftName = item.getAircraftName();
                String aircraftType = item.getAircraftType();
                StringBuilder sb = new StringBuilder();
                sb.append(StringUtils.getString(marketingCompanyName));
                sb.append(StringUtils.getString(marketingFlightNo));
                sb.append("    ");
                sb.append("|");
                sb.append("    ");
                sb.append(StringUtils.getString(aircraftName));
                if (!TextUtils.isEmpty(aircraftType)) {
                    sb.append("(");
                    sb.append(aircraftType);
                    sb.append(")");
                }
                String airLine = sb.toString();
                if (TextUtils.isEmpty(airLine)) {
                    tvAirline.setVisibility(View.GONE);
                } else {
                    tvAirline.setVisibility(View.VISIBLE);
                    tvAirline.setText(airLine);
                }
                if (!TextUtils.isEmpty(marketingCompanyLogo)) {
                    ivAirLineLogo.setVisibility(View.VISIBLE);
                    Picasso.with(mContext)
                            .load(marketingCompanyLogo)
                            .into(ivAirLineLogo);
                } else {
                    ivAirLineLogo.setVisibility(View.GONE);
                }
                String stopCityName = item.getStopCityName();
                if (TextUtils.isEmpty(stopCityName)) {
                    llMenelipsis.setVisibility(View.GONE);
                } else {
                    llMenelipsis.setVisibility(View.VISIBLE);
                    tvMenelipsis.setText("经停" + "    " + stopCityName);
                }
                break;
            case TRANSFER:
                TextView tvTransfer = helper.getView(R.id.tv_menelipsis);
                tvTransfer.setText(item.getTransferName());
                break;
        }
    }
}
