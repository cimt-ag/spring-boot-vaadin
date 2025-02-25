package de.cimt.springbootvaadin.services;

import de.cimt.springbootvaadin.model.AuthorizationMatrix;
import de.cimt.springbootvaadin.model.Role;
import de.cimt.springbootvaadin.model.User;
import de.cimt.springbootvaadin.repository.AuthorizationMatrixRepository;
import de.cimt.springbootvaadin.repository.RoleRepository;
import de.cimt.springbootvaadin.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service("userDetailsService")
public class UserService implements UserDetailsService {

    private static final SecureRandom random = new SecureRandom();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthorizationMatrixRepository authorizationMatrixRepository;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Collection<User> findAllUsers(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return userRepository.findAll();
        } else {
            return userRepository.search(stringFilter);
        }
    }

    public void saveUser(User user) {
        if (user == null) {
            log.warn("User is null. Can't save it.");
        }
        log.info("saveUser: user={}", user);
        userRepository.save(user);
    }

    public void saveUser(User user, List<Role> roles) {
        if (user == null) {
            log.warn("User is null. Can't save it.");
        }
        log.info("First save user: {}", user);
        user = userRepository.save(user);
        List<Role> copy = roles.stream().collect(Collectors.toList());
        List<AuthorizationMatrix> permissions = user.getMatrix();
        permissions.forEach(matrix -> {
            if (roles.contains(matrix.getRole())) {
                log.info("Role {} already assigned", matrix.getRole());
                copy.remove(matrix.getRole());
            } else {
                log.info("Role {} has to be removed", matrix.getRole());
                authorizationMatrixRepository.delete(matrix);
            }
        });
        log.info("remaining roles: {}", copy);
        for (Role role : copy) {
            AuthorizationMatrix matrix = new AuthorizationMatrix();
            matrix.setUser(user);
            matrix.setRole(role);
            authorizationMatrixRepository.save(matrix);
        }
    }

    public void deleteUser(User user) {
        if (user == null) {
            log.warn("User is null. Can't delete it.");
        }
        log.info("deleteUser: user={}", user);
        userRepository.delete(user);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        log.info("loadUserByUsername: userName={}", userName);
        User user = userRepository.findByUserName(userName);
        if (user == null) {
            log.info("User not found");
            return null;
        }

        Collection<? extends GrantedAuthority> authorities = getAuthorities(user);
        log.info("User {} hat Rolen: {}", userName, authorities);
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
                authorities);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return authorizationMatrixRepository.findByUser(user).stream()
                .map(matrix -> matrix.getRole().getName())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public String getRememberedUser(String id) {
        log.info("getRememberedUser: id={}", id);
        return "admin";
    }

    public String rememberUser(String username) {
        String randomId = new BigInteger(130, random).toString(32);
        log.info("rememberUser: username={}", username);
        return randomId;
    }

    public void removeRememberedUser(String id) {
        log.info("removeRememberedUser: id={}", id);
    }

    public String getUserFullName(String userName) {
        log.debug("get Fullname für user {}", userName);
        User user = userRepository.findByUserName(userName);
        if (user == null) {
            log.info("keinen Eintrag für {} gefunden", userName);
            return userName;
        } else {
            log.info("Voller Name des User {}: {}", userName, user.getFullName());
            return user.getFullName();
        }
    }

    public User getUser(String userName) {
        log.debug("get User {}", userName);
        return userRepository.findByUserName(userName);
    }

    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    public Collection<Role> findAllRoles(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return roleRepository.findAll();
        } else {
            return roleRepository.search(stringFilter);
        }
    }

    public Role addRole(String roleName) {
        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            log.info("Role {} was not found, will create it.", roleName);
            role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }
        return role;
    }

    public void saveRole(Role role) {
        if (role == null) {
            log.warn("Role is null. Can't save it.");
        }
        log.info("saveRole: role={}", role);
        roleRepository.save(role);
    }

    public void deleteRole(Role role) {
        if (role == null) {
            log.warn("Role is null. Can't delete it.");
        }
        log.info("deleteRole: role={}", role);
        roleRepository.delete(role);
    }

    public List<AuthorizationMatrix> findAllAuthorizationMatrices() {
        return authorizationMatrixRepository.findAll();
    }

    public void saveAuthorizationMatrix(AuthorizationMatrix authorizationMatrix) {
        if (authorizationMatrix == null) {
            log.warn("AuthorizationMatrix is null. Can't save it.");
        }
        log.info("saveAuthorizationMatrix: authorizationMatrix={}", authorizationMatrix);
        authorizationMatrixRepository.save(authorizationMatrix);
    }

    public void deleteAuthorizationMatrix(AuthorizationMatrix authorizationMatrix) {
        if (authorizationMatrix == null) {
            log.warn("AuthorizationMatrix is null. Can't delete it.");
        }
        log.info("deleteAuthorizationMatrix: authorizationMatrix={}", authorizationMatrix);
        authorizationMatrixRepository.delete(authorizationMatrix);
    }
}
