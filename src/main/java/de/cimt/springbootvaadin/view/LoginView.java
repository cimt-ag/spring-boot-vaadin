package de.cimt.springbootvaadin.view;

import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Route("login")
@PageTitle("Login | Spring Boot Vaadin")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm login = new LoginForm();

    public LoginView() {
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        login.setAction("login");

        add(new H1("Spring Boot Vaadin"));
        add(new Span("Username: user, Password: password"));
        add(new Span("Username: admin, Password: admin"));
        add(login);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        log.info("beforeEnter: {}", beforeEnterEvent.getLocation());
        log.info("beforeEnter: {}", SecurityContextHolder.getContext().getAuthentication());
        // inform the user about an authentication error
        if (beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
    }
}
