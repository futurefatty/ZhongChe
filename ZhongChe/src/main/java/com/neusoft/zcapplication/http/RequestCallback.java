package com.neusoft.zcapplication.http;


/**
 * Created by Administrator on 2017/11/20.
 * 网络请求回调接口
 */

 public interface RequestCallback {
    void requestSuccess(Object map, int type);
    void requestFail(int type);
    void requestCancel(int type);
}
