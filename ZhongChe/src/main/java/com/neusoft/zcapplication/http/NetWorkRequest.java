package com.neusoft.zcapplication.http;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/9/2.
 * 接口API
 */

public interface NetWorkRequest {
    /**
     * 获取机场列表
     *
     * @return
     */
    @POST("commonInfo/getCity")
    Call<Map<String, Object>> getCity(@Body Map<String, Object> params);

    /**
     * 获取酒店城市列表
     *
     * @return
     */
    @POST("commonInfo/getHotelCity")
    Call<Map<String, Object>> getHotelCity(@Body Map<String, Object> params);


    /**
     * 用户登录
     *
     * @param params
     * @return
     */
    @POST("sys/login")
    Call<Map<String, Object>> login(@Body Map<String, Object> params);

    /**
     * 获取更新
     *
     * @return
     */
    @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
    @GET("version/getNewVersion?type=1")
    Call<Map<String, Object>> getVersion();

    /**
     * 修改密码
     *
     * @param params
     * @return
     */
    @POST("changePsw/setPassword")
    Call<Map<String, Object>> changePassword(@Body Map<String, Object> params);

    /**
     * 查询预订申请单列表
     *
     * @param params
     * @return
     */
    @POST("order/queryOrder")
    Call<Map<String, Object>> getOrderList(@Body Map<String, Object> params);

    /**
     * 查询已授权人员列表
     *
     * @param params
     * @return
     */
    @POST("order/selectEmployee")
    Call<Map<String, Object>> getPersonList(@Body Map<String, Object> params);

    /**
     * 查询IPD项目信息
     *
     * @param params
     * @return
     */
    @POST("order/pmsTaskList")
    Call<Map<String, Object>> getpmsList(@Body Map<String, Object> params);

    /**
     * 查询项目负责人列表
     *
     * @param params
     * @return
     */
    @POST("order/selectProjectLeader")
    Call<Map<String, Object>> selectProjectLeader(@Body Map<String, Object> params);

    /**
     * 查询审批人列表
     *
     * @param params
     * @return
     */
    @POST("car/getAuditorByUnitCode")
    Call<Map<String, Object>> getAuditorByUnitCode(@Body Map<String, Object> params);

    /**
     * 查询已授权人员列表
     *
     * @param params
     * @return
     */
    @POST("AuthorizationInfo")
    Call<Map<String, Object>> getAuthorizationInfo(@Body Map<String, Object> params);

    /**
     * 查询出行方式列表
     *
     * @param params
     * @return
     */
    @POST("commonInfo/getTravelType")
    Call<Map<String, Object>> getTripType(@Body Map<String, Object> params);

    /**
     * 提交预订申请单
     *
     * @param params
     * @return
     */
    @POST("order/saveOrderApply")
    Call<Map<String, Object>> saveOrderApply(@Body Map<String, Object> params);

    /**
     * 保存单独预定酒店的预定申请单
     *
     * @param params
     * @return
     */
    @POST("order/saveOrderApplyHotelOnly")
    Call<Map<String, Object>> saveOrderApplyHotelOnly(@Body Map<String, Object> params);

    /**
     * 获取国内机票订单列表
     *
     * @param params
     * @return
     */
    @POST("my/getDomesticOrder")
    Call<Map<String, Object>> getInternalOrderList(@Body Map<String, Object> params);

    /**
     * 获取国际机票订单列表
     *
     * @param params
     * @return
     */
    @POST("my/getInternationOrder")
    Call<Map<String, Object>> getExternalOrderList(@Body Map<String, Object> params);

    /**
     * 获取酒店订单列表
     *
     * @param params
     * @return
     */
    @POST("my/getHotelOrder")
    Call<Map<String, Object>> getHotelOrderList(@Body Map<String, Object> params);

    /**
     * 退票
     *
     * @param params
     * @return
     */
    @POST("domestic/returnDomesticFlightOrder")
    Call<Map<String, Object>> returnOrder(@Body Map<String, Object> params);

    /**
     * 获取跳转国内因私机票 H5 HTML
     *
     * @param params
     * @return
     */
    @POST("domesticTicketPrivate/ssoH5LoginCrop")
    Call<Map<String, Object>> personalH5Ticket(@Body Map<String, Object> params);

    /**
     * 获取我的历史行程信息
     *
     * @param params
     * @return
     */
    @POST("my/getOldScheduleData")
    Call<Map<String, Object>> getHistoryScheduleData(@Body Map<String, Object> params);

    /**
     * 获取我的当前行程信息
     *
     * @param params
     * @return
     */
    @POST("my/getScheduleData")
    Call<Map<String, Object>> getScheduleData(@Body Map<String, Object> params);

    /**
     * 编辑个人信息保存
     *
     * @param params
     * @return
     */
    @POST("modify/updateEmployeeInfo")
    Call<Map<String, Object>> editSave(@Body Map<String, Object> params);

    /**
     * 获取国际机票详情方案
     *
     * @param params
     * @return
     */
    @POST("my/getInterMethod")
    Call<Map<String, Object>> getInterMethod(@Body Map<String, Object> params);

    /**
     * 获取我的待办列表
     *
     * @param params
     * @return
     */
    @POST("my/getBacklogData")
    Call<Map<String, Object>> getBacklogList(@Body Map<String, Object> params);

    /**
     * 取消授权
     *
     * @param params
     * @return
     */
    @POST("my/cancelAuthorizationInfo")
    Call<Map<String, Object>> cancelAuthorization(@Body Map<String, Object> params);

    /**
     * 搜索员工
     *
     * @param params
     * @return
     */
    @POST("my/getEmployeeInfo")
    Call<Map<String, Object>> searchEmployeeInfo(@Body Map<String, Object> params);

    /**
     * 授权
     *
     * @param params
     * @return
     */
    @POST("my/setAuthorizationInfo")
    Call<Map<String, Object>> setAuthorization(@Body Map<String, Object> params);

    /**
     * 意见反馈
     *
     * @param params
     * @return
     */
    @POST("my/setSuggestion")
    Call<Map<String, Object>> setSuggestion(@Body Map<String, Object> params);

    /**
     * 新增证件信息
     *
     * @param params
     * @return
     */
    @POST("my/saveDocument")
    Call<Map<String, Object>> saveDocument(@Body Map<String, Object> params);

    /**
     * 修改证件信息
     *
     * @param params
     * @return
     */
    @POST("my/updateDocument")
    Call<Map<String, Object>> updateDocument(@Body Map<String, Object> params);

    /**
     * 发表评价
     *
     * @param params
     * @return
     */
    @POST("my/supplierComment")
    Call<Map<String, Object>> sendComment(@Body Map<String, Object> params);

    /**
     * 获取问题分类
     *
     * @param params
     * @return
     */
    @POST("commonInfo/getSuggestionInfo")
    Call<Map<String, Object>> getProblemType(@Body Map<String, Object> params);

    /**
     * 预订方案
     *
     * @param params
     * @return
     */
    @POST("my/ChooseTicketSelect")
    Call<Map<String, Object>> book(@Body Map<String, Object> params);

    /**
     * 同程
     *
     * @param params
     * @return
     */
    @POST("domesticTicketPrivate/loginH5")
    Call<Map<String, Object>> tcTrain(@Body Map<String, Object> params);

    /**
     * 删除预订申请单
     *
     * @param params
     * @return
     */
    @POST("order/cancelOrder")
    Call<Map<String, Object>> deleteOrder(@Body Map<String, Object> params);

    /**
     * 新增税票信息
     *
     * @param params
     * @return
     */
    @POST("my/insertTax")
    Call<Map<String, Object>> addTaxInfo(@Body Map<String, Object> params);

    /**
     * 获取税票信息列表
     *
     * @param params
     * @return
     */
    @POST("my/queryTax")
    Call<Map<String, Object>> getTaxInfoList(@Body Map<String, Object> params);

    /**
     * 查询签证详情
     *
     * @param params
     * @return
     */
    @POST("commonInfo/queryVisaByAbbr")
    Call<Map<String, Object>> queryVisaByAbbr(@Body Map<String, Object> params);

    /**
     * 审批我的待办
     *
     * @param params
     * @return
     */
    @POST("my/approveOrder")
    Call<Map<String, Object>> approveOrder(@Body Map<String, Object> params);

    /**
     * 获取用户的证件类型列表
     *
     * @param params
     * @return
     */
    @POST("my/queryDocumentByEmp")
    Call<Map<String, Object>> getCredentialsData(@Body Map<String, Object> params);

    /**
     * 删除证件
     *
     * @param params
     * @return
     */
    @POST("my/deleteDocument")
    Call<Map<String, Object>> delCredentialsData(@Body Map<String, Object> params);

    /**
     * 获取证件类型
     *
     * @param params
     * @return
     */
    @POST("commonInfo/getDocumentType")
    Call<Map<String, Object>> getDocumentType(@Body Map<String, Object> params);

    /**
     * 取消酒店订单
     *
     * @param params
     * @return
     */
    @POST("hotelOrder/cancelHotelOrder")
    Call<Map<String, Object>> cancelHotelOrder(@Body Map<String, Object> params);

    /**
     * 获取用户是否有待办事项
     *
     * @param params
     * @return
     */
    @POST("my/getBacklogCount")
    Call<Map<String, Object>> getJobCount(@Body Map<String, Object> params);

    /**
     * 获取用户是否有 待选国际方案
     *
     * @param params
     * @return
     */
    @POST("my/getInterCount")
    Call<Map<String, Object>> getInterCount(@Body Map<String, Object> params);

    /**
     * 获取待确认入住信息的酒店订单列表
     *
     * @param params
     * @return
     */
    @POST("hotelOrder/getHotelOrderList")
    Call<Map<String, Object>> getQueryHotelOrderList(@Body Map<String, Object> params);

    /**
     * 获取某个待确认酒店的详细信息
     *
     * @param params
     * @return
     */
    @POST("hotelOrder/getHotelRoomInfo")
    Call<Map<String, Object>> getQueryHotelOrderDetail(@Body Map<String, Object> params);

    /**
     * 确认酒店入住信息
     *
     * @param params
     * @return
     */
    @POST("hotelOrder/updateHotelInDay")
    Call<Map<String, Object>> updateHotelInDay(@Body Map<String, Object> params);

    /**
     * 常见问题列表
     *
     * @param params
     * @return
     */
    @POST("commonInfo/queryQuestions")
    Call<Map<String, Object>> queryQuestions(@Body Map<String, Object> params);

    /**
     * 酒店订单详情
     *
     * @param params
     * @return
     */
    @POST("hotel/getHotelDetail")
    Call<Map<String, Object>> getHotelDetail(@Body Map<String, Object> params);

    /**
     * 查询常用联系人
     *
     * @param params
     * @return
     */
    @POST("my/queryContacts")
    Call<Map<String, Object>> queryContacts(@Body Map<String, Object> params);

    /**
     * 添加常用联系人信息
     *
     * @param params
     * @return
     */
    @POST("my/saveOrUpdateContacts")
    Call<Map<String, Object>> addContacts(@Body Map<String, Object> params);

    /**
     * 查询热门城市
     *
     * @param params
     * @return
     */
    @POST("commonInfo/queryHotCity")
    Call<Map<String, Object>> queryHotCity(@Body Map<String, Object> params);

    /**
     * @param params
     * @return
     */
    @POST("order/selectCompanyNameByEmployeeCode")
    Call<Map<String, Object>> selectCompanyNameByEmployeeCode(@Body Map<String, Object> params);
}
