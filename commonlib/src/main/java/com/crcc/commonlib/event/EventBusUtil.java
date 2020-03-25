package com.crcc.commonlib.event;

import org.greenrobot.eventbus.EventBus;


public class EventBusUtil {

    public static void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    public static void sendEvent(Event event) {
        EventBus.getDefault().post(event);
    }

    public static boolean isRegister(Object subscriber) {
        return EventBus.getDefault().isRegistered(subscriber);
    }

}
