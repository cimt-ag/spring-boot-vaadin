package de.cimt.springbootvaadin.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(
    indexes = @Index(columnList = "moduleName"),
    uniqueConstraints = @UniqueConstraint(columnNames = {"moduleName"})
)
public class ModuleData extends AbstractEntity {

    @NotEmpty
    private String moduleName;

    private Boolean importData;
}
