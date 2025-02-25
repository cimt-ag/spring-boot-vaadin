package de.cimt.springbootvaadin.view;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;

import de.cimt.springbootvaadin.model.Role;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RoleForm extends FormLayout {

    TextField name = new TextField("Role name");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Role> binder = new BeanValidationBinder<>(Role.class);

    public RoleForm() {
        addClassName("role-form");
        binder.bindInstanceFields(this);
        add(name, createButtonsLayout());
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

    public void setRole(Role role) {
        binder.setBean(role);
    }

    public abstract static class RoleFormEvent extends ComponentEvent<RoleForm> {
        private Role role;

        protected RoleFormEvent(RoleForm source, Role role) {
            super(source, false);
            this.role = role;
        }

        public Role getRole() {
            return role;
        }
    }

    public static class SaveEvent extends RoleFormEvent {
        SaveEvent(RoleForm source, Role role) {
            super(source, role);
        }
    }

    public static class DeleteEvent extends RoleFormEvent {
        DeleteEvent(RoleForm source, Role role) {
            super(source, role);
        }
    }

    public static class CloseEvent extends RoleFormEvent {
        CloseEvent(RoleForm source) {
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
