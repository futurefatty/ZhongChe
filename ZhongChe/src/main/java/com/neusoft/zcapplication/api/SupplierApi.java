package com.neusoft.zcapplication.api;

import com.neusoft.zcapplication.http.now.JsonResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Author: TenzLiu
 * Time: 2018/6/12 11:09
 * Desc:  供应商
 */

public interface SupplierApi {

    /**
     * 同程
     * ciphertext
     * interfaceType:2
     * style:5  1：美亚；2：慧通；3：HRS；4：携程 ,5同程
     * userId
     * @param params 参数
     * @return  返回
     */
    @POST("supplier/domesticTicketPrivate/loginH5")
    Call<JsonResult<String>> tcTrain(@Body Map<String,Object> params);

    /**
     * 获取跳转国内因私机票 H5 HTML
     * @param params 参数
     * @return  返回
     */
    @POST("supplier/domesticTicketPrivate/ssoH5LoginCrop")
    Call<JsonResult<String>> personalH5Ticket(@Body Map<String,Object> params);

}
