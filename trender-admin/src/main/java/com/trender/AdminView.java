package com.trender;

import com.trender.component.AdminMenu;
import com.trender.component.Header;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;

/**
 * Created by EgorVeremeychik on 19.06.2016.
 */
@Theme("mytheme")
@SpringView(name = AdminView.VIEW_NAME)
public class AdminView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "admin_panel";

    public AdminView() {
        setSizeFull();

        VerticalLayout head = new VerticalLayout();

        //addComponent(new Header().intitUserPageComponent("Admin"));

        addComponent(new AdminMenu());
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
