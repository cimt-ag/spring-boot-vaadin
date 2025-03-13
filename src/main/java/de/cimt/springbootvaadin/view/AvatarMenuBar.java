package de.cimt.springbootvaadin.view;

import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.router.Route;

import de.cimt.springbootvaadin.services.SecurityService;
import de.cimt.springbootvaadin.services.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Route("avatar-menu-bar")
public class AvatarMenuBar extends Div {

    private final SecurityService securityService;

    final UserService userService;

    public AvatarMenuBar(SecurityService securityService, UserService userService) {
        this.userService = userService;
        this.securityService = securityService;

        Avatar avatar = new Avatar();
        securityService.getAuthenticatedUser().ifPresent(user -> {
            log.info("AdminService: {}", userService);
            if (user  != null) {
                String userName = user.getUsername();
                String fullName = userService.getUserFullName(userName);
                avatar.setName(fullName);
            }    
        });
        // avatar.setImage(pictureUrl);
        MenuBar menuBar = new MenuBar();
        menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);
        MenuItem menuItem = menuBar.addItem(avatar);
        SubMenu subMenu = menuItem.getSubMenu();
        MenuItem logoutMenuItem = subMenu.addItem("Log out");
        logoutMenuItem.addClickListener(e -> securityService.logout());
        MenuItem profileItem = subMenu.addItem("Profile");
        profileItem.addClickListener(e -> profileItem.getUI().ifPresent(ui -> ui.navigate(UserProfileView.class)));
        subMenu.addItem("Settings");
        subMenu.addItem("Help");
        add(menuBar);
    }
}
