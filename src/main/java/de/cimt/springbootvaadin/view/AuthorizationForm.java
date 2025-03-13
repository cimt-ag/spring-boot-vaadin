package de.cimt.springbootvaadin.view;

import java.util.List;

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

import de.cimt.springbootvaadin.model.AuthorizationMatrix;
import de.cimt.springbootvaadin.model.Role;
import de.cimt.springbootvaadin.model.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthorizationForm extends FormLayout {

    ComboBox<User> user = new ComboBox<>("User");
    ComboBox<Role> role = new ComboBox<>("Role");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<AuthorizationMatrix> binder = new BeanValidationBinder<>(AuthorizationMatrix.class);

    public AuthorizationForm(List<User> users, List<Role> roles) {
        addClassName("authorizationmatrix-form");
        binder.bindInstanceFields(this);

        user.setItems(users);
        user.setItemLabelGenerator(User::getUserName);
        role.setItems(roles);
        role.setItemLabelGenerator(Role::getName);
        add(user, role, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    public void setAuthorizationMatrix(AuthorizationMatrix authorizationMatrix) {
        binder.setBean(authorizationMatrix);
    }

    public abstract static class AuthorizationFormEvent extends ComponentEvent<AuthorizationForm> {
        private AuthorizationMatrix authorizationMatrix;

        protected AuthorizationFormEvent(AuthorizationForm source, AuthorizationMatrix authorizationMatrix) {
            super(source, false);
            this.authorizationMatrix = authorizationMatrix;
        }

        public AuthorizationMatrix getAuthorizationMatrix() {
            return authorizationMatrix;
        }
    }

    public static class SaveEvent extends AuthorizationFormEvent {
        SaveEvent(AuthorizationForm source, AuthorizationMatrix authorizationMatrix) {
            super(source, authorizationMatrix);
        }
    }

    public static class DeleteEvent extends AuthorizationFormEvent {
        DeleteEvent(AuthorizationForm source, AuthorizationMatrix authorizationMatrix) {
            super(source, authorizationMatrix);
        }
    }

    public static class CloseEvent extends AuthorizationFormEvent {
        CloseEvent(AuthorizationForm source) {
            super(source, null);
        }
    }

    public void addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        addListener(DeleteEvent.class, listener);
    }

    public void addSaveListener(ComponentEventListener<SaveEvent> listener) {
        addListener(SaveEvent.class, listener);
    }

    public void addCloseListener(ComponentEventListener<CloseEvent> listener) {
        addListener(CloseEvent.class, listener);
    }
}
