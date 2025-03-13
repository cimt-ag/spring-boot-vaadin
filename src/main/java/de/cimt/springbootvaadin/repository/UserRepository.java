package de.cimt.springbootvaadin.repository;

import java.util.List;

import de.cimt.springbootvaadin.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, String> {

    @Query("select u from User u " +
            "where lower(u.lastName) like lower(concat('%', :searchTerm, '%')) ")
    List<User> search(@Param("searchTerm") String searchTerm);

    User findByUserName(String userName);
}
