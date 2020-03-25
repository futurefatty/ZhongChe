package com.neusoft.zcapplication.http.now;

import com.neusoft.zcapplication.tools.ToastUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by longbh on 16/5/25.
 * 网络请求回调
 */
public abstract class CallBack<T> implements Callback<JsonResult<T>> {

    @Override
    public void onResponse(Call<JsonResult<T>> call, Response<JsonResult<T>> response) {
        JsonResult<T> result = response.body();
        try {
            if (result == null) {
                ToastUtil.toast("服务器连接失败");
                fail("000001");
            } else {
                if (result.isSuccess()) {
                    success(result.data);
                } else {
                    ToastUtil.toast(result.codeMsg);
                    fail(result.code);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.toast("服务器连接失败");
            fail("000001");
        }
    }

    @Override
    public void onFailure(Call<JsonResult<T>> call, Throwable t) {
        try {
            ToastUtil.toast("服务器连接失败");
            fail("000001");
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.toast("服务器连接失败");
            fail("000001");
        }
    }

    public abstract void success(T response);

    public abstract void fail(String code);

}
