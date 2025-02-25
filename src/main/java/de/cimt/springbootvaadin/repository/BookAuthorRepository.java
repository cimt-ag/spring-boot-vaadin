package de.cimt.springbootvaadin.repository;

import java.util.List;

import de.cimt.springbootvaadin.model.Author;
import de.cimt.springbootvaadin.model.Book;
import de.cimt.springbootvaadin.model.BookAuthor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookAuthorRepository extends JpaRepository<BookAuthor, String> {

    List<BookAuthor> findByAuthor(Author author);

    List<BookAuthor> findByBook(Book book);
}
