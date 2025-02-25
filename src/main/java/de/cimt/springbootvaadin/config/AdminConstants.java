package de.cimt.springbootvaadin.config;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.RouterLink;
import de.cimt.springbootvaadin.view.*;

public class AdminConstants {

    public static final String ADMIN_TITLE = "Verwaltung";
    public static final String AUTHORIZATION = "Berechtigungen";
    public static final String AUTHORIZATION_ROUTE = "/admin/authorization";
    public static final String DATA = "Daten";
    public static final String METADATA_ROUTE = "admin/metadata";
    public static final String ROLE = "Rollen";
    public static final String ROLE_ROUTE = "/admin/role";
    public static final String USER = "Benutzer";
    public static final String USER_ROUTE = "/admin/user";
    public static final String DATA_IMPORT = "Data Import";

    private AdminConstants() {
        // private constructor to hide the implicit public one
    }

    public static SideNavItem getAdminNavigation() {
        SideNavItem administration = new SideNavItem(ADMIN_TITLE, USER_ROUTE, VaadinIcon.GROUP.create());
        administration.addItem(new SideNavItem(USER, USER_ROUTE, VaadinIcon.USERS.create()));
        administration.addItem(new SideNavItem(ROLE, RoleView.class));
        SideNavItem data = new SideNavItem(DATA, AUTHORIZATION_ROUTE, VaadinIcon.DATABASE.create());
        data.addItem(new SideNavItem(BookConstants.BOOKAUTHOR, BookAuthorView.class));
        data.addItem(new SideNavItem(AUTHORIZATION, AuthorizationView.class));
        data.addItem(new SideNavItem(DATA_IMPORT, ModuleDataView.class));
        administration.addItem(data);
        return administration;
    }
}
