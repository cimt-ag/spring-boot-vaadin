package de.cimt.springbootvaadin.view;

import de.cimt.springbootvaadin.model.Book;
import de.cimt.springbootvaadin.services.BookService;
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
import lombok.Getter;

@SpringComponent
@Scope("prototype")
@PermitAll
@Route(value = BookConstants.BOOK_ROUTE, layout = MainLayout.class)
@PageTitle("Book | Spring Boot Vaadin")
public class BookView extends VerticalLayout {

    @Getter
    Grid<Book> grid = new Grid<>(Book.class);
    TextField filterText = new TextField();
    @Getter
    BookForm form;
    BookService service;

    public BookView(BookService service) {
        this.service = service;
        addClassName("book-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
    }

    private void configureGrid() {
        grid.addClassName("book-grid");
        grid.setSizeFull();
        grid.setColumns("title", "isbn", "publisher.name", "year");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editBook(event.getValue()));
    }

    private void configureForm() {
        form = new BookForm(service.findAllPublishers(null));
        form.setWidth("75em");
        form.setVisible(false);
        form.addSaveListener(this::saveBook);
        form.addDeleteListener(this::deleteBook);
        form.addCloseListener(e -> closeEditor());
    }

    private void saveBook(BookForm.SaveEvent event) {
        service.saveBook(event.getBook());
        updateList();
        closeEditor();
    }

    private void deleteBook(BookForm.DeleteEvent event) {
        service.deleteBook(event.getBook());
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
        filterText.setPlaceholder("Filter by title or isbn...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addBookButton = new Button("Add book");
        addBookButton.addClickListener(click -> addBook());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addBookButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editBook(Book book) {
        if (book == null) {
            closeEditor();
        } else {
            form.setBook(book);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setBook(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addBook() {
        grid.asSingleSelect().clear();
        editBook(new Book());
    }

    public void updateList() {
        grid.setItems(service.findAllBooks(filterText.getValue()));
    }
}
