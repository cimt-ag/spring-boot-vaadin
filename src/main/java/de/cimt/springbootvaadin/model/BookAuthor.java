package de.cimt.springbootvaadin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name="book_authors",
        indexes = @Index(columnList = "author_id, book_id"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"author_id", "book_id"})
)
public class BookAuthor extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "author_id")
    @NotNull
    private Author author;

    @ManyToOne
    @JoinColumn(name = "book_id")
    @NotNull
    private Book book;
}
