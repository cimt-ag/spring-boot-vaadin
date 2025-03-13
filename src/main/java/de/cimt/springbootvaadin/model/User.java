package de.cimt.springbootvaadin.model;

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
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Getter
@Setter
@ToString
@Entity
@Table(
        name="users",
        indexes = @Index(columnList = "userName"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"userName"})
)
public class User extends AbstractEntity {

    private String firstName;

    private String lastName;

    @NotEmpty
    private String userName;

    private String email;

    private String password;

    private boolean enabled;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    @Nullable
    private List<AuthorizationMatrix> matrix = new LinkedList<>();

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
