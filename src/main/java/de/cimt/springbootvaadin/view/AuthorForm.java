package de.cimt.springbootvaadin.view;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import de.cimt.springbootvaadin.model.Author;
import de.cimt.springbootvaadin.model.Book;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class AuthorForm extends FormLayout {

    TextField firstName = new TextField("First Name");
    TextField lastName = new TextField("Last Name");
    ListBox<Book> books = new ListBox<>();

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Author> binder = new BeanValidationBinder<>(Author.class);
    
    public AuthorForm() {
        addClassName("author-form");
        binder.bindInstanceFields(this);

        add (firstName, 2);
        add(lastName, 2);

        books.setHeight("100px");
        books.setItemLabelGenerator(Book::getTitle);
        books.setMinWidth("50em");
        add(books, 2);
        add(createButtonsLayout());
    }
    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new AuthorForm.DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new AuthorForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new AuthorForm.SaveEvent(this, binder.getBean()));
        }
    }
    
    public void setAuthor(Author author) {
        binder.setBean(author);
    }
    
    public void setBooks(List<Book> books) {
        log.info("setting Books: ", books);
        this.books.setItems(books);
    }

    public abstract static class AuthorFormEvent extends ComponentEvent<AuthorForm> {
        private Author author;

        protected AuthorFormEvent(AuthorForm source, Author author) {
            super(source, false);
            this.author = author;
        }

        public Author getAuthor() {
            return author;
        }
    }

    public static class SaveEvent extends AuthorForm.AuthorFormEvent {
        SaveEvent(AuthorForm source, Author author) {
            super(source, author);
        }
    }

    public static class DeleteEvent extends AuthorForm.AuthorFormEvent {
        DeleteEvent(AuthorForm source, Author author) {
            super(source, author);
        }
    }

    public static class CloseEvent extends AuthorForm.AuthorFormEvent {
        CloseEvent(AuthorForm source) {
            super(source, null);
        }
    }

    public void addDeleteListener(ComponentEventListener<AuthorForm.DeleteEvent> listener) {
        addListener(AuthorForm.DeleteEvent.class, listener);
    }

    public void addSaveListener(ComponentEventListener<AuthorForm.SaveEvent> listener) {
        addListener(AuthorForm.SaveEvent.class, listener);
    }

    public void addCloseListener(ComponentEventListener<AuthorForm.CloseEvent> listener) {
        addListener(AuthorForm.CloseEvent.class, listener);
    }
}
