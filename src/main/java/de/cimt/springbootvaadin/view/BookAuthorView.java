package de.cimt.springbootvaadin.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import de.cimt.springbootvaadin.config.AdminConstants;
import de.cimt.springbootvaadin.config.BookConstants;
import de.cimt.springbootvaadin.model.AuthorizationMatrix;
import de.cimt.springbootvaadin.model.BookAuthor;
import de.cimt.springbootvaadin.services.BookService;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;

@Slf4j
@SpringComponent
@Scope("prototype")
@RolesAllowed("ROLE_ADMIN")
@Route(value = BookConstants.BOOKAUTHOR_ROUTE, layout = MainLayout.class)
@PageTitle("Book Author Assignments | Books | Spring Boot Vaadin")
public class BookAuthorView extends VerticalLayout {

    Grid<BookAuthor> grid = new Grid<>(BookAuthor.class);
    BookAuthorForm form;
    BookService service;

    public BookAuthorView(BookService service) {
        this.service = service;
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
    }

    private void configureGrid() {
        grid.addClassName("bookauthor-grid");
        grid.setSizeFull();
        grid.setColumns("book.title", "author.firstName", "author.lastName");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editBookAuthor(event.getValue()));
    }

    private void configureForm() {
        form = new BookAuthorForm(service.findAllBooks(), service.findAllAuthors());
        form.setWidth("25em");
        form.addSaveListener(this::saveBookAuthor);
        form.addDeleteListener(this::deleteBookAuthor);
        form.addCloseListener(e -> closeEditor());
    }
    private void saveBookAuthor(BookAuthorForm.SaveEvent event) {
        BookAuthor bookAuthor = event.getBookAuthor();
        service.saveBookAuthor(bookAuthor);
        updateList();
        closeEditor();
    }

    private void deleteBookAuthor(BookAuthorForm.DeleteEvent event) {
        service.deleteBookAuthor(event.getBookAuthor());
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
        Button addBookAuthorButton = new Button("Add assignment", click -> addBookAuthor());
        HorizontalLayout toolbar = new HorizontalLayout(addBookAuthorButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editBookAuthor(BookAuthor bookAuthor) {
        if (bookAuthor == null) {
            closeEditor();
        } else {
            form.setBookAuthor(bookAuthor);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    public void closeEditor() {
        form.setBookAuthor(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addBookAuthor() {
        grid.asSingleSelect().clear();
        editBookAuthor(new BookAuthor());
    }

    private void updateList() {
        grid.setItems(service.findAllBookAuthors());
    }
}
