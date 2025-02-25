package de.cimt.springbootvaadin.view;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import de.cimt.springbootvaadin.model.*;
import lombok.Getter;

import java.util.List;

public class BookAuthorForm extends FormLayout {

    ComboBox<Book> book = new ComboBox<>("Book");
    ComboBox<Author> author = new ComboBox<>("Author");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<BookAuthor> binder = new BeanValidationBinder<>(BookAuthor.class);

    public BookAuthorForm(List<Book> books, List<Author> authors) {
        addClassName("bookauthor-form");
        binder.bindInstanceFields(this);

        book.setItems(books);
        book.setItemLabelGenerator(Book::getTitle);
        author.setItems(authors);
        author.setItemLabelGenerator(Author::getFullName);
        add(book, author, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new BookAuthorForm.DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new BookAuthorForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new BookAuthorForm.SaveEvent(this, binder.getBean()));
        }
    }

    public void setBookAuthor(BookAuthor bookAuthor) {
        binder.setBean(bookAuthor);
    }

    @Getter
    public abstract static class BookAuthorFormEvent extends ComponentEvent<BookAuthorForm> {
        private BookAuthor bookAuthor;

        protected BookAuthorFormEvent(BookAuthorForm source, BookAuthor bookAuthor) {
            super(source, false);
            this.bookAuthor = bookAuthor;
        }

    }

    public static class SaveEvent extends BookAuthorForm.BookAuthorFormEvent {
        SaveEvent(BookAuthorForm source, BookAuthor bookAuthor) {
            super(source, bookAuthor);
        }
    }

    public static class DeleteEvent extends BookAuthorForm.BookAuthorFormEvent {
        DeleteEvent(BookAuthorForm source, BookAuthor bookAuthor) {
            super(source, bookAuthor);
        }
    }

    public static class CloseEvent extends BookAuthorForm.BookAuthorFormEvent {
        CloseEvent(BookAuthorForm source) {
            super(source, null);
        }
    }

    public void addDeleteListener(ComponentEventListener<BookAuthorForm.DeleteEvent> listener) {
        addListener(BookAuthorForm.DeleteEvent.class, listener);
    }

    public void addSaveListener(ComponentEventListener<BookAuthorForm.SaveEvent> listener) {
        addListener(BookAuthorForm.SaveEvent.class, listener);
    }

    public void addCloseListener(ComponentEventListener<BookAuthorForm.CloseEvent> listener) {
        addListener(BookAuthorForm.CloseEvent.class, listener);
    }
}
