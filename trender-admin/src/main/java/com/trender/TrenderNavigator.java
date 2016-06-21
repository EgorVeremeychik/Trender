package com.trender;

import com.trender.component.AdminViewType;
import com.trender.event.TrenderEvent.PostViewChangeEvent;
import com.trender.event.TrenderEvent.BrowserResizeEvent;
import com.trender.event.TrenderEvent.CloseOpenWindowsEvent;
import com.trender.event.TrenderEventBus;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 * Created by Egor.Veremeychik on 21.06.2016.
 */

public class TrenderNavigator extends Navigator {

    private static final AdminViewType ERROR_VIEW = AdminViewType.QUESTION;
    private ViewProvider errorViewProvider;

    public TrenderNavigator(final ComponentContainer container) {

        super(UI.getCurrent(), container);
        String host = getUI().getPage().getLocation().getHost();
        System.out.println(host);
        initViewChangeListener();
        initViewProviders();
    }

    private void initViewChangeListener() {
        addViewChangeListener(new ViewChangeListener() {

            @Override
            public boolean beforeViewChange(final ViewChangeEvent event) {

                return true;
            }

            @Override
            public void afterViewChange(final ViewChangeEvent event) {
                AdminViewType view = AdminViewType.getByViewName(event.getViewName());
                TrenderEventBus.post(new PostViewChangeEvent(view));
                TrenderEventBus.post(new BrowserResizeEvent());
                TrenderEventBus.post(new CloseOpenWindowsEvent());
            }
        });
    }

    private void initViewProviders() {
        for (final AdminViewType viewType : AdminViewType.values()) {
            ViewProvider viewProvider = new ClassBasedViewProvider(viewType.getViewName(), viewType.getViewClass()) {

                private View cachedInstance;

                @Override
                public View getView(final String viewName) {
                    View result = null;
                    if (viewType.getViewName().equals(viewName)) {
                        if (viewType.isStateful()) {
                            if (cachedInstance == null) {
                                cachedInstance = super.getView(viewType
                                        .getViewName());
                            }
                            result = cachedInstance;
                        } else {
                            result = super.getView(viewType.getViewName());
                        }
                    }
                    return result;
                }
            };

            if (viewType == ERROR_VIEW) {
                errorViewProvider = viewProvider;
            }

            addProvider(viewProvider);
        }

        setErrorProvider(new ViewProvider() {
            @Override
            public String getViewName(final String viewAndParameters) {
                return ERROR_VIEW.getViewName();
            }

            @Override
            public View getView(final String viewName) {
                return errorViewProvider.getView(ERROR_VIEW.getViewName());
            }
        });
    }

}
