package de.cimt.springbootvaadin.view;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import de.cimt.springbootvaadin.model.ModuleData;

public class ModuleDataForm extends FormLayout {
    TextField moduleName = new TextField("Module Name");
    Checkbox importData = new Checkbox("Import Data");

    Button save = new com.vaadin.flow.component.button.Button("Save");
    Button delete = new com.vaadin.flow.component.button.Button("Delete");
    Button close = new Button("Cancel");

    Binder<ModuleData> binder = new BeanValidationBinder<>(ModuleData.class);

    public ModuleDataForm() {
        addClassName("moduleData-form");
        binder.bindInstanceFields(this);
        add(moduleName, importData, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new ModuleDataForm.DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new ModuleDataForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new ModuleDataForm.SaveEvent(this, binder.getBean()));
        }
    }

    public void setModuleData(ModuleData moduleData) {
        binder.setBean(moduleData);
    }

    public abstract static class ModuleDataFormEvent extends ComponentEvent<ModuleDataForm> {
        private ModuleData moduleData;

        protected ModuleDataFormEvent(ModuleDataForm source, ModuleData moduleData) {
            super(source, false);
            this.moduleData = moduleData;
        }

        public ModuleData getModuleData() {
            return moduleData;
        }
    }

    public static class SaveEvent extends ModuleDataForm.ModuleDataFormEvent {
        SaveEvent(ModuleDataForm source, ModuleData moduleData) {
            super(source, moduleData);
        }
    }

    public static class DeleteEvent extends ModuleDataForm.ModuleDataFormEvent {
        DeleteEvent(ModuleDataForm source, ModuleData moduleData) {
            super(source, moduleData);
        }
    }

    public static class CloseEvent extends ModuleDataForm.ModuleDataFormEvent {
        CloseEvent(ModuleDataForm source) {
            super(source, null);
        }
    }

    public void addDeleteListener(ComponentEventListener<ModuleDataForm.DeleteEvent> listener) {
        addListener(ModuleDataForm.DeleteEvent.class, listener);
    }

    public void addSaveListener(ComponentEventListener<ModuleDataForm.SaveEvent> listener) {
        addListener(ModuleDataForm.SaveEvent.class, listener);
    }

    public void addCloseListener(ComponentEventListener<ModuleDataForm.CloseEvent> listener) {
        addListener(ModuleDataForm.CloseEvent.class, listener);
    }
}
