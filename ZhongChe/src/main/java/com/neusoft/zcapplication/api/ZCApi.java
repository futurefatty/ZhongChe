package com.neusoft.zcapplication.api;

import com.neusoft.zcapplication.entity.QueryHotCity;
import com.neusoft.zcapplication.http.now.JsonResult;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Author: TenzLiu
 * Time: 2018/6/12 11:09
 * Desc: 中车
 */

public interface ZCApi {

    /**
     * 查询热门城市
     * ciphertext (string): 密文 ,
     * cityId (string): 城市编码 ,
     * loginType (integer): 用户登录途径，微信：1；app：2；pc：3 ,
     * style (string): 城市编码类型：1：酒店；2：机场
     * @param params 参数
     * @return  返回
     */
    @POST("zc/commonInfo/queryHotCity")
    Call<JsonResult<List<QueryHotCity>>> queryHotCity(@Body Map<String,Object> params);

}
