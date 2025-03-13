package de.cimt.springbootvaadin.repository;

import java.util.List;

import de.cimt.springbootvaadin.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PublisherRepository extends JpaRepository<Publisher, String> {

    @Query("select p from Publisher p " +
            "where lower(p.name) like lower(concat('%', :searchTerm, '%')) ")
    List<Publisher> search(@Param("searchTerm") String searchTerm);

    Publisher findByName(String name);

    List<Publisher> findByNameIgnoreCase(String name);
}
