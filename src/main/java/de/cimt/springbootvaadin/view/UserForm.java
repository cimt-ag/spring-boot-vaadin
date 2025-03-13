package de.cimt.springbootvaadin.view;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import de.cimt.springbootvaadin.model.Role;
import de.cimt.springbootvaadin.model.User;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class UserForm extends FormLayout {

    TextField userName = new TextField("User name");
    PasswordField password = new PasswordField("Password");
    EmailField email = new EmailField("Email");
    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    Checkbox enabled = new Checkbox("Enabled");
    String originalPassword;

    CheckboxGroup<Role> permissions = new CheckboxGroup<>("Permissions");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<User> binder = new BeanValidationBinder<>(User.class);

    public UserForm() {
        addClassName("user-form");
        binder.bindInstanceFields(this);
        add(userName, password, email, firstName, lastName, enabled, configurePermissionsGroup(), createButtonsLayout());
    }

    private CheckboxGroup<Role> configurePermissionsGroup() {
        permissions.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        permissions.setItemLabelGenerator(Role::getName);
        permissions.addValueChangeListener(event -> {
            log.debug("permissions changed: {}", event);
        });
        return permissions;
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

    public void setUser(User user) {
        binder.setBean(user);
        //log.debug("UserForm.setUser: {}", user);
        if (user != null) {
            this.originalPassword = user.getPassword();
        } else {
            this.originalPassword = null;
        }
    }

    public void setRoles(List<Role> roles, User user) {
        permissions.setItems(roles);
        user.getMatrix().stream().forEach(authorizationMatrix -> {
            permissions.select(authorizationMatrix.getRole());
        });
    }

    public boolean hasPasswordChanged(User user) {
        return !originalPassword.equals(user.getPassword());
    }

    public abstract static class UserFormEvent extends ComponentEvent<UserForm> {
        private User user;

        protected UserFormEvent(UserForm source, User user) {
            super(source, false);
            this.user = user;
        }

        public User getUser() {
            return user;
        }
    }

    public static class SaveEvent extends UserFormEvent {
        SaveEvent(UserForm source, User user) {
            super(source, user);
        }
    }

    public static class DeleteEvent extends UserFormEvent {
        DeleteEvent(UserForm source, User user) {
            super(source, user);
        }
    }

    public static class CloseEvent extends UserFormEvent {
        CloseEvent(UserForm source) {
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
