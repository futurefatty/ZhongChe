package com.neusoft.zcapplication.api;

import com.neusoft.zcapplication.entity.GetVersion;
import com.neusoft.zcapplication.entity.Login;
import com.neusoft.zcapplication.http.now.JsonResult;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Author: TenzLiu
 * Time: 2018/6/12 11:05
 * Desc:  个人信息
 */

public interface MemberApi {

    /**
     * 用户登录
     * ciphertext (string): 密文 ,
     * employeeCode (string): 员工编号 ,
     * loginType (integer): 用户登录途径，微信：1；app：2；pc：3 ,
     * password (string): 加密后的密码
     * @param params 参数
     * @return  返回
     */
    @POST("member/sys/login")
    Call<JsonResult<Login>> login(@Body Map<String, Object> params);

    /**
     * 获取更新
     * @param type 2
     * @return  返回
     */
    @GET("member/version/getNewVersion")
    Call<JsonResult<List<GetVersion>>> getNewVersion(@Query("type") int type);

    /**
     * 编辑个人信息保存
     * address (string): 地址 ,
     * birthday (string): 生日 ,
     * ciphertext (string): 密文 ,
     * employeeCode (string): 员工编号 ,
     * employeeName (string): 员工姓名，按道理来说是不能改的，但是还是先传参，到时候要改的话接口改就好 ,
     * gender (string): 性别，1：男，2：女。按道理来说是不能改的，但是还是先传参，到时候要改的话接口改就好 ,
     * loginType (integer): 用户登录途径，微信：1；app：2；pc：3 ,
     * mail (string): 电子邮箱 ,
     * mobil (string): 电话
     * @param params 参数
     * @return  返回
     */
    @POST("member/modify/updateEmployeeInfo")
    Call<JsonResult<Object>> updateEmployeeInfo(@Body Map<String,Object> params);

    /**
     * 修改密码
     * ciphertext (string): 密文 ,
     * employeeCode (string): 员工编号 ,
     * loginType (integer): 用户登录途径，微信：1；app：2；pc：3 ,
     * newPassword (string): 加密后的新密码 ,
     * oldPassword (string): 加密后的旧密码
     * @param params 参数
     * @return  返回
     */
    @POST("member/changePsw/setPassword")
    Call<JsonResult<Object>> setPassword(@Body Map<String,Object> params);

    @POST("login/process")
    @Headers({"Content-Type:application/x-www-form-urlencoded; charset=UTF-8"})
    Call<Object> process(@Body Map<String,Object> params);
}
