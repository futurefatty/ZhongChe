package com.neusoft.zcapplication.api;

import com.neusoft.zcapplication.entity.GetAllApplyUseCars;
import com.neusoft.zcapplication.entity.GetAllCarRange;
import com.neusoft.zcapplication.entity.GetAllCarType;
import com.neusoft.zcapplication.entity.GetApplyCarsAudit;
import com.neusoft.zcapplication.entity.GetAllSuppliers;
import com.neusoft.zcapplication.entity.GetApplyCarsDeal;
import com.neusoft.zcapplication.http.now.JsonResult;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Author: TenzLiu
 * Time: 2018/6/12 11:09
 * Desc: 公务用车
 */

public interface CarApi {

    /**
     * 申请用车
     * applicatId (string): 申请人工号 ,
     * applicatName (string): 申请人名称 ,
     * mobile (string): 申请人联系方式 ,
     * unitCode (string): 部门编号 ,
     * email (string): 邮箱 ,
     * carRange (integer): 用车范围 , 1
     * carType (string): 车型 , 轿车
     * numberPeople (integer): 用车人数 ,
     * startTime (string): 用车开始时间 ,
     * endTime (string): 用车结束时间 ,
     * applyReason (string): 申请事由 ,
     * carNumber (string): 用车数量 ,
     * userName (string): 用车人姓名 ,
     * userMobile (string): 用车人电话 ,
     * importantCustomer (integer, optional): 是否重要客户（0表示否，1表示是） ,
     * customerLevelJob (string, optional): 客户职务级别 ,
     * unitCode
     * unitName
     * accountingSubjectCode
     * accountingSubjectName
     * auditorId 审批人id
     * auditorName 审批人姓名
     * @param params 参数
     * @return  返回
     */
    @POST("order/car/createApplyCar")
    Call<JsonResult<Object>> createApplyCar(@Body Map<String, Object> params);

    /**
     * 查询该申请人的所有用车申请单列表
     * applicatId (string): 申请人工号 ,
     * @param params 参数
     * @return  返回
     */
    @POST("order/car/getAllApplyUseCars")
    Call<JsonResult<List<GetAllApplyUseCars>>> getAllApplyUseCars(@Body Map<String, Object> params);

    /**
     * 获取所有供应商
     * @param params 参数
     * @return  返回
     */
    @POST("order/car/getAllSuppliers")
    Call<JsonResult<List<GetAllSuppliers>>> getAllSuppliers(@Body Map<String, Object> params);

    /**
     * 获取用车待审核列表
     * auditorId (string):  ,
     * @param params 参数
     * @return  返回
     */
    @POST("order/car/getApplyCars_Audit")
    Call<JsonResult<List<GetApplyCarsAudit>>> getApplyCars_Audit(@Body Map<String, Object> params);

    /**
     * 获取用车待办理列表
     * auditorId (string):  ,
     * @param params 参数
     * @return  返回
     */
    @POST("order/car/getApplyCars_Deal")
    Call<JsonResult<List<GetApplyCarsDeal>>> getApplyCars_Deal(@Body Map<String, Object> params);

    /**
     * 审批用车
     * id (int):52
     * applicatId (int):
     * applicatName (int):
     * unitCode (int):
     * auditorId (int):
     * auditStatus (string):1同意 2拒绝
     * auditorAdvice (string):
     * applyReason (string):
     * carApplyId (string):
     * publicAuditId (string):52
     * createDate (string):
     * auditorEmail (string):本地登录本人email
     * applicatorEmail (string):本地登录本人email
     * @param params 参数
     * @return  返回
     */
    @POST("order/car/examineApplyCar")
    Call<JsonResult<Object>> examineApplyCar(@Body Map<String, Object> params);

    /**
     * 办理用车发送短信/拒绝办理
     * conductAdvice (string): 办理意见 ,
     * conductStatus (string): 办理状态 ,  31同意 32拒绝
     * conductorId (string): 办理人编号 ,
     * conductorName (string): 办理人姓名 ,
     * id (integer): 公务请车申请id ,
     * supplierCarTypeId (string): 供应商车型id ,
     * supplierId (integer): 供应商id
     * @param params 参数
     * @return  返回
     */
    @POST("order/car/sendMessage")
    Call<JsonResult<Object>> sendMessage(@Body Map<String, Object> params);

    /**
     * 获取用车范围
     * @param params 参数
     * @return  返回
     */
    @POST("order/car/getAllCarRange")
    Call<JsonResult<List<GetAllCarRange>>> getAllCarRange(@Body Map<String, Object> params);

    /**
     * 获取用车类型
     * @param params 参数
     * @return  返回
     */
    @POST("order/car/getAllCarType")
    Call<JsonResult<List<GetAllCarType>>> getAllCarType(@Body Map<String, Object> params);

    /**
     * 编辑我的用车申请
     * id
     * mobile
     * carType
     * carRange
     * numberPeople
     * startTime
     * endTime
     * applyReason
     * unitCode
     * unitName
     * accountingSubjectCode
     * accountingSubjectName
     * @param params 参数
     * @return  返回
     */
    @POST("order/car/editApplyCar")
    Call<JsonResult<Object>> editApplyCar(@Body Map<String, Object> params);

    /**
     * 编辑我的用车申请
     * @param params 参数
     * @return  返回
     */
    @POST("order/car/delApplyCar")
    Call<JsonResult<Object>> delApplyCar(@Body Map<String, Object> params);

    /**
     * 获取供应商的所有车型
     * supplierId
     * @param params 参数
     * @return  返回
     */
    @POST("order/car/getAllCarTypeBySupplierId")
    Call<JsonResult<List<GetAllSuppliers.GetAllCarTypeBySupplierId>>> getAllCarTypeBySupplierId(@Body Map<String, Object> params);

}
