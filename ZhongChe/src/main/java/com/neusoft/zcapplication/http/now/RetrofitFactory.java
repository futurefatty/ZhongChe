package com.neusoft.zcapplication.http.now;

import com.neusoft.zcapplication.base.ZcApplication;
import com.neusoft.zcapplication.http.URL;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Author: TenzLiu
 * Time: 2018/6/7 14:15
 * Desc:
 */

public class RetrofitFactory {

//    public static final String BASEURL  = "https://58.20.212.74:9005/travel_app-web/";//正式环境(不用IP用域名)
//    public static final String BASEURL  = "https://sl.csrzic.com:9005/travel_app-web/";//正式环境
//    public static final String BASEURL  = "http://58.20.212.75:8001/travel_app-web/";//测试环境

    private static final int DEFAULT_TIMEOUT_READ = 60;
    private static final int DEFAULT_TIMEOUT_WRITE = 60;
    private static final int DEFAULT_TIMEOUT_CONNECT = 60;
    private static RetrofitFactory sRetrofitFactory;
    private static OkHttpClient sOkHttpClient;
    private static Retrofit sRetrofit;
    // 避免出现 HTTP 403 Forbidden，参考：http://stackoverflow.com/questions/13670692/403-forbidden-with-java-but-not-web-browser
    public static final String AVOID_HTTP403_FORBIDDEN = "User-Agent: Mozilla/5.0 (Windows NT 6.1; " +
            "WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11";

    /**
     * 构造方法私有化
     */
    private RetrofitFactory() {
        sOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT_CONNECT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT_WRITE, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT_READ, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .cache(new Cache(new File(ZcApplication.getInstance()
                        .getCacheDir(), ""), 1024 * 1024 * 10))
                .addInterceptor(InterceptorUtil.getHeadInterceptor())
                .addInterceptor(InterceptorUtil.getLogInterceptor())
                .addInterceptor(InterceptorUtil.getCacheInterceptor())
                .addNetworkInterceptor(InterceptorUtil.getCacheInterceptor())
                .build();
        sRetrofit = new Retrofit.Builder()
                .baseUrl(URL.IP)
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(sOkHttpClient)
                .build();
    }

    /**
     * 单例获取
     *
     * @return
     */
    public static RetrofitFactory getInstance() {
        if (sRetrofitFactory == null) {
            synchronized (RetrofitFactory.class) {
                if (sRetrofitFactory == null) {
                    sRetrofitFactory = new RetrofitFactory();
                }
            }
        }
        return sRetrofitFactory;
    }

    /**
     * 创建API
     *
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> T createApi(Class<T> tClass) {
        return sRetrofit.create(tClass);
    }

    /**
     * 创建API
     *
     * @param baseUrl 多个BaseURL切换
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> T createApi(String baseUrl, Class<T> tClass) {
        sRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(sOkHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return sRetrofit.create(tClass);
    }

}
