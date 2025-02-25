package de.cimt.springbootvaadin.view;

import de.cimt.springbootvaadin.model.Role;
import de.cimt.springbootvaadin.services.UserService;
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

import de.cimt.springbootvaadin.config.AdminConstants;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringComponent
@Scope("prototype")
@RolesAllowed("ROLE_ADMIN")
@Route(value = AdminConstants.ROLE_ROUTE, layout = MainLayout.class)
@PageTitle("Rollen | Admin | Spring Boot Vaadin")
public class RoleView extends VerticalLayout {
    Grid<Role> grid = new Grid<>(Role.class);
    TextField filterText = new TextField();
    RoleForm form;
    UserService service;

    public RoleView(UserService service) {
        this.service = service;
        addClassName("user-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
    }

    private void configureGrid() {
        grid.addClassName("user-grid");
        grid.setSizeFull();
        grid.setColumns("name");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editRole(event.getValue()));
    }

    private void configureForm() {
        form = new RoleForm();
        form.setWidth("25em");
        form.addSaveListener(this::saveRole);
        form.addDeleteListener(this::deleteRole);
        form.addCloseListener(e -> closeEditor());
    }

    private void saveRole(RoleForm.SaveEvent event) {
        service.saveRole(event.getRole());
        updateList();
        closeEditor();
    }

    private void deleteRole(RoleForm.DeleteEvent event) {
        service.deleteRole(event.getRole());
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
        filterText.setPlaceholder("Filter by user name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        Button addUserButton = new Button("Add user", click -> addUser());
        HorizontalLayout toolbar = new HorizontalLayout(filterText, addUserButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editRole(Role role) {
        if (role == null) {
            closeEditor();
        } else {
            form.setRole(role);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    public void closeEditor() {
        form.setRole(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addUser() {
        grid.asSingleSelect().clear();
        editRole(new Role());
    }

    private void updateList() {
        grid.setItems(service.findAllRoles(filterText.getValue()));
    }
}
