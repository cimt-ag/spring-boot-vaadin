package de.cimt.springbootvaadin.model;

import java.util.LinkedList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * Entity class representing a book.
 * <p>
 * This class is mapped to the "books" table in the database and includes fields for the book's ID, title, bookAuthors and publisher.
 * Thw association between books and bookAuthors is modelled as m-n relation.
 * </p>
 *
 * @author Thomas Peetz
 */
@Entity
@Table(
        name = "books",
        indexes = @Index(columnList = "title, isbn"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"isbn"})
)
@Getter
@Setter
public class Book extends AbstractEntity {

    @NotEmpty
    private String title;

    @NotEmpty
    private String isbn;

    @Nullable
    private int year;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "book")
    @Nullable
    private List<BookAuthor> bookAuthors = new LinkedList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "publisher_id")
    @Nullable
    @JsonIgnoreProperties({ "books" })
    private Publisher publisher;
}
