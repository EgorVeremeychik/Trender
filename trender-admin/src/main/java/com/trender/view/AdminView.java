package com.trender.view;

import com.trender.TrenderNavigator;
import com.trender.component.AdminMenu;
import com.trender.service.UserService;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by EgorVeremeychik on 19.06.2016.
 */

@SpringView(name = AdminView.VIEW_NAME)
public class AdminView extends HorizontalLayout implements View {

    public static final String VIEW_NAME = "admin_panel";

    public AdminView() {
        setSizeFull();
        //addStyleName("mainview");
        addComponent(new AdminMenu());

        ComponentContainer content = new CssLayout();
        //content.addStyleName("view-content");
        content.setSizeFull();
        addComponent(content);
        setExpandRatio(content, 70);

        new TrenderNavigator(content);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
