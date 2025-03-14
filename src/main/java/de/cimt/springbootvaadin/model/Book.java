package de.cimt.springbootvaadin.model;

import java.util.LinkedList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.imt.springbootvaadin.model.AbstractEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Entity class representing a book.
 * <p>
 * This class is mapped to the "books" table in the database and includes fields for the book's ID, title, authors and publisher.
 * Thw association between books and authors is modelled as m-n relation.
 * </p>
 *
 * @author Thomas Peetz
 */
@Entity
@Table(name = "books", indexes = @Index(columnList = "title, isbn"), uniqueConstraints = @UniqueConstraint(columnNames = {"isbn"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book extends AbstractEntity {

    @NotEmpty
    private String title;

    @NotEmpty
    private String isbn;

    @Nullable
    private int year;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "book")
    @Nullable
    private List<BookAuthor> authors = new LinkedList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "publisher_id")
    @NotNull
    @JsonIgnoreProperties({ "books" })
    private Publisher publisher;
i
    /**
     * Converts this {@link Book} entity to a {@link BookDto}.
     *
     * @return a {@link BookDto} with the same properties as this {@link Book} entity
     */
    public BookDto toDto() {
        return BookDto.builder()
            .id(id)
            .title(title)
            .authora(authora)
            .publisher(publisher
            .build();
    }
}

