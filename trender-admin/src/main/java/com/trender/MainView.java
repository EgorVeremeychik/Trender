package com.trender;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

/**
 * Created by Egor.Veremeychik on 16.06.2016.
 */

@SpringView(name = MainView.VIEW_NAME)

public class MainView extends Panel implements View {

    public static final String VIEW_NAME = "";

    public MainView() {

        Label welcome = new Label("Welcome");
        setContent(welcome);

    }

    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}