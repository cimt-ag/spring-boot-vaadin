package de.cimt.springbootvaadin.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletResponse;
import com.vaadin.flow.spring.security.AuthenticationContext;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Component
@Service
public class SecurityService {

    private static final String LOGOUT_SUCCESS_URL = "/";

    private final AuthenticationContext authenticationContext;

    @Autowired
    private UserService userService;

    public SecurityService(AuthenticationContext authenticationContext) {
        this.authenticationContext = authenticationContext;
    }

    public Optional<UserDetails> getAuthenticatedUser() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("getAuthenticatedUser: {}", name);

        return Optional.ofNullable(userService.loadUserByUsername(name));
    }

    public void logout() {
        log.info("logout");
        authenticationContext.logout();
        clearCookies();

        UI.getCurrent().getPage().setLocation(LOGOUT_SUCCESS_URL);
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(VaadinServletRequest.getCurrent().getHttpServletRequest(), null, null);
        clearCookies();
    }

    public boolean isLoggedIn() {
        log.info("User {}", getAuthenticatedUser());
        return getAuthenticatedUser().isPresent();
    }

    private static final String JWT_HEADER_AND_PAYLOAD_COOKIE_NAME = "jwt.headerAndPayload";
    private static final String JWT_SIGNATURE_COOKIE_NAME = "jwt.signature";

    private void clearCookies() {
        log.info("clearCookies");
        clearCookie(JWT_HEADER_AND_PAYLOAD_COOKIE_NAME);
        clearCookie(JWT_SIGNATURE_COOKIE_NAME);
    }

    private void clearCookie(String cookieName) {
        log.info("clearCookie: {}", cookieName);
        HttpServletRequest request = VaadinServletRequest.getCurrent()
                .getHttpServletRequest();
        HttpServletResponse response = VaadinServletResponse.getCurrent()
                .getHttpServletResponse();

        Cookie k = new Cookie(cookieName, null);
        k.setPath(getRequestContextPath(request));
        k.setMaxAge(0);
        k.setSecure(request.isSecure());
        k.setHttpOnly(false);
        response.addCookie(k);
    }

    private String getRequestContextPath(HttpServletRequest request) {
        log.info("getRequestContextPath");
        final String contextPath = request.getContextPath();
        return "".equals(contextPath) ? "/" : contextPath;
    }
}
