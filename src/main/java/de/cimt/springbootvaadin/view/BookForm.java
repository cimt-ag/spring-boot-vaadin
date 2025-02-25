package de.cimt.springbootvaadin.view;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import de.cimt.springbootvaadin.model.Book;
import de.cimt.springbootvaadin.model.Publisher;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class BookForm extends FormLayout {

    TextField title = new TextField("Title");
    TextField isbn = new TextField("ISBN");
    IntegerField year = new IntegerField("Year");
    ComboBox<Publisher> publisher = new ComboBox<>("Publisher");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Book> binder = new BeanValidationBinder<>(Book.class);

    public BookForm(List<Publisher> publishers) {
        addClassName("book-form");
        binder.bindInstanceFields(this);

        add(title, 2);
        add(isbn, 2);
        add(year, 2);
        publisher.setItems(publishers);
        publisher.setItemLabelGenerator(Publisher::getName);
        add(publisher, 2);
        add(createButtonsLayout());
    }
    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new BookForm.DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new BookForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new BookForm.SaveEvent(this, binder.getBean()));
        }
    }

    public void setBook(Book book) {
        binder.setBean(book);
    }

    public abstract static class BookFormEvent extends ComponentEvent<BookForm> {
        private Book book;

        protected BookFormEvent(BookForm source, Book book) {
            super(source, false);
            this.book = book;
        }

        public Book getBook() {
            return book;
        }
    }

    public static class SaveEvent extends BookForm.BookFormEvent {
        SaveEvent(BookForm source, Book book) {
            super(source, book);
        }
    }

    public static class DeleteEvent extends BookForm.BookFormEvent {
        DeleteEvent(BookForm source, Book book) {
            super(source, book);
        }
    }

    public static class CloseEvent extends BookForm.BookFormEvent {
        CloseEvent(BookForm source) {
            super(source, null);
        }
    }

    public void addDeleteListener(ComponentEventListener<BookForm.DeleteEvent> listener) {
        addListener(BookForm.DeleteEvent.class, listener);
    }

    public void addSaveListener(ComponentEventListener<BookForm.SaveEvent> listener) {
        addListener(BookForm.SaveEvent.class, listener);
    }

    public void addCloseListener(ComponentEventListener<BookForm.CloseEvent> listener) {
        addListener(BookForm.CloseEvent.class, listener);
    }
}
