package com.neusoft.zcapplication.http;

import android.content.Context;

import com.neusoft.zcapplication.widget.LoadingDialog;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * 网络请求工具类
 */
public class RequestUtil {

    private LoadingDialog loadDialog;

    /**
     * 获取Retrofit对象
     * @param url
     * @return
     */
    public Retrofit getRequestClient(String url){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(InterceptorUtil.getHeadInterceptor())
                .addInterceptor(InterceptorUtil.getLogInterceptor())
                .addInterceptor(InterceptorUtil.getCacheInterceptor())
                .addNetworkInterceptor(InterceptorUtil.getCacheInterceptor())
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                //增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client).build();
        return retrofit;
    }

    /**
     * 统一请求数据的方法
     * @param call
     * @param callback
     */
    public void requestData(Call<Map<String,Object>> call,final RequestCallback callback,final int type,Context context){
        showLoading("",true,context);
        call.enqueue(new Callback<Map<String,Object>>() {
            /**
             * @param call
             * @param response
             */
            @Override
            public void onResponse(Call<Map<String,Object>> call, Response<Map<String,Object>> response) {
                stopLoading();
                Map<String,Object> result = response.body();
                if(null == result){
                    callback.requestFail(type);
                }else{
                    callback.requestSuccess(result,type);
                }
            }
            @Override
            public void onFailure(Call<Map<String,Object>> call, Throwable t) {
                stopLoading();
                if(call.isCanceled()){
                    //取消操作之后，进行相关的提示
                    callback.requestCancel(type);
                }else{
                    callback.requestFail(type);
                }
            }
        });
    }
    public void requestDataNoLoading(Call<Map<String,Object>> call,final RequestCallback callback,final int type,Context context){
        call.enqueue(new Callback<Map<String,Object>>() {
            /**
             * @param call
             * @param response
             */
            @Override
            public void onResponse(Call<Map<String,Object>> call, Response<Map<String,Object>> response) {
                Map<String,Object> result = response.body();
                if(null == result){
                    callback.requestFail(type);
                }else{
                    callback.requestSuccess(result,type);
                }
            }
            @Override
            public void onFailure(Call<Map<String,Object>> call, Throwable t) {
                if(call.isCanceled()){
                    //取消操作之后，进行相关的提示
                    callback.requestCancel(type);
                }else{
                    callback.requestFail(type);
                }
            }
        });
    }
    /**
     * 统一请求数据的方法，带加载框
     * @param call
     * @param callback
     * @param bool true 加载框可以通过按返回键取消
     */
    public void requestData(Call<Map<String,Object>> call,final RequestCallback callback,final int type,
                            String loadStr,boolean bool,Context context){
        //显示加载框
        showLoading(loadStr,bool,context);

        call.enqueue(new Callback<Map<String,Object>>() {

            /**
             * @param call
             * @param response
             */
            @Override
            public void onResponse(Call<Map<String,Object>> call, Response<Map<String,Object>> response) {
                stopLoading();
                Map<String,Object> result = response.body();
                if(null == result){
                    callback.requestFail(type);
                }else{
                    callback.requestSuccess(result,type);
                }
            }
            @Override
            public void onFailure(Call<Map<String,Object>> call, Throwable t) {
                stopLoading();
                if(call.isCanceled()){
                    //取消操作之后，进行相关的提示
                    callback.requestCancel(type);
                }else{
                    callback.requestFail(type);
                }
            }
        });
    }

    /**
     *
     * @param str 提示文字
     * @param bool true可取消
     */
    private void showLoading(String str, boolean bool, Context context){
        if (loadDialog == null){
            loadDialog = LoadingDialog.createDialog(context);
            loadDialog.setMessage(str);
            loadDialog.setCancelable(bool);
        }
        if(!loadDialog.isShowing()){
            loadDialog.show();
        }
    }

    /**
     * 关闭加载框
     */
    private void stopLoading(){
        if (loadDialog != null){
            loadDialog.dismiss();
            loadDialog = null;
        }
    }
}
