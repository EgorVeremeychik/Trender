package com.trender.component;

/*import com.trender.user.AddEditStudyView;
import com.trender.user.ProfilePageView;
import com.trender.user.UserPageView;*/
import com.vaadin.navigator.Navigator;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

/**
 * Created by Egor.Veremeychik on 20.06.2016.
 */
public class Header extends CssLayout {

    private Button singInOrSingOutButton, singUpButton;
    private Label nameCompany, userNameLable;
    private MenuBar menuBar;
    private Navigator navigator;
    private Window window;

    public Header() {
        super();
    }

    public Component intitUserPageComponent(String name) {
        init();

        userNameLable = new Label();
        userNameLable.setWidth(400, Unit.PIXELS);
        userNameLable.setValue("Добро пожаловать " + name);
        userNameLable.addStyleName("v-label-header-user-name");
        userNameLable.addStyleName("v-label");
        singInOrSingOutButton = new Button();
        singInOrSingOutButton.setCaption("Выйти");
        singInOrSingOutButton.addStyleName("v-button-header-sing-out");
        singInOrSingOutButton.addStyleName("v-button-primary");

        MenuBar.Command command = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                Header.this.menuSelected(selectedItem.getId());
            }
        };

        menuBar.addItem("Мои иследования", command);
        menuBar.addItem("Создать новое иследования", command);
        menuBar.addItem("Профиль", command);

        addComponent(userNameLable);
        addComponent(singInOrSingOutButton);
        addComponent(menuBar);

        return this;
    }

    private void init() {
        setSizeFull();
        addStyleName("v-background");
        setHeight(70, Unit.PIXELS);

        nameCompany = new Label();
        nameCompany.setValue("Trender");
        nameCompany.addStyleName("v-label");
        nameCompany.addStyleName("v-label-align-center");
        nameCompany.addStyleName("v-logo");
        nameCompany.setWidth(150, Unit.PIXELS);

        menuBar = new MenuBar();
        menuBar.setSizeFull();
        menuBar.setWidth(100.6F, Unit.PERCENTAGE);
        menuBar.setHeight(35, Unit.PIXELS);
        menuBar.addStyleName("v-menubar");
        menuBar.addStyleName("v-menu");

        addComponentAsFirst(nameCompany);
    }

    private void menuSelected(int id) {
        navigator = getUI().getNavigator();
        System.out.println(id);
        /*switch (id) {
            case 2:
                System.out.println("Вызов: " + UserPageView.USER_PAGE_VIEW_NAME);
                navigator.navigateTo(UserPageView.USER_PAGE_VIEW_NAME);
                break;
            case 3:
                System.out.println("Вызов: " + AddEditStudyView.ADD_EDIT_STUDY_VIEW_NAME);
                navigator.navigateTo(AddEditStudyView.ADD_EDIT_STUDY_VIEW_NAME);
                break;
            case 4:
                System.out.println("Вызов: " + ProfilePageView.PROFILE_PAGE_VIEW_NAME);
                navigator.navigateTo(ProfilePageView.PROFILE_PAGE_VIEW_NAME);
                break;
        }*/
    }

    private void initWindowModal(int size, String name, boolean flag) {

        window = new Window(name);
        window.setWidth(size, Unit.PIXELS);
        window.setResizable(false);

        /*if (flag) {
            window.setContent(new UserForm().loginForm());
        }
        else{
            window.setContent(new UserForm().regForm());
        }*/

        window.setModal(true);
        window.center();
        UI.getCurrent().addWindow(window);
    }

}
