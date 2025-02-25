package de.cimt.springbootvaadin.view;

import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent
@Scope("prototype")
@AnonymousAllowed
@Route(value = "/", layout = MainLayout.class)
@PageTitle("Spring Boot Vaadin")
public class MainView extends VerticalLayout {

}
