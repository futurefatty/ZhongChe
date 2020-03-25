package com.neusoft.zcapplication.flight.internation.adapter;

import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.crcc.commonlib.utils.StringUtils;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.flight.internation.model.InternationCurrentJourneyModel;
import com.neusoft.zcapplication.flight.internation.model.InternationLowerPriceModel;
import com.neusoft.zcapplication.flight.internation.model.InternationSeachModel;
import com.neusoft.zcapplication.tools.DateUtils;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

/**
 * author:Six
 * Date:2019/5/28
 */
public class InternationFlightListAdapter extends BaseQuickAdapter<InternationSeachModel.Schedule, BaseViewHolder> {
    public InternationFlightListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, InternationSeachModel.Schedule schedule) {
        TextView tvDepartTime = helper.getView(R.id.tv_depart_time);
        TextView tvDepartAirline = helper.getView(R.id.tv_depart_airline);
        TextView tvPointToArrowMiddle = helper.getView(R.id.tv_point_to_arrow_middle);
        TextView tvPointToArrowBelow = helper.getView(R.id.tv_point_to_arrow_below);
        TextView tvArriveTime = helper.getView(R.id.tv_arrive_time);
        TextView tvArriveAirline = helper.getView(R.id.tv_arrive_airline);
        TextView tvShare = helper.getView(R.id.tv_share);
        TextView tvPrice = helper.getView(R.id.tv_price);
        TextView tvLook = helper.getView(R.id.tv_look);
        ImageView ivAirlineLogo = helper.getView(R.id.iv_airline_logo);
        TextView tvAirline = helper.getView(R.id.tv_airline);
        TextView tvFlightTime = helper.getView(R.id.tv_flight_time);
        TextView tvPriceSymbol = helper.getView(R.id.tv_price_symbol);
        View tvVerticalLine = helper.getView(R.id.tv_vertical_line);
        TextView tvTommorow = helper.getView(R.id.tv_tomorrow);
        helper.addOnClickListener(R.id.tv_look);
        InternationLowerPriceModel lowerPriceInfo = schedule.getLowerPriceInfo();
        int adultTicketPrice = lowerPriceInfo.getAdultTicketPrice();
        int adultTaxPrice = lowerPriceInfo.getAdultTaxPrice();
        if (adultTicketPrice + adultTaxPrice == 0) {
            tvPrice.setText("");
            tvPriceSymbol.setText("");
        } else {
            tvPrice.setText(String.format(Locale.CHINA, "%d", adultTicketPrice + adultTaxPrice));
            tvPriceSymbol.setText("￥");
        }
        InternationCurrentJourneyModel currentJourney = schedule.getCurrentJourney();
        List<InternationCurrentJourneyModel.FlightSegmentsBean> flightSegments = currentJourney.getFlightSegments();
        if (flightSegments == null || flightSegments.isEmpty()) {
            ivAirlineLogo.setVisibility(View.GONE);
            tvAirline.setText(View.GONE);
            tvVerticalLine.setVisibility(View.GONE);
        } else {
            InternationCurrentJourneyModel.FlightSegmentsBean first = flightSegments.get(0);
            String marketingCompanyName = first.getMarketingCompanyName();
            boolean isMarketingCompanyNameBoth = true;
            for (int index = 1; index < flightSegments.size(); index++) {
                InternationCurrentJourneyModel.FlightSegmentsBean flightSegmentsBean = flightSegments.get(index);
                if (!marketingCompanyName.equals(flightSegmentsBean.getMarketingCompanyName())) {
                    isMarketingCompanyNameBoth = false;
                    break;
                }
            }
            /**
             * 是否存在两个一样的航空公司
             * 如存在 不用在航空公司名称后面拼接...等
             */
            if (isMarketingCompanyNameBoth) {
                if (marketingCompanyName.length() > 7) {
                    marketingCompanyName = marketingCompanyName.substring(0, 7) + "...";
                }
            } else {
                //如果存在多个航空公司，超过限制...等
                if (marketingCompanyName.length() > 7) {
                    marketingCompanyName = marketingCompanyName.substring(0, 7) + "...等";
                }
            }

            if (TextUtils.isEmpty(marketingCompanyName)) {
                tvVerticalLine.setVisibility(View.GONE);
                tvAirline.setVisibility(View.GONE);
            } else {
                tvAirline.setText(marketingCompanyName);
                tvAirline.setVisibility(View.VISIBLE);
                tvVerticalLine.setVisibility(View.VISIBLE);
            }
            String marketingCompanyLogo = first.getMarketingCompanyLogo();
            if (!TextUtils.isEmpty(marketingCompanyLogo)) {
                ivAirlineLogo.setVisibility(View.VISIBLE);
                Picasso.with(mContext)
                        .load(first.getMarketingCompanyLogo())
                        .into(ivAirlineLogo);
            } else {
                ivAirlineLogo.setVisibility(View.GONE);
            }
            boolean isShare = false;
            for (int index = 0; index < flightSegments.size(); index++) {
                InternationCurrentJourneyModel.FlightSegmentsBean flightSegmentsBean = flightSegments.get(index);
                if (flightSegmentsBean.isCodeShare()) {
                    isShare = true;
                    break;
                }
            }
            tvShare.setVisibility(isShare ? View.VISIBLE : View.GONE);
            if (isShare) {
                tvLook.setBackgroundResource(R.drawable.shape_rectangle_ccfcfcf_r2);
                tvLook.setEnabled(false);
            } else {
                tvLook.setEnabled(true);
                tvLook.setBackgroundResource(R.drawable.shape_rectangle_cc70019_r2);
            }
            switch (flightSegments.size()) {
                case 1:
                    InternationCurrentJourneyModel.FlightSegmentsBean flightSegmentsBean = flightSegments.get(0);
                    String stopCityName = flightSegmentsBean.getStopCityName();
                    if (TextUtils.isEmpty(stopCityName)) {
                        tvPointToArrowMiddle.setVisibility(View.GONE);
                        tvPointToArrowBelow.setVisibility(View.GONE);
                    } else {
                        tvPointToArrowMiddle.setVisibility(View.VISIBLE);
                        tvPointToArrowBelow.setVisibility(View.VISIBLE);
                        String[] stopCityNameArr = stopCityName.split(",");
                        if (stopCityNameArr.length > 0) {
                            if (stopCityNameArr.length > 1) {
                                tvPointToArrowBelow.setText(String.format(Locale.CHINA, "%d次", stopCityNameArr.length));
                                tvPointToArrowMiddle.setText("经停");
                            } else {
                                tvPointToArrowMiddle.setText("经停");
                                tvPointToArrowBelow.setText(StringUtils.getString(stopCityNameArr[0]));
                            }
                        } else {
                            tvPointToArrowMiddle.setText("经停");
                            tvPointToArrowBelow.setText("");
                        }
                    }
                    break;
                default:
                    tvPointToArrowMiddle.setVisibility(View.VISIBLE);
                    tvPointToArrowBelow.setVisibility(View.VISIBLE);
                    tvPointToArrowMiddle.setText("中转");
                    boolean hasStopCity = false;
                    for (InternationCurrentJourneyModel.FlightSegmentsBean segmentsBean : flightSegments) {
                        stopCityName = segmentsBean.getStopCityName();
                        if (!TextUtils.isEmpty(stopCityName)) {
                            hasStopCity = true;
                            break;
                        }
                    }
                    if (hasStopCity) {
                        tvPointToArrowBelow.setText("经停");
                    } else {
                        if (flightSegments.size() == 2) {
                            tvPointToArrowBelow.setText(StringUtils.getString(first.getArrCityName()));
                        } else {
                            tvPointToArrowBelow.setText(String.format(Locale.CHINA, "%d次", flightSegments.size() - 1));
                        }
                    }
                    break;
            }
        }
        //起飞机场名称
        String depAirportName = currentJourney.getDepAirportName();
        //起飞时间
        String depTime = currentJourney.getDepTime();
        //到达机场名称
        String arrAirportName = currentJourney.getArrAirportName();
        //到达时间
        String arrTime = currentJourney.getArrTime();
        //耗时
        String duration = currentJourney.getDuration();
        //处理起飞机场 字符串长度
        limitTextViewLen(tvDepartAirline, depAirportName);
        //处理到达机场 字符串长度
        limitTextViewLen(tvArriveAirline, arrAirportName);
        String[] depArr = depTime.split("T");
        String[] arrArr = arrTime.split("T");
        String depHour = DateUtils.formatToDate(DateUtils.parseDate(depArr[1], "HH:mm:ss"), "HH:mm");
        String arrHour = DateUtils.formatToDate(DateUtils.parseDate(arrArr[1], "HH:mm:ss"), "HH:mm");
        int differDays = DateUtils.differDays(depArr[0], arrArr[0]);
        if (differDays > 0) {
            tvTommorow.setText(String.format(Locale.CHINA, "+%d天", differDays));
        } else {
            tvTommorow.setText("");
        }
        tvDepartTime.setText(depHour);
        tvArriveTime.setText(arrHour);

        String minuteToHour = DateUtils.minuteToHour(duration);
        if (TextUtils.isEmpty(minuteToHour)) {
            tvFlightTime.setVisibility(View.GONE);
        } else {
            tvFlightTime.setText(minuteToHour);
            tvFlightTime.setVisibility(View.VISIBLE);
        }

    }

    private void limitTextViewLen(TextView textView, String str) {
        if (!TextUtils.isEmpty(str)) {
            if (str.length() > 4) {
                str = str.substring(0, 4) + "...";
                float arriveTextWidth = textView.getPaint().measureText(str);
                ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
                layoutParams.width = (int) arriveTextWidth;
                textView.setLayoutParams(layoutParams);
                textView.setText(str);
            } else {
                ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
                layoutParams.width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                textView.setLayoutParams(layoutParams);
                textView.setText(str);
            }
        } else {
            textView.setText("");
        }
    }
}
