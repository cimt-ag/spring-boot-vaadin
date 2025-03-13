package de.cimt.springbootvaadin.repository;

import de.cimt.springbootvaadin.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, String> {

    @Query("select a from Author a " +
            "where lower(a.firstName) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(a.lastName) like lower(concat('%', :searchTerm, '%'))")
    List<Author> search(@Param("searchTerm") String searchTerm);

    Author findByFirstNameAndLastName(String firstName, String lastName);
}
