package com.neusoft.zcapplication.api;

import com.neusoft.zcapplication.entity.StaffGoModel;
import com.neusoft.zcapplication.http.now.JsonResult;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface StaffGoApi {
    @POST("dataReport/getUnusedDomesticOrder")
    Call<JsonResult<List<StaffGoModel>>> getUnusedDomesticOrder(@Body Map<String,Object> params);
}
