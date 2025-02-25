package de.cimt.springbootvaadin.view;

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
import de.cimt.springbootvaadin.model.Role;
import de.cimt.springbootvaadin.model.User;
import de.cimt.springbootvaadin.services.UserService;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@SpringComponent
@Scope("prototype")
@RolesAllowed("ROLE_ADMIN")
@Route(value = "admin/user", layout = MainLayout.class)
@PageTitle("User | Admin | Spring Boot Vaadin")
public class UserView extends VerticalLayout {

    Grid<User> grid = new Grid<>(User.class);
    TextField filterText = new TextField();
    UserForm form;
    UserService service;

    @Autowired
    PasswordEncoder passwordEncoder;

    public UserView(UserService service) {
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
        grid.setColumns("userName", "email", "firstName", "lastName", "enabled");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editUser(event.getValue()));
    }

    private void configureForm() {
        form = new UserForm();
        form.setWidth("25em");
        form.setVisible(false);
        form.addSaveListener(this::saveUser);
        form.addDeleteListener(this::deleteUser);
        form.addCloseListener(e -> closeEditor());
    }

    private void saveUser(UserForm.SaveEvent event) {
        User user = event.getUser();
        log.debug("UserView.saveUser: {}", user);
        List<Role> permissions = form.permissions.getSelectedItems().stream().collect(Collectors.toList());
        log.info("selected permissions: {}", permissions);
        if (form.hasPasswordChanged(user)) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            log.debug("password changed for user {}", user);
        }
        service.saveUser(user, permissions);
        updateList();
        closeEditor();
    }

    private void deleteUser(UserForm.DeleteEvent event) {
        service.deleteUser(event.getUser());
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

    public void editUser(User user) {
        if (user == null) {
            closeEditor();
        } else {
            form.setUser(user);
            form.setRoles(service.findAllRoles(), user);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    public void closeEditor() {
        form.setUser(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addUser() {
        grid.asSingleSelect().clear();
        editUser(new User());
    }

    private void updateList() {
        grid.setItems(service.findAllUsers(filterText.getValue()));
    }
}
