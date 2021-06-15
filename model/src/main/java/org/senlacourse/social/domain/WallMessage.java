package org.senlacourse.social.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "wall_messages")
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class WallMessage extends AbstractMessageEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wall_id", referencedColumnName = "id")
    @ToString.Exclude
    private Wall wall;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ToString.Exclude
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o.getClass() != getClass()) return false;
        WallMessage wallMessage = (WallMessage) o;
        return id != null && id.equals(wallMessage.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}