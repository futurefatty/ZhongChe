package com.crcc.commonlib;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.crcc.commonlib.loadsir.EmptyCallBack;
import com.crcc.commonlib.loadsir.LoadingCallBack;
import com.crcc.commonlib.loadsir.NoNetWorkCallBack;
import com.crcc.commonlib.loadsir.RetryCallBack;
import com.kingja.loadsir.core.LoadSir;

/**
 * author:Six
 * Date:2019/3/7
 */
public class CommonApplication implements IApplication {
    private static Application application;

    public static Application getApplication() {
        return application;
    }

    @Override
    public void onCreate(Application application) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
        CommonApplication.application = application;
        LoadSir.beginBuilder().addCallback(new LoadingCallBack())
                .addCallback(new EmptyCallBack())
                .addCallback(new RetryCallBack())
                .addCallback(new NoNetWorkCallBack())
                .commit();
    }
}
