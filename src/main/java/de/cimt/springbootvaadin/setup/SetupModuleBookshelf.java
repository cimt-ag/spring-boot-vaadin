package de.cimt.springbootvaadin.setup;

import de.cimt.springbootvaadin.config.BookConstants;
import de.cimt.springbootvaadin.model.Author;
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

@Slf4j
@Component
public class SetupModuleBookshelf implements ApplicationListener<ContextRefreshedEvent> {

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
        moduleService.setDataImported(BookConstants.BOOK);
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
