package com.neusoft.zcapplication.flight;

import com.neusoft.zcapplication.city.CityModel;
import com.neusoft.zcapplication.flight.internation.model.InterFlightJourneyDetailModel;
import com.neusoft.zcapplication.flight.internation.model.InternationSeachModel;
import com.neusoft.zcapplication.http.now.JsonResult;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * author:Six
 * Date:2019/5/28
 */
public interface FlightApi {

    @POST("flightController/getFlightCityList")
    Call<JsonResult<List<CityModel>>> getInternationFlightCity(@Body Map<String, Object> params);

    @POST("flightController/InterFlightAirlineSearch")
    Call<JsonResult<InternationSeachModel>> getInterFlightAirlineSearch(@Body Map<String, Object> params);

    @POST("flightController/BookingTips")
    Call<JsonResult<InterFlightJourneyDetailModel>> getInterFlightJourneyDetail(@Body Map<String, Object> params);

}
