package com.trender;

import com.trender.component.AdminMenu;
import com.trender.entity.User;
import com.trender.event.TrenderEvent.BrowserResizeEvent;
import com.trender.event.TrenderEventBus;
import com.trender.service.UserService;
import com.trender.view.AdminView;
import com.trender.view.LoginView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.server.Page;
import com.vaadin.server.Page.BrowserWindowResizeEvent;
import com.vaadin.server.Page.BrowserWindowResizeListener;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the us interface and initialize non-component functionality.
 */


@Theme("mytheme")
@SpringUI
//@Widgetset("com.trender.MyAppWidgetset")
public class TrenderUI extends UI {

    @Autowired
    private SpringViewProvider viewProvider;

    private Navigator navigator;
    private final TrenderEventBus trenderEventBus = new TrenderEventBus();

    @Override
    protected void init(VaadinRequest request) {

        navigator = new Navigator(this, this);
        navigator.addProvider(viewProvider);

        TrenderEventBus.register(this);
        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);

        updateContext();

        Page.getCurrent().addBrowserWindowResizeListener(
                new BrowserWindowResizeListener() {
                    @Override
                    public void browserWindowResized(
                            final BrowserWindowResizeEvent event) {
                        TrenderEventBus.post(new BrowserResizeEvent());
                    }
                });
    }

    private void updateContext() {
        User user = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());

        //TODO huinya polnaya

       /* if (user != null && "admin".equals(user.getRoles())) {
            // Authenticated user
            setContent(new AdminView());
            //removeStyleName("loginview");
            getNavigator().navigateTo(getNavigator().getState());
        } else {
            setContent(new LoginView());
            //addStyleName("loginview");
        }*/

        setContent(new AdminView());
        navigator.addView(AdminView.VIEW_NAME, new AdminView());
        //getNavigator().navigateTo(getNavigator().getState());

        navigator.navigateTo(AdminView.VIEW_NAME);
    }

    public static TrenderEventBus getTrenderEventBus() {
        return ((TrenderUI) getCurrent()).trenderEventBus;
    }
}
