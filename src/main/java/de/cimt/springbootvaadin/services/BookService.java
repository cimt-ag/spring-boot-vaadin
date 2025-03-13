package de.cimt.springbootvaadin.services;

import java.util.List;

import com.vaadin.flow.data.provider.DataProvider;
import de.cimt.springbootvaadin.model.Author;
import de.cimt.springbootvaadin.model.Book;
import de.cimt.springbootvaadin.model.BookAuthor;
import de.cimt.springbootvaadin.model.Publisher;
import de.cimt.springbootvaadin.repository.AuthorRepository;
import de.cimt.springbootvaadin.repository.BookAuthorRepository;
import de.cimt.springbootvaadin.repository.BookRepository;
import de.cimt.springbootvaadin.repository.PublisherRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BookService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final BookAuthorRepository bookAuthorRepository;
    private final PublisherRepository publisherRepository;

    public BookService(AuthorRepository authorRepository,
                       BookRepository bookRepository,
                       BookAuthorRepository bookAuthorRepository,
                       PublisherRepository publisherRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.bookAuthorRepository = bookAuthorRepository;
        this.publisherRepository = publisherRepository;
    }

    public List<Publisher> findAllPublishers(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return publisherRepository.findAll();
        } else {
            return publisherRepository.search(stringFilter);
        }
    }

    public Publisher findPublisherByName(String publisherName) {
        return publisherRepository.findByName(publisherName);
    }

    public void deletePublisher(Publisher publisher) {
        publisherRepository.delete(publisher);
    }

    public void savePublisher(Publisher publisher) {
        if (publisher == null) {
            log.warn("Publisher is null. Are you sure you have connected your form to the application?");
            return;
        }
        publisherRepository.save(publisher);
    }

    public List<Author> findAllAuthors() { return authorRepository.findAll(); }

    public List<Author> findAllAuthors(String filter) {
        if (filter == null || filter.isEmpty()) {
            return authorRepository.findAll();
        } else {
            return authorRepository.search(filter);
        }
    }

    public void saveAuthor(Author author) {
        if (author == null) {
            log.warn("Author is null. Are you sure you have connected your form to the application?");
            return;
        }
        authorRepository.save(author);
    }

    public void deleteAuthor(Author author) {
        authorRepository.delete(author);
    }

    public List<Book> findAllBooks() {return bookRepository.findAll(); }

    public List<Book> findAllBooks(String filter) {
        if (filter == null || filter.isEmpty()) {
            return bookRepository.findAll();
        } else {
            return bookRepository.search(filter);
        }
    }

    public void saveBook(Book book) {
        if (book == null) {
            log.warn("Book is null. Are you sure you have connected your form to the application?");
            return;
        }
        bookRepository.save(book);
    }

    public void deleteBook(Book book) {
        Publisher publisher = book.getPublisher();
        publisher.getBooks().remove(book);
        publisherRepository.save(publisher);
        bookRepository.delete(book);
    }

    public List<BookAuthor> findAllBookAuthors() {
        return bookAuthorRepository.findAll();
    }

    public void saveBookAuthor(BookAuthor bookAuthor) {
        if (bookAuthor == null) {
            log.warn("BookAuthor is null. Can't save it.");
            return;
        }
        bookAuthorRepository.save(bookAuthor);
    }

    public void deleteBookAuthor(BookAuthor bookAuthor) {
        if (bookAuthor == null) {
            log.warn("BookAuthor is null. Can't delete it.");
            return;
        }
        bookAuthorRepository.delete(bookAuthor);
    }
}
