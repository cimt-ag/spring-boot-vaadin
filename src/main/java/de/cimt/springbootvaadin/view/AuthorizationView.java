package de.cimt.springbootvaadin.view;

import de.cimt.springbootvaadin.model.AuthorizationMatrix;
import de.cimt.springbootvaadin.services.UserService;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

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
@Route(value = AdminConstants.AUTHORIZATION_ROUTE, layout = MainLayout.class)
@PageTitle("Authorization | Admin | Spring Boot Vaadin")
public class AuthorizationView extends VerticalLayout {

    Grid<AuthorizationMatrix> grid = new Grid<>(AuthorizationMatrix.class);
    AuthorizationForm form;
    UserService service;

    public AuthorizationView(UserService service) {
        this.service = service;
        addClassName("authoriaztionmatrix-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
    }

    private void configureGrid() {
        grid.addClassName("authorizationmatrix-grid");
        grid.setSizeFull();
        grid.setColumns("user.userName", "role.name");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editAuthorizationMatrix(event.getValue()));
    }

    private void configureForm() {
        form = new AuthorizationForm(service.findAllUsers(), service.findAllRoles());
        form.setWidth("25em");
        form.addSaveListener(this::saveAuthorizationMatrix);
        form.addDeleteListener(this::deleteAuthorizationMatrix);
        form.addCloseListener(e -> closeEditor());
    }

    private void saveAuthorizationMatrix(AuthorizationForm.SaveEvent event) {
        AuthorizationMatrix authorizationMatrix = event.getAuthorizationMatrix();
        service.saveAuthorizationMatrix(authorizationMatrix);
        updateList();
        closeEditor();
    }

    private void deleteAuthorizationMatrix(AuthorizationForm.DeleteEvent event) {
        service.deleteAuthorizationMatrix(event.getAuthorizationMatrix());
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
        Button addAuthorizationMaxtrixButton = new Button("Add permssion", click -> addAuthorizationMatrix());
        HorizontalLayout toolbar = new HorizontalLayout(addAuthorizationMaxtrixButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editAuthorizationMatrix(AuthorizationMatrix authorizationMatrix) {
        if (authorizationMatrix == null) {
            closeEditor();
        } else {
            form.setAuthorizationMatrix(authorizationMatrix);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    public void closeEditor() {
        form.setAuthorizationMatrix(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addAuthorizationMatrix() {
        grid.asSingleSelect().clear();
        editAuthorizationMatrix(new AuthorizationMatrix());
    }

    private void updateList() {
        grid.setItems(service.findAllAuthorizationMatrices());
    }
}
