package senlacourse.social.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "talks")
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class Talk extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    @ToString.Exclude
    private User owner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Talk talk = (Talk) o;

        return id != null && id.equals(talk.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}