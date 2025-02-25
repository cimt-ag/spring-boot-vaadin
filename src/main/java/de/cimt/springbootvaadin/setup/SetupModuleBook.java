package de.cimt.springbootvaadin.setup;

import de.cimt.springbootvaadin.config.BookConstants;
import de.cimt.springbootvaadin.model.Author;
import de.cimt.springbootvaadin.model.Book;
import de.cimt.springbootvaadin.model.BookAuthor;
import de.cimt.springbootvaadin.model.Publisher;
import de.cimt.springbootvaadin.repository.AuthorRepository;
import de.cimt.springbootvaadin.repository.BookAuthorRepository;
import de.cimt.springbootvaadin.repository.BookRepository;
import de.cimt.springbootvaadin.repository.PublisherRepository;
import de.cimt.springbootvaadin.services.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Component
public class SetupModuleBook implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookAuthorRepository bookAuthorRepository;

    @Autowired
    private ModuleService moduleService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            log.info("SetupModuleBookshelf already executed, skipping");
            return;
        }
        if (!moduleService.importData(BookConstants.BOOK)) {
            log.info("Module Book should not setup data");
            return;
        }

        log.info("Set up Bookshelf data");
        Author douglasadams = createAuthorIfNotFound("Douglas", "Adams");
        Author erichgamma = createAuthorIfNotFound("Erich", "Gamma");
        Author richardhelm = createAuthorIfNotFound("Richard", "Helm");
        Author ralphjohnson = createAuthorIfNotFound("Ralph", "Johnson");
        Publisher addison = createPublisherIfNotFound("Addison Wesley");
        Book designpattern = createBookIfNotFound("Entwurfsmuster . Elemente wiederverwendbarer objektorientierter Software (Programmer's Choice)", "978-3827318626", 2001);
        designpattern.setPublisher(addison);
        List<BookAuthor> bookAuthorList = new LinkedList<>();
        BookAuthor erich_gamma_design = createAssignmentIfNotFound(erichgamma, designpattern);
        bookAuthorList.add(erich_gamma_design);
        BookAuthor richard_helm_design = createAssignmentIfNotFound(richardhelm, designpattern);
        bookAuthorList.add(richard_helm_design);
        BookAuthor ralph_johnson_design = createAssignmentIfNotFound(ralphjohnson, designpattern);
        bookAuthorList.add(ralph_johnson_design);
        designpattern.setBookAuthors(bookAuthorList);
        bookRepository.save(designpattern);
        moduleService.setDataImported(BookConstants.BOOK);
    }

    private BookAuthor createAssignmentIfNotFound(Author author, Book book) {
        log.info("createAssignmentIfNotFound {} {}", author, book);
        List<BookAuthor> bookAuthors = bookAuthorRepository.findByBook(book);
        for (BookAuthor bookAuthor: bookAuthors) {
            if (bookAuthor.getAuthor().equals(author)) {
                return bookAuthor;
            }
        }
        BookAuthor bookAuthor = new BookAuthor();
        bookAuthor.setBook(book);
        bookAuthor.setAuthor(author);
        bookAuthorRepository.save(bookAuthor);
        return bookAuthor;
    }

    private Book createBookIfNotFound(String title, String isbn, int year) {
        log.info("createBookIfNotFound {} {}", title, isbn);
        Book book = bookRepository.findByIsbn(isbn);
        if (book == null) {
            log.info("Book {} {} not found, will create it", title, isbn);
            book = new Book();
            book.setTitle(title);
            book.setIsbn(isbn);
            book.setYear(year);
            bookRepository.save(book);
        }
        return book;
    }

    private Author createAuthorIfNotFound(String firstName, String lastName) {
        log.info("createAuthorIfNotFound {} {}", firstName, lastName);
        Author author = authorRepository.findByFirstNameAndLastName(firstName, lastName);
        if (author == null) {
            log.info("Author {} {} not found, will create it", firstName, lastName);
            author = new Author();
            author.setFirstName(firstName);
            author.setLastName(lastName);
            authorRepository.save(author);
        }
        return author;
    }

    private Publisher createPublisherIfNotFound(String publisherName) {
        log.info("createPublisherIfNotFound {}", publisherName);
        Publisher publisher = publisherRepository.findByName(publisherName);
        if (publisher == null) {
            log.info("Publisher {} not found, will create it", publisherName);
            publisher = new Publisher();
            publisher.setName(publisherName);
            publisherRepository.save(publisher);
        }
        return publisher;
    }
}
