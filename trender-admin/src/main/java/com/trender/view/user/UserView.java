package com.trender.view.user;

import java.util.Set;
import java.util.List;
import com.vaadin.ui.*;
import com.vaadin.data.Item;
import com.vaadin.server.Page;
import com.trender.dao.UserDao;
import com.trender.entity.User;
import com.vaadin.event.Action;
import com.vaadin.data.Property;
import com.vaadin.navigator.View;
import com.vaadin.server.Responsive;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.event.Action.Handler;
import com.vaadin.shared.ui.MarginInfo;
import com.trender.service.UserService;
import com.vaadin.data.Container.Filter;
import com.trender.event.TrenderEventBus;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Table.TableDragMode;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.trender.service.exception.ServiceException;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.trender.event.TrenderEvent.BrowserResizeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.google.gwt.thirdparty.guava.common.eventbus.Subscribe;

/**
 * Created by Egor.Veremeychik on 20.06.2016.
 */

@SuppressWarnings({"serial", "unchecked"})
@SpringView(name = com.trender.view.question.QuestionView.VIEW_NAME)
public final class UserView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "users";

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    private final Table table;
    private Button createUser;
    private Component filter;
    private static final String[] DEFAULT_COLLAPSIBLE = {"id", "first name", "second name", "email"};

    public UserView() {
        setSizeFull();
        addStyleName("transactions");
        TrenderEventBus.register(this);
        addComponent(buildToolbar());
        table = buildTable();
        addComponent(table);
        setExpandRatio(table, 1);
    }

    @Override
    public void detach() {
        super.detach();
        TrenderEventBus.unregister(this);
    }

    private Component buildToolbar() {
        HorizontalLayout header = new HorizontalLayout();
        header.setWidth(100, Unit.PERCENTAGE);

        HorizontalLayout headerL = new HorizontalLayout();
        headerL.setSpacing(true);
        Responsive.makeResponsive(headerL);
        Label title = new Label("Users editor");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        headerL.addComponent(title);

        createUser = buildCreateUser();
        filter = buildFilter();
        HorizontalLayout headerR = new HorizontalLayout(filter, createUser);
        headerR.setSpacing(true);
        header.addComponent(headerL);
        header.addComponent(headerR);
        header.setComponentAlignment(headerR, Alignment.MIDDLE_RIGHT);
        headerR.setMargin(new MarginInfo(false, true, false, true));

        return header;
    }

    private Button buildCreateUser() {
        final Button createUser = new Button("Create User");
        createUser.setDescription("Create new user");
       /* createReport.addClickListener(new MouseEvents.ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                createNewReportFromSelection();
            }
        });*/
        return createUser;
    }

    private Component buildFilter() {
        final TextField filter = new TextField();
        filter.addTextChangeListener(new TextChangeListener() {
            @Override
            public void textChange(final TextChangeEvent event) {
                Filterable data = (Filterable) table.getContainerDataSource();
                data.removeAllContainerFilters();
                data.addContainerFilter(new Filter() {
                    @Override
                    public boolean passesFilter(final Object itemId, final Item item) {
                        if (event.getText() == null || event.getText().equals("")) {
                            return true;
                        }
                        return filterByProperty("first name", item, event.getText())
                                || filterByProperty("second name", item, event.getText())
                                || filterByProperty("email", item, event.getText());
                    }

                    @Override
                    public boolean appliesToProperty(final Object propertyId) {
                        if (propertyId.equals("first name")
                                || propertyId.equals("second name")
                                || propertyId.equals("email")) {
                            return true;
                        }
                        return false;
                    }
                });
            }
        });

        filter.setInputPrompt("Filter");
        filter.setIcon(FontAwesome.SEARCH);
        filter.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        filter.addShortcutListener(new ShortcutListener("Clear",KeyCode.ESCAPE, null) {
            @Override
            public void handleAction(final Object sender, final Object target) {
                filter.setValue("");
                ((Filterable) table.getContainerDataSource()).removeAllContainerFilters();
            }
        });
        return filter;
    }

    private Table buildTable() {
        final Table table = new Table() {
            @Override
            protected String formatPropertyValue(final Object rowId, final Object colId, final Property<?> property) {
                String result = super.formatPropertyValue(rowId, colId, property);
                return result;
            }
        };

        table.setSizeFull();
        table.addStyleName(ValoTheme.TABLE_BORDERLESS);
        table.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        table.addStyleName(ValoTheme.TABLE_COMPACT);
        table.setSelectable(true);
        table.setColumnCollapsingAllowed(true);
        table.setColumnReorderingAllowed(true);

        try {
            IndexedContainer container = new IndexedContainer();
            container.addContainerProperty("ID", Long.class, "");
            container.addContainerProperty("First name", String.class, "");
            container.addContainerProperty("Second name", String.class, "");
            container.addContainerProperty("Email", String.class, "");

            List<User> users = userService.getAll();
            for (User user : users) {
                Item item = container.addItem(user);
                item.getItemProperty("ID").setValue(user.getId());
                item.getItemProperty("First name").setValue(user.getFirstName());
                item.getItemProperty("Second name").setValue(user.getSecondName());
                item.getItemProperty("Email").setValue(user.getEmail());
            }
            table.setContainerDataSource(container);

            table.setSortContainerPropertyId("time");
            table.setSortAscending(false);

            table.setColumnAlignment("Email", Table.Align.RIGHT);

            table.setVisibleColumns("First name", "Second name", "Email");
            table.setColumnHeaders("First name", "Second name", "Email");

            table.setFooterVisible(true);

            table.setDragMode(TableDragMode.MULTIROW);
            table.setMultiSelect(true);

            table.addActionHandler(new TransactionsActionHandler());

            table.addValueChangeListener(new ValueChangeListener() {
                @Override
                public void valueChange(final ValueChangeEvent event) {
                    if (table.getValue() instanceof Set) {
                        Set<Object> val = (Set<Object>) table.getValue();
                        createUser.setEnabled(val.size() > 0);
                    }
                }
            });
            table.setImmediate(true);

        } catch (ServiceException e) {
            e.printStackTrace();
        }

        return table;
    }

    private boolean defaultColumnsVisible() {
        boolean result = true;
        for (String propertyId : DEFAULT_COLLAPSIBLE) {
            if (table.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
                result = false;
            }
        }
        return result;
    }

    @Subscribe
    public void browserResized(final BrowserResizeEvent event) {
        if (defaultColumnsVisible()) {
            for (String propertyId : DEFAULT_COLLAPSIBLE) {
                table.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
            }
        }
    }

    private boolean filterByProperty(final String prop, final Item item,final String text) {
        if (item == null || item.getItemProperty(prop) == null
                || item.getItemProperty(prop).getValue() == null) {
            return false;
        }
        String val = item.getItemProperty(prop).getValue().toString().trim()
                .toLowerCase();
        if (val.contains(text.toLowerCase().trim())) {
            return true;
        }
        return false;
    }

    void createNewUserFromSelection() {
        //TODO modal create user
        //UI.getCurrent().getNavigator().navigateTo(AdminViewType.QUESTION.getViewName());
        //TrenderEventBus.post(new TransactionReportEvent((Collection<Question>) table.getValue()));
    }

    @Override
    public void enter(final ViewChangeEvent event) {
    }

    private class TransactionsActionHandler implements Handler {
        private final Action report = new Action("Create User");

        private final Action discard = new Action("Discard");

        private final Action details = new Action("Movie details");

        @Override
        public void handleAction(final Action action, final Object sender,
                                 final Object target) {
            if (action == report) {
                createNewUserFromSelection();
            } else if (action == discard) {
                Notification.show("Not implemented");
            } else if (action == details) {
                Item item = ((Table) sender).getItem(target);
                if (item != null) {
                    Long userId = (Long) item.getItemProperty("movieId").getValue();
                    //MovieDetailsWindow.open(DashboardUI.getDataProvider().getMovie(movieId), null, null);
                }
            }
        }

        @Override
        public Action[] getActions(final Object target, final Object sender) {
            return new Action[]{details, report, discard};
        }
    }

   /* private class TempTransactionsContainer extends
            FilterableListContainer<Transaction> {

        public TempTransactionsContainer(
                final Collection<Transaction> collection) {
            super(collection);
        }

        // This is only temporarily overridden until issues with
        // BeanComparator get resolved.
        @Override
        public void sort(final Object[] propertyId, final boolean[] ascending) {
            final boolean sortAscending = ascending[0];
            final Object sortContainerPropertyId = propertyId[0];
            Collections.sort(getBackingList(), new Comparator<Transaction>() {
                @Override
                public int compare(final Transaction o1, final Transaction o2) {
                    int result = 0;
                    if ("time".equals(sortContainerPropertyId)) {
                        result = o1.getTime().compareTo(o2.getTime());
                    } else if ("country".equals(sortContainerPropertyId)) {
                        result = o1.getCountry().compareTo(o2.getCountry());
                    } else if ("city".equals(sortContainerPropertyId)) {
                        result = o1.getCity().compareTo(o2.getCity());
                    } else if ("theater".equals(sortContainerPropertyId)) {
                        result = o1.getTheater().compareTo(o2.getTheater());
                    } else if ("room".equals(sortContainerPropertyId)) {
                        result = o1.getRoom().compareTo(o2.getRoom());
                    } else if ("title".equals(sortContainerPropertyId)) {
                        result = o1.getTitle().compareTo(o2.getTitle());
                    } else if ("seats".equals(sortContainerPropertyId)) {
                        result = new Integer(o1.getSeats()).compareTo(o2
                                .getSeats());
                    } else if ("price".equals(sortContainerPropertyId)) {
                        result = new Double(o1.getPrice()).compareTo(o2
                                .getPrice());
                    }

                    if (!sortAscending) {
                        result *= -1;
                    }
                    return result;
                }
            });
        }

    }*/

}

