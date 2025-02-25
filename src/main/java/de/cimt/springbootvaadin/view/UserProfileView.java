package de.cimt.springbootvaadin.view;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.cimt.springbootvaadin.model.User;
import de.cimt.springbootvaadin.services.SecurityService;
import de.cimt.springbootvaadin.services.UserService;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Route(value="user/profile", layout = MainLayout.class)
@PermitAll
@PageTitle("Profile | User | Spring Boot Vaadin")
public class UserProfileView extends VerticalLayout {

    private SecurityService securityService;

    private UserService userService;

    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");

    Button save = new Button("Save");
    Button close = new Button("Cancel");
    Binder<User> binder = new BeanValidationBinder<>(User.class);

    public UserProfileView(UserService userService, SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
        binder.bindInstanceFields(this);

        add(firstName, lastName, createButtonsLayout());
        securityService.getAuthenticatedUser().ifPresent(user -> {
            log.info("UserProfileView: {}", user.getUsername());
            binder.setBean(userService.getUser(user.getUsername()));
        });

    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        close.addClickListener(event -> closeView());

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, close);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            log.info("update user profile");
        }
    }

    private void closeView() {
        log.info("close user profile view");
    }
}
