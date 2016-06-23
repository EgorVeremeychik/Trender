package com.trender.view.question;


import java.util.Set;
import java.util.List;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import java.util.ArrayList;
import com.vaadin.data.Item;
import com.vaadin.server.Page;
import com.vaadin.event.Action;
import com.vaadin.data.Property;
import com.vaadin.navigator.View;
import com.trender.dao.QuestionDao;
import com.trender.entity.Question;
import com.vaadin.server.Responsive;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.event.Action.Handler;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.data.Container.Filter;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Table.TableDragMode;
import com.trender.service.QuestionService;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

/**
 * Created by Egor.Veremeychik on 20.06.2016.
 */

@SuppressWarnings({"serial"})
@UIScope
@SpringView(name = QuestionView.VIEW_NAME)
public final class QuestionView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "questions";

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionDao questionDao;

    private final Table table;
    private Button createQuestion;
    private Component filter;
    private static final String[] DEFAULT_COLLAPSIBLE = {"id", "value"};

    public QuestionView() {
        setSizeFull();
        addStyleName("transactions");
        addComponent(buildToolbar());
        table = buildTable();
        addComponent(table);
        setExpandRatio(table, 1);
    }

    @Override
    public void detach() {
        super.detach();

    }

    private Component buildToolbar() {
        HorizontalLayout header = new HorizontalLayout();
        header.setWidth(100, Unit.PERCENTAGE);

        HorizontalLayout headerL = new HorizontalLayout();
        headerL.setSpacing(true);
        Responsive.makeResponsive(headerL);
        Label title = new Label("Questions editor");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        headerL.addComponent(title);

        createQuestion = buildCreateQuestion();
        filter = buildFilter();
        HorizontalLayout headerR = new HorizontalLayout(filter, createQuestion);
        headerR.setSpacing(true);
        headerR.setMargin(new MarginInfo(false, true, false, true));

        header.addComponent(headerL);
        header.addComponent(headerR);
        header.setComponentAlignment(headerR, Alignment.MIDDLE_RIGHT);

        return header;
    }

    private Button buildCreateQuestion() {
        final Button createQuestion = new Button("Create question");
        createQuestion.setDescription("Create a new question");
        /*createQuestion.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                createNewQuestion();
            }
        });*/
        return createQuestion;
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
                        return filterByProperty("value", item, event.getText());
                    }

                    @Override
                    public boolean appliesToProperty(final Object propertyId) {
                        if (propertyId.equals("value")) {
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
        filter.addShortcutListener(new ShortcutListener("Clear", KeyCode.ESCAPE, null) {
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

        //table.setColumnCollapsingAllowed(true);
        //table.setColumnReorderingAllowed(true);
        /*try {*/
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty("ID", Long.class, "");
        container.addContainerProperty("Value", String.class, "");
        Question question0 = new Question(1L, "asdfasdasgd");
        Question question1 = new Question(2L, "sdfgsdfgsdfgsd");
        Question question2 = new Question(3L, "hgfgvsbvbzxcvbzxcvz");
        List<Question> questions = new ArrayList<>();
        questions.add(question0);
        questions.add(question1);
        questions.add(question2);
        // List<Question> questions1 = questionService.getAll();
        for (Question question : questions) {
            Item item = container.addItem(question);
            item.getItemProperty("ID").setValue(question.getId());
            item.getItemProperty("Value").setValue(question.getValue());
        }
        table.setContainerDataSource(container);
        /*} catch (ServiceException e) {
            e.printStackTrace();
        }*/
        table.setSortContainerPropertyId("value");
        table.setSortAscending(false);

        table.setVisibleColumns("Value");
        table.setColumnHeaders("Value");

        // Allow dragging items to the reports menu
        table.setDragMode(TableDragMode.MULTIROW);
        table.setMultiSelect(true);

        table.addActionHandler(new TransactionsActionHandler());

        table.addValueChangeListener(
                new ValueChangeListener() {
                    @Override
                    public void valueChange(final ValueChangeEvent event) {
                        if (table.getValue() instanceof Set) {
                            Set<Object> val = (Set<Object>) table.getValue();
                            createQuestion.setEnabled(val.size() > 0);
                        }
                    }
                });
        table.setImmediate(true);

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

    private boolean filterByProperty(final String prop, final Item item, final String text) {
        if (item == null || item.getItemProperty(prop) == null || item.getItemProperty(prop).getValue() == null) {
            return false;
        }
        String val = item.getItemProperty(prop).getValue().toString().trim().toLowerCase();
        if (val.contains(text.toLowerCase().trim())) {
            return true;
        }
        return false;
    }

    void createNewReportFromSelection() {
        //TODO modal create question
        //UI.getCurrent().getNavigator().navigateTo(AdminViewType.QUESTION.getViewName());
        //TrenderEventBus.post(new TransactionReportEvent((Collection<Question>) table.getValue()));
    }

    @Override
    public void enter(final ViewChangeEvent event) {
    }

    private class TransactionsActionHandler implements Handler {
        private final Action report = new Action("Create Report");

        private final Action discard = new Action("Discard");

        private final Action details = new Action("Movie details");

        @Override
        public void handleAction(final Action action, final Object sender, final Object target) {
            if (action == report) {
                createNewReportFromSelection();
            } else if (action == discard) {
                Notification.show("Not implemented in this demo");
            } else if (action == details) {
                Item item = ((Table) sender).getItem(target);
                if (item != null) {
                    Long movieId = (Long) item.getItemProperty("movieId").getValue();
                    //MovieDetailsWindow.open(TrenderUI.getDataProvider().getMovie(movieId), null, null);
                }
            }
        }

        @Override
        public Action[] getActions(final Object target, final Object sender) {
            return new Action[]{details, report, discard};
        }
    }

    /*private class TempTransactionsContainer extends FilterableListContainer<Question> {
        private Collection<Question> questions;

        public TempTransactionsContainer(Collection<Question> questions) {
            this.questions = questions;
        }

        public void sort(final Object[] propertyId, final boolean[] ascending) {
            final boolean sortAscending = ascending[0];
            final Object sortContainerPropertyId = propertyId[0];
            try {
                Collections.sort(questionService.getAll(), new Comparator<Question>() {
                    @Override
                    public int compare(final Question o1, final Question o2) {
                        int result = 0;
                        if ("value".equals(sortContainerPropertyId)) {
                            result = o1.getValue().compareTo(o2.getValue());
                        }
                        if (!sortAscending) {
                            result *= -1;
                        }
                        return result;
                    }
                });
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }

    }*/

}
