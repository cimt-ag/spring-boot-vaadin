package de.cimt.springbootvaadin.model;

import java.util.LinkedList;
import java.util.List;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(
        name= "authors",
        indexes = {
            @Index(columnList = "firstName, lastName"),
            @Index(columnList = "lastName, firstName")
        },
        uniqueConstraints = {
            @UniqueConstraint(columnNames = { "firstName", "lastName" })
        }
)
public class Author extends AbstractEntity {

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "author")
    @Nullable
    private List<BookAuthor> bookAuthors = new LinkedList<>();

    public String getFullName() {
        StringBuilder fullNamBuilder = new StringBuilder();
        if (firstName != null) {
            fullNamBuilder.append(firstName);
        }
        if (lastName != null) {
            if (fullNamBuilder.length() > 0) {
                fullNamBuilder.append(" ");
            }
            fullNamBuilder.append(lastName);
        }
        return fullNamBuilder.toString();
    }
}
