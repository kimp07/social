package senlacourse.social.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "roles")
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class Role extends AbstractEntity {

    @Column(name = "role_name")
    private String roleName;
    @Column(name = "role_disabled")
    private Boolean roleDisabled;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Role role = (Role) o;

        return id != null && id.equals(role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
