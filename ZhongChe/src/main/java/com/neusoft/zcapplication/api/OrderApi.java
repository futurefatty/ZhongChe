package com.neusoft.zcapplication.api;

import com.neusoft.zcapplication.entity.GetAuthorizationInfo;
import com.neusoft.zcapplication.entity.GetBacklogData;
import com.neusoft.zcapplication.entity.GetCredentialsData;
import com.neusoft.zcapplication.entity.GetDocumentType;
import com.neusoft.zcapplication.entity.GetEmployeeInfo;
import com.neusoft.zcapplication.entity.GetPayment;
import com.neusoft.zcapplication.entity.GetPersonalData;
import com.neusoft.zcapplication.entity.GetScheduleData;
import com.neusoft.zcapplication.entity.GetSelectEmployee;
import com.neusoft.zcapplication.entity.GetSuggestionInfo;
import com.neusoft.zcapplication.entity.QueryFeeback;
import com.neusoft.zcapplication.entity.QueryFeebackDetail;
import com.neusoft.zcapplication.http.now.JsonResult;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Author: TenzLiu
 * Time: 2018/6/12 11:06
 * Desc:  订单
 */

public interface OrderApi {

    /**
     * 获取用户是否有待办事项
     * ciphertext (string): 密文 ,
     * employeeCode (string): 员工编号 ,
     * loginType (integer): 用户登录途径，微信：1；app：2；pc：3
     * @param params 参数
     * @return  返回
     */
//    @POST("order/my/getBacklogCount")
    @POST("order/my/getTabDealWithCount")
    Call<JsonResult<Map<String,Integer>>> getBacklogCount(@Body Map<String,Object> params);

    /**
     * 获取用户是否有待选国际方案
     * ciphertext (string): 密文 ,
     * employeeCode (string): 员工编号 ,
     * loginType (integer): 用户登录途径，微信：1；app：2；pc：3
     * @param params 参数
     * @return  返回
     */
    @POST("order/my/getInterCount")
    Call<JsonResult<Integer>> getInterCount(@Body Map<String,Object> params);

    /**
     * 获取我的当前行程信息
     * ciphertext (string): 密文 ,
     * employeeCode (string): 员工编号 ,
     * loginType (integer): 用户登录途径，微信：1；app：2；pc：3 ,
     * timeSlot (integer): 时间段，0：一个月内，1：三个月内，2：半年内，3：一年内，4：三年内，5：全部数据 ,  (5)
     * type (integer): 查询类型，0：全部，1：国内，2：国际，3：酒店    (1)
     * @param params 参数
     * @return  返回
     */
    @POST("order/my/getScheduleData")
    Call<JsonResult<List<GetScheduleData>>> getScheduleData(@Body Map<String,Object> params);

    /**
     * 获取我的待办列表
     * ciphertext (string): 密文 ,
     * employeeCode (string): 员工编号 ,
     * loginType (integer): 用户登录途径，微信：1；app：2；pc：3
     * @param params 参数
     * @return  返回
     */
    @POST("order/my/getBacklogData")
    Call<JsonResult<List<GetBacklogData>>> getBacklogData(@Body Map<String,Object> params);

    /**
     * 查询已授权人员列表
     * ciphertext (string): 密文 ,
     * employeeCode (string): 员工编号 ,
     * loginType (integer): 用户登录途径，微信：1；app：2；pc：3
     * @param params 参数
     * @return  返回
     */
    @POST("order/my/getAuthorizationInfo")
    Call<JsonResult<List<GetAuthorizationInfo>>> getAuthorizationInfo(@Body Map<String,Object> params);


    /**
     * 取消授权
     * beAgreeCode (string): 授权的员工编号 ,
     * ciphertext (string): 密文 ,
     * employeeCode (string): 登录进去的员工编号 ,
     * loginType (integer): 用户登录途径，微信：1；app：2；pc：3
     * @param params 参数
     * @return  返回
     */
    @POST("order/my/cancelAuthorizationInfo")
    Call<JsonResult<Object>> cancelAuthorization(@Body Map<String,Object> params);

    /**
     * 搜索员工获取信息进行授权
     * applicateId (string): 申请人员工编号 ,
     * ciphertext (string): 密文 ,
     * employeeCode (string): 员工编号 ,
     * employeeName (string): 员工姓名 ,
     * loginType (integer): 用户登录途径，微信：1；app：2；pc：3
     * @param params 参数
     * @return  返回
     */
    @POST("order/my/getEmployeeInfo")
    Call<JsonResult<List<GetEmployeeInfo>>> getEmployeeInfo(@Body Map<String,Object> params);

    /**
     * 授权
     * agreeValidTime (string): 授权有效期，格式‘yyyy-MM-dd’ ,
     * beAgreeCode (string): 授权的员工编号 ,
     * ciphertext (string): 密文 ,
     * employeeCode (string): 登录进去的员工编号 ,
     * loginType (integer): 用户登录途径，微信：1；app：2；pc：3
     * @param params 参数
     * @return  返回
     */
    @POST("order/my/setAuthorizationInfo")
    Call<JsonResult<Object>> setAuthorization(@Body Map<String,Object> params);

    /**
     * 获取问题分类
     * ciphertext (string): 密文 ,
     * loginType (integer): 用户登录途径，微信：1；app：2；pc：3
     * @param params 参数
     * @return  返回
     */
    @POST("order/commonInfo/getSuggestionInfo")
    Call<JsonResult<List<GetSuggestionInfo>>> getProblemType(@Body Map<String,Object> params);

    /**
     * 意见反馈
     * ciphertext (string): 密文 ,
     * contractInfo (string): 电话 ,
     * contracts (string): 员工姓名 ,
     * detail (string): 投诉与建议 ,
     * employeeCode (string): 编号 ,
     * loginType (integer): 用户登录途径，微信：1；app：2；pc：3 ,
     * questionVal (string): 获取问题分类,从common中获取 ,
     * suggestionId (integer): id ,
     * suggestionType (integer): 类型
     * @param params 参数
     * @return  返回
     */
    @POST("order/my/setSuggestion")
    Call<JsonResult<Object>> setSuggestion(@Body Map<String,Object> params);

    /**
     * 删除预订申请单
     * ciphertext (string): 密文 ,
     * employeeCode (string): 员工编号 ,
     * id (Array[string]): id ,
     * loginType (integer): 用户登录途径，微信：1；app：2；pc：3 ,
     * orderApplyId (Array[string]): orderApplyId ,
     * type (string): 预定申请单类别，1：国内机票，2：国际机票，3：酒店
     * @param params 参数
     * @return  返回
     */
    @POST("order/order/cancelOrder")
    Call<JsonResult<Object>> deleteOrder(@Body Map<String,Object> params);

    /**
     * 查询预订申请单列表
     * ciphertext (string): 密文 ,
     * employeeCode (string): 员工编号 ,
     * loginType (integer): 用户登录途径，微信：1；app：2；pc：3 ,
     * type (integer): 申请单类型，1：国内机票，2：国际机票，3：酒店
     * @param params 参数
     * @return  返回
     */
    @POST("order/order/queryOrder")
    Call<Map<String,Object>> getOrderList(@Body Map<String,Object> params);

    /**
     * 新增证件信息
     * begindate (string): 签发时间,yyyy-MM-dd ,
     * ciphertext (string): 密文 ,
     * documentId (integer): 证件类型ID，接口/commonInfo/getDocumentType中‘id’字段 ,
     * documentInfo (string): 证件编号 ,
     * employeeCode (string): 员工编号 ,
     * employeeName (string): 员工姓名 ,
     * enddate (string): 到期时间,yyyy-MM-dd ,
     * familyName (string): 英文姓名的姓 ,
     * id (number, optional): 数据id，新增操作时无需入此参，更新操作时才入 ,
     * loginType (integer): 用户登录途径，微信：1；app：2；pc：3 ,
     * secondName (string): 英文姓名的名
     * @param params 参数
     * @return  返回
     */
    @POST("order/my/saveDocument")
    Call<JsonResult<Object>> saveDocument(@Body Map<String,Object> params);

    /**
     * 修改证件信息
     * begindate (string): 签发时间,yyyy-MM-dd ,
     * ciphertext (string): 密文 ,
     * documentId (integer): 证件类型ID，接口/commonInfo/getDocumentType中‘id’字段 ,
     * documentInfo (string): 证件编号 ,
     * employeeCode (string): 员工编号 ,
     * employeeName (string): 员工姓名 ,
     * enddate (string): 到期时间,yyyy-MM-dd ,
     * familyName (string): 英文姓名的姓 ,
     * id (number, optional): 数据id，新增操作时无需入此参，更新操作时才入 ,
     * loginType (integer): 用户登录途径，微信：1；app：2；pc：3 ,
     * secondName (string): 英文姓名的名
     * @param params 参数
     * @return  返回
     */
    @POST("order/my/updateDocument")
    Call<JsonResult<Object>> updateDocument(@Body Map<String,Object> params);

    /**
     * 获取证件类型
     * ciphertext (string): 密文 ,
     * loginType (integer): 用户登录途径，微信：1；app：2；pc：3
     * @param params 参数
     * @return  返回
     */
    @POST("order/commonInfo/getDocumentType")
    Call<JsonResult<List<GetDocumentType>>> getDocumentType(@Body Map<String,Object> params);

    /**
     * 删除证件
     * ciphertext (string): 密文 ,
     * employeeCode (string): 员工编号 ,
     * id (integer): 数据id ,
     * loginType (integer): 用户登录途径，微信：1；app：2；pc：3
     * @param params 参数
     * @return  返回
     */
    @POST("order/my/deleteDocument")
    Call<JsonResult<Object>> delCredentialsData(@Body Map<String,Object> params);

    /**
     * 获取用户的证件类型列表
     * ciphertext (string): 密文 ,
     * employeeCode (string): 员工编号 ,
     * loginType (integer): 用户登录途径，微信：1；app：2；pc：3
     * @param params 参数
     * @return  返回
     */
    @POST("order/my/queryDocumentByEmp")
    Call<JsonResult<List<GetCredentialsData>>> getCredentialsData(@Body Map<String,Object> params);

    /**
     * 删除证件
     * changeFlag (string): 改签标记,正常订单支付：0，改签订单支付：1 ,
     * ciphertext (string): 密文 ,
     * loginType (integer): 用户登录途径，微信：1；app：2；pc：3 ,
     * orderApplyId (string): 预定申请单编号 ,
     * orderCode (string): 订单编号 ,
     * type (integer): 请求类别，1：国内机票，2：国际机票，3：酒店
     * @return  返回
     */
    @POST("pay/payment")
    Call<JsonResult<GetPayment>> payment(@Body Map<String,Object> params);

    /**
     * 查询已授权人员列表
     * ciphertext (string): 密文 ,
     * employeeCode (string): 员工编号 ,
     * loginType (integer): 用户登录途径，微信：1；app：2；pc：3 ,
     * @return  返回
     */
    @POST("order/order/selectEmployee")
    Call<JsonResult<List<GetSelectEmployee>>> selectEmployee(@Body Map<String,Object> params);

    /**
     * 查询预订申请单列表
     * cciphertext (string): 密文 ,
     * employeeCode (string): 员工号 ,
     * loginType (integer): 用户登录途径，微信：1；app：2；pc：3 ,
     * year (string): 年份 例：2018 ,
     * quarterType (string, optional): 年份 例：第一季 传 1 ,
     * yearMonth (string, optional): 年月例：2018-08
     * @param params 参数
     * @return  返回
     */
    @POST("zc/personalData/getPersonalData")
    Call<JsonResult<GetPersonalData>> getPersonalData(@Body Map<String,Object> params);

    /**
     * 投诉与建议_获取当前用户反馈列表
     * ciphertext (string): 密文 ,
     * employeeCode (string): 员工编号 ,
     * loginType (integer): 用户登录途径，微信：1；app：2；pc：3 ,
     * @return  返回
     */
    @POST("order/my/queryFeeback")
    Call<JsonResult<List<QueryFeeback>>> queryFeeback(@Body Map<String,Object> params);

    /**
     * 投诉与建议_获取回复内容
     * ciphertext (string): 密文 ,
     * employeeCode (string): 员工编号 ,
     * loginType (integer): 用户登录途径，微信：1；app：2；pc：3 ,
     * suggestionsId  (integer): 反馈记录ID
     * @return  返回
     */
    @POST("order/my/getParent")
    Call<JsonResult<List<QueryFeebackDetail>>> getParent(@Body Map<String,Object> params);

}
