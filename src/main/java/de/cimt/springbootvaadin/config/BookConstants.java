package de.cimt.springbootvaadin.config;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.RouterLink;

import de.cimt.springbootvaadin.view.AuthorView;
import de.cimt.springbootvaadin.view.BookView;
import de.cimt.springbootvaadin.view.PublisherView;

public class BookConstants {

    public static final String AUTHOR = "Author";
    public static final String BOOK = "Book";
    public static final String BOOKAUTHOR = "Book Author Assignment";
    public static final String PUBLISHER = "Publisher";
    public static final String PUBLISHER_ROUTE = "book/publisher";
    public static final String AUTHOR_ROUTE = "book/author";
    public static final String BOOK_ROUTE = "book/book";
    public static final String BOOKAUTHOR_ROUTE = "book/bookauthor";

    public static RouterLink getPublisherLink() {
        return new RouterLink(PUBLISHER, PublisherView.class);
    }

    public static RouterLink getAuthorLink() {
        return new RouterLink(AUTHOR, AuthorView.class);
    }

    public static RouterLink getBookLink() {
        return new RouterLink(BOOK, BookView.class);
    }

    public static SideNavItem getBookNavigation() {
        SideNavItem bookshelf = new SideNavItem(BOOK, BookView.class, VaadinIcon.OPEN_BOOK.create());
        bookshelf.addItem(new SideNavItem(BOOK, BookView.class));
        bookshelf.addItem(new SideNavItem(PUBLISHER, PublisherView.class));
        bookshelf.addItem(new SideNavItem(AUTHOR, AuthorView.class));
        return bookshelf;
    }

    private BookConstants() {
        // private constructor to hide the implicit public one
    }
}
