package com.neusoft.zcapplication.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.crcc.commonlib.ApplicationDispatch;
import com.crcc.commonlib.CommonApplication;
import com.neusoft.zcapplication.tools.LogUtil;

/**
 * Created by Administrator on 2017/11/7.
 * application
 */

public class ZcApplication extends Application {

    private static ZcApplication sApplication;
    protected static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        sContext = getApplicationContext();
        LogUtil.init(true);
        ApplicationDispatch.getInstance().
                registerApplication(CommonApplication.class)
                .dispatch(this);
    }

    /**
     * 获取当前对象
     *
     * @return
     */
    public static synchronized ZcApplication getInstance() {
        return sApplication;
    }

    /**
     * 获取上下文对象
     *
     * @return
     */
    public static Context getAppContext() {
        return sContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

}
