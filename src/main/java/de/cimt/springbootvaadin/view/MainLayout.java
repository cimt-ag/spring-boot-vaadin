package de.cimt.springbootvaadin.view;

import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

import de.thpeetz.kontor.admin.AdminConstants;
import de.thpeetz.kontor.admin.services.AdminService;
import de.thpeetz.kontor.security.SecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
public class MainLayout extends AppLayout {

    private final AdminService adminService;

    private final SecurityService securityService;

    public MainLayout(AdminService adminService, SecurityService securityService) {
        this.adminService = adminService;
        this.securityService = securityService;

        createHeader("Spring Boot Vaadin");
        createDrawer();
    }

    public void createDrawer() {
        H1 appTitle = new H1(new RouterLink("Kontor", MainView.class));
        appTitle.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("line-height", "var(--lumo-size-l)")
                .set("margin", "0 var(--lumo-space-m)");
        Scroller scroller = new Scroller(getPrimaryNavigation());
        scroller.setClassName(LumoUtility.Padding.SMALL);
        addToDrawer(appTitle, scroller);
    }

    public void createHeader(String titleName) {
        DrawerToggle toggle = new DrawerToggle();
        H2 viewTitle = new H2(titleName);
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        AvatarMenuBar avatar = new AvatarMenuBar(securityService, adminService);
        HorizontalLayout wrapper = new HorizontalLayout(toggle, viewTitle, avatar);
        wrapper.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        wrapper.expand(viewTitle);
        wrapper.setWidthFull();
        wrapper.setSpacing(false);

        VerticalLayout viewHeader = new VerticalLayout(wrapper);
        viewHeader.setPadding(false);
        viewHeader.setSpacing(false);

        addToNavbar(viewHeader);
        setPrimarySection(Section.DRAWER);
    }

    private SideNav getPrimaryNavigation() {
        SideNav sideNav = new SideNav();
        ArrayList<String> roles = new ArrayList<>();
        securityService.getAuthenticatedUser().ifPresent(user -> {
            log.info("Get roles for user {}", user.getUsername());
            user.getAuthorities().forEach(grantedAuthority -> {
                roles.add(grantedAuthority.getAuthority());
            });
            log.info("user {} has roles: {}", user, roles);
        });
        securityService.getAuthenticatedUser().ifPresent(user -> {
            log.info("User {} found", user.getUsername());
            boolean isAdmin = user.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> "ROLE_ADMIN".equals(grantedAuthority.getAuthority()));
            log.info("User {} hat Admin-Rechte: {}", user, isAdmin);
            if (isAdmin) {
                sideNav.addItem(AdminConstants.getAdminNavigation());
            }
        });
        return sideNav;
    }
}}

