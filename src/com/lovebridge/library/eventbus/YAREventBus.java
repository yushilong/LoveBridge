
package com.lovebridge.library.eventbus;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * @author yushilong
 * @date 2014-9-30
 * @version 1.0
 */
public class YAREventBus {
    private static volatile YAREventBus defaultInstance;
    private final WeakHashMap<Object, List<Object>> notificateMaps;

    public static YAREventBus getInstance() {
        if (defaultInstance == null) {
            synchronized (YAREventBus.class) {
                if (defaultInstance == null) {
                    defaultInstance = new YAREventBus();
                }
            }
        }
        return defaultInstance;
    }

    public YAREventBus() {
        notificateMaps = new WeakHashMap<Object, List<Object>>();
    }

    // 此函数的设计，是注册你要监听的事件名称，监听对象，一般放在onResume里面，activity,fragment均可
    public void subscribe(Object notificationName, SubscriberChangeListener subscriberChangeListener) {
        unSubscribe(notificationName, subscriberChangeListener);
        List<Object> subscribedEvents = notificateMaps.get(notificationName);
        if (subscribedEvents == null) {
            subscribedEvents = new ArrayList<Object>();
            notificateMaps.put(notificationName, subscribedEvents);
        }
        subscribedEvents.add(subscriberChangeListener);
    }

    // 订阅某一个观察者中的多个事件
    public void subscribes(SubscriberChangeListener subscriberChangeListener, Object... notificationNames) {
        for (int i = 0; i < notificationNames.length; i++) {
            subscribe(notificationNames[i], subscriberChangeListener);
        }
    }

    // 此函数的设计，是取消注册时监听的事件名称，监听对象，一般放在onDestory里面,千万不要放在onPause里面，不然会监听不到事件。activity,fragment均可
    public void unSubscribe(Object notificationName, SubscriberChangeListener subscriberChangeListener) {
        List<Object> subscribedEvents = notificateMaps.get(notificationName);
        if (subscribedEvents != null) {
            if (subscribedEvents.contains(subscriberChangeListener)) {
                subscribedEvents.remove(subscriberChangeListener);
            }
            if (subscribedEvents.isEmpty()) {
                notificateMaps.remove(subscriberChangeListener);
            }
        }
    }

    // 取消订阅某一个观察者中的多个事件
    public void unSubscribes(SubscriberChangeListener subscriberChangeListener, Object... notificationNames) {
        for (int i = 0; i < notificationNames.length; i++) {
            unSubscribe(notificationNames[i], subscriberChangeListener);
        }
    }

    // 此函数的设计，通知监听了事件名称的对象集，进行对象的更改。activity,fragment均可
    public void notifiDataUpdate(Object notificationName, Object notificateContent) {
        List<Object> subscribedEvents = notificateMaps.get(notificationName);
        if (subscribedEvents != null) {
            for (Object eventType : subscribedEvents) {
                SubscriberChangeListener subscriberChangeListener = (SubscriberChangeListener)eventType;
                subscriberChangeListener.onSubscriberDataChanged(notificationName, notificateContent);
            }
        }
    }

    // 当有事件发生时的，会回调这个函数
    public interface SubscriberChangeListener {
        public void onSubscriberDataChanged(Object notificationName, Object notificateContent);
    }
}
