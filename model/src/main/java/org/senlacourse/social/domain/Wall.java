package org.senlacourse.social.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "walls")
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class Wall extends AbstractEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_id", referencedColumnName = "id")
    @ToString.Exclude
    private Society society;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Wall wall = (Wall) o;

        return id != null && id.equals(wall.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}