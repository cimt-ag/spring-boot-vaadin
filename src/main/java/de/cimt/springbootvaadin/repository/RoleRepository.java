package de.cimt.springbootvaadin.repository;

import java.util.List;

import de.cimt.springbootvaadin.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends JpaRepository<Role, String> {
    
    @Query("select r from Role r " +
    "where lower(r.name) like lower(concat('%', :searchTerm, '%')) ")
    List<Role> search(@Param("searchTerm") String searchTerm);

    @Query("select r from Role r " +
    "where lower(r.name) like lower(:name) ")
    Role findByName(@Param("name") String name);
}
