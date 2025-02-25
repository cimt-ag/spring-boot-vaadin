package de.cimt.springbootvaadin.setup;

import de.cimt.springbootvaadin.model.AuthorizationMatrix;
import de.cimt.springbootvaadin.model.Role;
import de.cimt.springbootvaadin.model.User;
import de.cimt.springbootvaadin.repository.AuthorizationMatrixRepository;
import de.cimt.springbootvaadin.repository.UserRepository;
import de.cimt.springbootvaadin.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Slf4j
@Component
public class SetupModuleAdmin implements ApplicationListener<ContextRefreshedEvent> {
    boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorizationMatrixRepository authorizationMatrixRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        // Create initial roles and users
        Role adminRole = userService.addRole("ROLE_ADMIN");
        Role userRole = userService.addRole("ROLE_USER");
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            User adminUser = initAdminUser();
            initMatrix(adminRole, adminUser);
            initMatrix(userRole, adminUser);
            User user = initUser();
            initMatrix(userRole, user);
        }
    }

    private void initMatrix(Role role, User user) {
        log.info("initMatrix: Role {} for User {}", role.getName(), user.getUserName());
        Collection<AuthorizationMatrix> configuredRoles = authorizationMatrixRepository.findByUser(user);
        if (configuredRoles.stream().anyMatch(matrix -> matrix.getRole().getId().equals(role.getId()))) {
            log.info("Role {} already defined", role.getName());
        } else {
            log.info("add Role {} to User {}", role.getName(), user.getUserName());
            final AuthorizationMatrix adminMatrix = new AuthorizationMatrix();
            adminMatrix.setUser(user);
            adminMatrix.setRole(role);
            authorizationMatrixRepository.save(adminMatrix);
        }
    }

    private User initAdminUser() {
        log.info("initAdminUser");
        User admin = userRepository.findByUserName("admin");
        if (admin == null) {
            log.info("User admin not found, will be created.");
            admin = new User();
            admin.setFirstName("Admin");
            admin.setLastName("Administrator");
            admin.setUserName("admin");
            admin.setEmail("admin@example.org");
            admin.setPassword(passwordEncoder.encode("admin"));
            userRepository.save(admin);
        }
        return admin;
    }

    private User initUser() {
        log.info("initUser");
        User user = userRepository.findByUserName("user");
        if (user == null) {
            log.info("User user not found, will be created.");
            user = new User();
            user.setFirstName("Unknown");
            user.setLastName("User");
            user.setUserName("user");
            user.setEmail("user@example.org");
            user.setPassword(passwordEncoder.encode("password"));
            userRepository.save(user);
        }
        return user;
    }
}
