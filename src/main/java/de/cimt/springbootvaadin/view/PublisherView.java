package de.cimt.springbootvaadin.view;

import de.cimt.springbootvaadin.model.Publisher;
import de.cimt.springbootvaadin.services.BookService;
import lombok.Getter;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;

import de.cimt.springbootvaadin.config.BookConstants;
import jakarta.annotation.security.PermitAll;

@SpringComponent
@Getter
@Scope("prototype")
@PermitAll
@Route(value = BookConstants.PUBLISHER_ROUTE, layout = MainLayout.class)
@PageTitle("Publisher | Book | Spring Boot Vaadin")
public class PublisherView extends VerticalLayout {

    Grid<Publisher> grid = new Grid<>(Publisher.class);
    TextField filterText = new TextField();
    PublisherForm form;

    BookService service;

    public PublisherView(BookService service) {
        this.service = service;
        addClassName("publisher-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
    }

    private void configureGrid() {
        grid.addClassName("publisher-grid");
        grid.setSizeFull();
        grid.setColumns("name");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editPublisher(event.getValue()));
    }

    private void configureForm() {
        form = new PublisherForm();
        form.setWidth("25em");
        form.setVisible(false);
        form.addSaveListener(this::savePublisher);
        form.addDeleteListener(this::deletePublisher);
        form.addCloseListener(e -> closeEditor());
    }

    private void savePublisher(PublisherForm.SaveEvent event) {
        service.savePublisher(event.getPublisher());
        updateList();
        closeEditor();
    }

    private void deletePublisher(PublisherForm.DeleteEvent event) {
        service.deletePublisher(event.getPublisher());
        updateList();
        closeEditor();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addPublisherButton = new Button("Add publisher");
        addPublisherButton.addClickListener(click -> addPublisher());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addPublisherButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editPublisher(Publisher publisher) {
        if (publisher == null) {
            closeEditor();
        } else {
            form.setPublisher(publisher);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setPublisher(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addPublisher() {
        grid.asSingleSelect().clear();
        editPublisher(new Publisher());
    }

    public void updateList() {
        grid.setItems(service.findAllPublishers(filterText.getValue()));
    }
}
