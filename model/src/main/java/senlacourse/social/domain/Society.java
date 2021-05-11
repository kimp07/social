package senlacourse.social.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "societies")
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class Society extends AbstractEntity {

    @Column(name = "title")
    private String title;
    @OneToOne(mappedBy = "society", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Wall wall;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    @ToString.Exclude
    private User owner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Society society = (Society) o;

        return id != null && id.equals(society.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}