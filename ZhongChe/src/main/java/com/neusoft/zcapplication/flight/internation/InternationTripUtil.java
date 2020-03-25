package com.neusoft.zcapplication.flight.internation;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.neusoft.zcapplication.Bean.OuterTripItem;
import com.neusoft.zcapplication.approval.ApprovalActivity;
import com.neusoft.zcapplication.flight.internation.model.InternationCurrentJourneyModel;
import com.neusoft.zcapplication.flight.internation.model.InternationJourneyModel;
import com.neusoft.zcapplication.flight.internation.model.InternationJourneySchemeModel;

import java.util.ArrayList;
import java.util.List;

/**
 * author:Six
 * Date:2019/6/1
 */
public class InternationTripUtil {
    /**
     * 已选择的行程方案
     */
    private static final ArrayList<OuterTripItem> outerTripItems = new ArrayList<>();

    public static ArrayList<OuterTripItem> getOuterTripItems() {
        return outerTripItems;
    }

    public static boolean canAdd(int size) {
        return outerTripItems.size() != size;
    }

    public static void reset() {
        outerTripItems.clear();
    }

    /**
     * 用于国际机票航段详情,选择该参考方案时，将当前航段信息转换为国际机票申请方案模型
     * 并添加到行程方案{@link InternationTripUtil#outerTripItems}
     *
     * @param internationCurrentJourneyModel 当前航段信息
     */
    public static void addTrip(InternationCurrentJourneyModel internationCurrentJourneyModel) {
        OuterTripItem outerTripItem = obtainOuterTripItem(internationCurrentJourneyModel);
        outerTripItems.add(outerTripItem);
    }


    /**
     * 用于国际机票列表查询不到数据时,将当前查询条件转换为国际机票申请方案模型
     * 并添加到行程方案{@link InternationTripUtil#outerTripItems}
     *
     * @param internationJourneyModel 当前行程信息
     */
    public static void addTrip(InternationJourneyModel internationJourneyModel) {
        OuterTripItem outerTripItem = obtainOuterTripItem(internationJourneyModel);
        outerTripItems.add(outerTripItem);
    }

    /**
     * 跳转到国际机票申请单页面
     *
     * @param activity
     */
    public static void startApprovalActivity(Activity activity) {
        Intent intent = new Intent(activity, ApprovalActivity.class);
        //0 当前选择的是国内机票，1国际机票
        intent.putExtra("navIndex", 1);
        intent.putExtra(ApprovalActivity.INTERNATION_FLIGHT_JURNEY_SCHEME, outerTripItems);
        activity.startActivity(intent);
    }


    /**
     * 此方法将航段数据转换为国际机票申请方案模型
     * <p>
     * 将航段信息转换为行程方案
     *
     * @param currentJourneyModel 当前航段信息
     * @return 国际机票申请方案模型
     */
    public static OuterTripItem obtainOuterTripItem(InternationCurrentJourneyModel currentJourneyModel) {
        OuterTripItem outerTripItem = new OuterTripItem();
        outerTripItem.setIsType(8);
        outerTripItem.setFromCityName(currentJourneyModel.getDepCityName());
        outerTripItem.setToCityName(currentJourneyModel.getArrCityName());
        String depTime = "";
        if (!TextUtils.isEmpty(currentJourneyModel.getDepTime())) {
            depTime = currentJourneyModel.getDepTime().split("T")[0];
        } else {
            depTime = "";
        }
        outerTripItem.setStartTime(depTime);
        ArrayList<InternationJourneySchemeModel> journeySchemeModels = new ArrayList<>();
        List<InternationCurrentJourneyModel.FlightSegmentsBean> flightSegments = currentJourneyModel.getFlightSegments();
        for (InternationCurrentJourneyModel.FlightSegmentsBean flightSegmentsBean : flightSegments) {
            InternationJourneySchemeModel journeySchemeModel = new InternationJourneySchemeModel();
            journeySchemeModel.setDepCity(flightSegmentsBean.getDepCityName());
            journeySchemeModel.setDepCityAirport(flightSegmentsBean.getDepAirportName());
            journeySchemeModel.setArrCity(flightSegmentsBean.getArrCityName());
            journeySchemeModel.setArrCityAirport(flightSegmentsBean.getArrAirportName());
            journeySchemeModel.setDepTime(flightSegmentsBean.getDepTime());
            journeySchemeModel.setArrTime(flightSegmentsBean.getArrTime());
            journeySchemeModel.setFlightNumber(flightSegmentsBean.getMarketingFlightNo());
            journeySchemeModels.add(journeySchemeModel);
        }
        outerTripItem.setReferenceScheme(journeySchemeModels);
        return outerTripItem;
    }

    /**
     * 当列表查询不到行程数据时使用此方法将查询数据转换为国际机票申请方案模型
     * <p>
     * 将当前所查询的行程换为行程方案
     *
     * @param internationJourneyModel 当前行程
     * @return 国际机票申请方案模型
     */
    public static OuterTripItem obtainOuterTripItem(InternationJourneyModel internationJourneyModel) {
        OuterTripItem outerTripItem = new OuterTripItem();
        outerTripItem.setIsType(8);
        outerTripItem.setFromCityName(internationJourneyModel.getFromCity());
        outerTripItem.setToCityName(internationJourneyModel.getToCity());
        outerTripItem.setStartTime(internationJourneyModel.getJourneyDateStr());
        outerTripItem.setReferenceScheme(new ArrayList<>());
        return outerTripItem;
    }
}
