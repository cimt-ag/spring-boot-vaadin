package de.cimt.springbootvaadin.repository;

import de.cimt.springbootvaadin.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, String> {

    @Query("select b from Book b " +
            "where lower(b.title) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(b.isbn) like lower(concat('%', :searchTerm, '%'))")
    List<Book> search(@Param("searchTerm") String searchTerm);

    List<Book> findByTitle(String name);

    List<Book> findByTitleIgnoreCase(String name);

    Book findByIsbn(String isbn);
}
