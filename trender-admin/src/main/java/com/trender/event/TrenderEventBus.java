package com.trender.event;



import com.google.gwt.thirdparty.guava.common.eventbus.EventBus;
import com.google.gwt.thirdparty.guava.common.eventbus.SubscriberExceptionContext;
import com.google.gwt.thirdparty.guava.common.eventbus.SubscriberExceptionHandler;
import com.trender.TrenderUI;

/**
 * Created by Egor.Veremeychik on 21.06.2016.
 */
public class TrenderEventBus implements SubscriberExceptionHandler {

    private final EventBus eventBus = new EventBus(this);

    public static void post(final Object event) {
        TrenderUI.getTrenderEventBus().eventBus.post(event);
    }

    public static void register(final Object object) {
        TrenderUI.getTrenderEventBus().eventBus.register(object);
    }

    public static void unregister(final Object object) {
        TrenderUI.getTrenderEventBus().eventBus.unregister(object);
    }

    @Override
    public final void handleException(final Throwable exception,final SubscriberExceptionContext context) {
        exception.printStackTrace();
    }
}