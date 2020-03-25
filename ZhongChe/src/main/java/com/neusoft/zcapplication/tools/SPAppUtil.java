package com.neusoft.zcapplication.tools;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.crcc.commonlib.utils.JSONUtils;
import com.crcc.commonlib.utils.StringUtils;
import com.neusoft.zcapplication.Constant.Constant;
import com.neusoft.zcapplication.city.CityModel;
import com.neusoft.zcapplication.entity.SearchFlightTicketCity;
import com.neusoft.zcapplication.flight.internation.model.InternationJourneyModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Author: TenzLiu
 * Date: 2018-04-12 12:11
 * Description: sharePreference二次项目封装保存信息工具类
 */

public class SPAppUtil {

    /**
     * 获取国际机票行程
     *
     * @param context
     * @return
     */
    public static InternationJourneyModel getInternationFlightJourney(Context context) {
        String json = SpUtil.getString(context, Constant.INTERNATION_FLIGHT_JOURENY, "");
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        return JSONUtils.gsonToBean(json, InternationJourneyModel.class);
    }

    /**
     * 存储国际机票行程
     *
     * @param context
     * @return
     */
    public static void setInternationFlightJourney(Context context, InternationJourneyModel internationJourneyModel) {
        SpUtil.putString(context, Constant.INTERNATION_FLIGHT_JOURENY, JSONUtils.gsonString(internationJourneyModel));
    }

    /**
     * 存储国内机票城市历史记录
     *
     * @param context
     * @param cityModels
     */
    public static void setFlightCityHistory(Context context, List<CityModel> cityModels) {
        if (cityModels == null || cityModels.isEmpty()) {
            return;
        }
        List<CityModel> cityCacheModels = getFlightCityHistory(context);
        cityCacheModels.addAll(cityModels);
        removeDuplicateWithOrder(cityCacheModels);
        if (cityCacheModels.size() > 6) {
            cityCacheModels = cityCacheModels.subList(cityCacheModels.size() - 6, cityCacheModels.size());
        }
        SpUtil.putString(context, Constant.FLIGHT_CITY_HISTORY, JSONUtils.gsonString(cityCacheModels));
    }

    /**
     * 获取国内机票城市历史
     * 当前/历史：保留最多6个城市
     *
     * @param context
     * @return
     */
    @NonNull
    public static List<CityModel> getFlightCityHistory(Context context) {
        String json = SpUtil.getString(context, Constant.FLIGHT_CITY_HISTORY, "");
        if (StringUtils.isEmpty(json)) {
            return new ArrayList<>();
        }
        return JSONUtils.jsonToList(json, CityModel.class);
    }

    /**
     * 存储国际机票城市历史记录
     *
     * @param context
     * @param cityModels
     */
    public static void setInternationFlightCityHistory(Context context, List<CityModel> cityModels) {
        if (cityModels == null || cityModels.isEmpty()) {
            return;
        }
        List<CityModel> cityCahceModels = getInternationFlightCityHistory(context);
        cityCahceModels.addAll(cityModels);
        removeDuplicateWithOrder(cityCahceModels);
        if (cityCahceModels.size() > 6) {
            cityCahceModels = cityCahceModels.subList(cityCahceModels.size() - 6, cityCahceModels.size());
        }
        SpUtil.putString(context, Constant.INTERNATION_FLIGHT_CITY_HISTORY, JSONUtils.gsonString(cityCahceModels));
    }

    /**
     * 获取国际机票城市历史
     * 当前/历史：保留最多6个城市
     *
     * @param context
     * @return
     */
    @NonNull
    public static List<CityModel> getInternationFlightCityHistory(Context context) {
        String json = SpUtil.getString(context, Constant.INTERNATION_FLIGHT_CITY_HISTORY, "");
        if (TextUtils.isEmpty(json)) {
            return new ArrayList<>();
        }
        return JSONUtils.jsonToList(json, CityModel.class);
    }

    public static <T> void removeDuplicateWithOrder(List<T> list) {
        Set<T> set = new HashSet<>();
        List<T> newList = new ArrayList<>();
        for (Iterator<T> iter = list.iterator(); iter.hasNext(); ) {
            T element = iter.next();
            if (set.add(element)) {
                newList.add(element);
            }
        }
        list.clear();
        list.addAll(newList);

    }


    /**
     * 获取机票查询出发城市
     *
     * @param context
     * @return
     */
    public static SearchFlightTicketCity getSearchFlightTicketStartCity(Context context) {
        SearchFlightTicketCity searchFlightTicketCity = new SearchFlightTicketCity();
        String cityCode = SpUtil.getString(context, Constant.SEARCHFLIGHTTICKETCITYCODESTART, "");
        String cityName = SpUtil.getString(context, Constant.SEARCHFLIGHTTICKETCITYNAMESTART, "");
        searchFlightTicketCity.setCityCode(cityCode);
        searchFlightTicketCity.setCityName(cityName);
        return searchFlightTicketCity;
    }

    /**
     * 保存机票查询出发城市
     *
     * @param context
     * @return
     */
    public static void setSearchFlightTicketStartCity(Context context, SearchFlightTicketCity searchFlightTicketCity) {
        SpUtil.putString(context, Constant.SEARCHFLIGHTTICKETCITYCODESTART, searchFlightTicketCity.getCityCode());
        SpUtil.putString(context, Constant.SEARCHFLIGHTTICKETCITYNAMESTART, searchFlightTicketCity.getCityName());
    }

    /**
     * 获取机票查询到达城市
     *
     * @param context
     * @return
     */
    public static SearchFlightTicketCity getSearchFlightTicketEndCity(Context context) {
        SearchFlightTicketCity searchFlightTicketCity = new SearchFlightTicketCity();
        String cityCode = SpUtil.getString(context, Constant.SEARCHFLIGHTTICKETCITYCODEEND, "");
        String cityName = SpUtil.getString(context, Constant.SEARCHFLIGHTTICKETCITYNAMEEND, "");
        searchFlightTicketCity.setCityCode(cityCode);
        searchFlightTicketCity.setCityName(cityName);
        return searchFlightTicketCity;
    }

    /**
     * 保存机票查询到达城市
     *
     * @param context
     * @return
     */
    public static void setSearchFlightTicketEndCity(Context context, SearchFlightTicketCity searchFlightTicketCity) {
        SpUtil.putString(context, Constant.SEARCHFLIGHTTICKETCITYCODEEND, searchFlightTicketCity.getCityCode());
        SpUtil.putString(context, Constant.SEARCHFLIGHTTICKETCITYNAMEEND, searchFlightTicketCity.getCityName());
    }

//    SEARCHFLIGHTTICKETCITYTIME

    /**
     * 获取机票查询时间
     *
     * @param context
     * @return
     */
    public static String getSearchFlightTicketTime(Context context) {
        String searchTime = SpUtil.getString(context, Constant.SEARCHFLIGHTTICKETCITYTIME, "");
        return searchTime;
    }

    /**
     * 保存机票查询时间
     *
     * @param context
     * @return
     */
    public static void setSearchFlightTicketTime(Context context, String seachTime) {
        SpUtil.putString(context, Constant.SEARCHFLIGHTTICKETCITYTIME, seachTime);
    }

}
