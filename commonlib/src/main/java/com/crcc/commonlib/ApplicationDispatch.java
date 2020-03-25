package com.crcc.commonlib;

import android.app.Application;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


/**
 * author:Six
 * Date:2019/2/21
 */
public class ApplicationDispatch {


    private final Map<String, IApplication> applicationMap;
    private final Map<String, Class<? extends IApplication>> classMap;

    private static class ApplicationDispatchHolder {
        private static final ApplicationDispatch INSTANCE = new ApplicationDispatch();
    }


    private ApplicationDispatch() {
        applicationMap = new LinkedHashMap<>();
        classMap = new LinkedHashMap<>();
    }

    public ApplicationDispatch registerApplication(Class<? extends IApplication> classz) {
        classMap.put(classz.getName(), classz);
        return this;
    }

    public ApplicationDispatch dispatch(Application application) {
        Set<String> keySet = classMap.keySet();
        for (String key : keySet) {
            IApplication iApplication = findIApplication(key);
            if (iApplication != null) {
                iApplication.onCreate(application);
            }
        }
        return this;
    }

    private IApplication findIApplication(String key) {
        IApplication iApplication = null;
        try {
            iApplication = applicationMap.get(key);
            if (iApplication == null) {
                Class<? extends IApplication> aClass = classMap.get(key);
                iApplication = aClass.newInstance();
                applicationMap.put(key, iApplication);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return iApplication;
    }

    public static ApplicationDispatch getInstance() {
        return ApplicationDispatchHolder.INSTANCE;
    }
}
