package de.cimt.springbootvaadin.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(
        name="publishers",
        indexes = @Index(columnList = "name"),
        uniqueConstraints = @UniqueConstraint(columnNames = { "name" })
)
public class Publisher extends AbstractEntity {

    @NotEmpty
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "publisher", orphanRemoval = true)
    @Nullable
    List<Book> books = new LinkedList<>();
}
