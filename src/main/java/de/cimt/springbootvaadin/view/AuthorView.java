package de.cimt.springbootvaadin.view;

import java.util.stream.Collectors;

import de.cimt.springbootvaadin.model.Author;
import de.cimt.springbootvaadin.model.BookAuthor;
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
@Route(value = BookConstants.AUTHOR_ROUTE, layout = MainLayout.class)
@PageTitle("Author | Book | Spring Boot Vaadin")
public class AuthorView extends VerticalLayout {

    Grid<Author> grid = new Grid<>(Author.class);
    TextField filterText = new TextField();
    AuthorForm form;
    BookService service;

    public AuthorView(BookService service) {
        this.service = service;
        addClassName("author-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
    }

    private void configureGrid() {
        grid.addClassName("author-grid");
        grid.setSizeFull();
        grid.setColumns("firstName", "lastName");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editAuthor(event.getValue()));
    }

    private void configureForm() {
        form = new AuthorForm();
        form.setWidth("75em");
        form.setVisible(false);
        form.addSaveListener(this::saveAuthor);
        form.addDeleteListener(this::deleteAuthor);
        form.addCloseListener(e -> closeEditor());
    }

    private void saveAuthor(AuthorForm.SaveEvent event) {
        service.saveAuthor(event.getAuthor());
        updateList();
        closeEditor();
    }

    private void deleteAuthor(AuthorForm.DeleteEvent event) {
        service.deleteAuthor(event.getAuthor());
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

        Button addAuthorButton = new Button("Add author");
        addAuthorButton.addClickListener(click -> addAuthor());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addAuthorButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editAuthor(Author author) {
        if (author == null) {
            closeEditor();
        } else {
            form.setAuthor(author);
            form.setBooks(author.getBookAuthors().stream().map(BookAuthor::getBook).collect(Collectors.toList()));
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setAuthor(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addAuthor() {
        grid.asSingleSelect().clear();
        editAuthor(new Author());
    }

    public void updateList() {
        grid.setItems(service.findAllAuthors(filterText.getValue()));
    }
}
