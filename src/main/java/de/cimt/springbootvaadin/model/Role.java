package de.cimt.springbootvaadin.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;

@Slf4j
@Getter
@Setter
@ToString
@Entity
public class Role extends AbstractEntity {

    @NotEmpty
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "role")
    @Nullable
    private List<AuthorizationMatrix> matrix;
}
