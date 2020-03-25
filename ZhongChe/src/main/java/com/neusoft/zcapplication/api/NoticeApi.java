package com.neusoft.zcapplication.api;

import com.neusoft.zcapplication.entity.GetNotice;
import com.neusoft.zcapplication.http.now.JsonResult;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NoticeApi {
//    @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
    @POST("zc/notice/list")
    Call<JsonResult<List<GetNotice>>> getNoticeList();
}
