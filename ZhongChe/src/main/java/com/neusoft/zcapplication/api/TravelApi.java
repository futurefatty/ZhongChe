package com.neusoft.zcapplication.api;

import com.neusoft.zcapplication.entity.GetTravelProductDetail;
import com.neusoft.zcapplication.entity.GetTravelProducts;
import com.neusoft.zcapplication.http.now.JsonResult;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Author: TenzLiu
 * Time: 2018/6/12 11:09
 * Desc: 旅游
 */

public interface TravelApi {

    /**
     * 获取旅游产品
     * @param params 参数
     * @return  返回
     */
    @POST("travel/getTravelProducts")
    Call<JsonResult<List<GetTravelProducts>>> getTravelProducts(@Body Map<String, Object> params);

    /**
     * 获取旅游产品详情
     * @param params 参数
     * @return  返回
     */
    @POST("travel/getTravelProductDetail")
    Call<JsonResult<GetTravelProductDetail>> getTravelProductDetail(@Body Map<String, Object> params);
}
