/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trender.config;

import com.trender.MyUI;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.spring.server.SpringVaadinServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet(value = "/*", asyncSupported = true)
@VaadinServletConfiguration(widgetset="com.trender.MyWidgetSet",productionMode = false, ui = MyUI.class)
public class ServletConfig extends SpringVaadinServlet {

}
