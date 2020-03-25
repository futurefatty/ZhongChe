package com.crcc.commonlib.utils;

import android.app.Activity;

import java.util.LinkedList;

/**
 * Created  2018/3/10.
 *
 * @author six
 */

public class ActivityStack {

    private static ActivityStack activityStack;
    private static LinkedList<Activity> activities = new LinkedList<>();


    private ActivityStack() {
    }


    //静态工厂方法
    public static ActivityStack getInstance() {
        if (activityStack == null) {
            activityStack = new ActivityStack();
        }
        return activityStack;
    }


    public static int size() {
        return activities.size();
    }

    /**
     * 加入队列
     */
    public void registerActivity(Activity activity) {
        try {
            if (-1 == containActivity(activity.getClass())) {
                activities.addFirst(activity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int containActivity(Class activityClass) {
        try {
            if (activities != null) {
                for (int index = 0; index < activities.size(); index++) {
                    Activity activity = activities.get(index);
                    if (activity.getClass().equals(activityClass)) {
                        return index;
                    }
                }
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    /**
     * 从队列删除
     */
    public void unRegisterActivity(Activity activity) {
        try {
            int index = containActivity(activity.getClass());
            if (-1 != index) {
                activities.remove(index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void finishActivity(Class activityClass) {
        try {
            int index = containActivity(activityClass);
            if (-1 != index) {
                Activity activity = activities.remove(index);
                activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 关闭Activity列表中的所有Activity
     */
    public void finishAllActivity() {
        try {
            for (Activity activity : activities) {
                activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            activities.clear();
        }
    }
}
