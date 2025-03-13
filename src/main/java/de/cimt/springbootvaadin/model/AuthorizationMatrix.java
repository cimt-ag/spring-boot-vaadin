package de.cimt.springbootvaadin.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class AuthorizationMatrix extends de.cimt.springbootvaadin.model.AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @NotNull
    private Role role;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("AuthorizationMatrix{");
        sb.append("user=").append(user.getUserName());
        sb.append(", role=").append(role.getName());
        sb.append('}');
        return sb.toString();
    }
}
