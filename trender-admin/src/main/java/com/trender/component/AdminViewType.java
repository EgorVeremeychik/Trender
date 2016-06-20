package com.trender.component;

/**
 * Created by Egor.Veremeychik on 20.06.2016.
 */
import com.trender.question.QuestionView;
import com.trender.user.UserView;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

public enum AdminViewType {
    QUESTION("question", QuestionView.class, FontAwesome.TABLE, false),
    USER("user", UserView.class, FontAwesome.TABLE, false);

    private final String viewName;
    private final Class<? extends View> viewClass;
    private final Resource icon;
    private final boolean stateful;

    private AdminViewType(final String viewName,
                              final Class<? extends View> viewClass, final Resource icon,
                              final boolean stateful) {
        this.viewName = viewName;
        this.viewClass = viewClass;
        this.icon = icon;
        this.stateful = stateful;
    }

    public boolean isStateful() {
        return stateful;
    }

    public String getViewName() {
        return viewName;
    }

    public Class<? extends View> getViewClass() {
        return viewClass;
    }

    public Resource getIcon() {
        return icon;
    }

    public static AdminViewType getByViewName(final String viewName) {
        AdminViewType result = null;
        for (AdminViewType viewType : values()) {
            if (viewType.getViewName().equals(viewName)) {
                result = viewType;
                break;
            }
        }
        return result;
    }

}
