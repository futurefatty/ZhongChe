package com.neusoft.zcapplication.api;

import com.neusoft.zcapplication.entity.GetNotice;
import com.neusoft.zcapplication.entity.MonthlyIndexDatas;
import com.neusoft.zcapplication.entity.MonthlyTicketing;
import com.neusoft.zcapplication.http.now.JsonResult;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CompanyDataApi {
    @POST("dataReport/getMonthlyIndexDatas")
    Call<JsonResult<List<MonthlyIndexDatas>>> getMonthlyIndexDatas(@Body Map<String,Object> params);

    @POST("dataReport/getMonthlyTicketingData")
    Call<JsonResult<List<MonthlyTicketing>>> getMonthlyTicketingData(@Body Map<String,Object> params);

}
