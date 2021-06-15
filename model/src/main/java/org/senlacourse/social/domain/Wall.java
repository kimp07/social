package org.senlacourse.social.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
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
    @Column(name = "root_wall")
    private Boolean root;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o.getClass() != getClass()) return false;
        Wall wall = (Wall) o;
        return id != null && id.equals(wall.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}