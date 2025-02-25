package de.cimt.springbootvaadin.repository;

import java.util.List;
import java.util.UUID;

import de.cimt.springbootvaadin.model.AuthorizationMatrix;
import de.cimt.springbootvaadin.model.Role;
import de.cimt.springbootvaadin.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorizationMatrixRepository extends JpaRepository<AuthorizationMatrix, String> {

    List<AuthorizationMatrix> findByUser(User user);

    List<AuthorizationMatrix> findByRole(Role role);
}
