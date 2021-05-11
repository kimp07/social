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
@Table(name = "wall_message_comments")
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class WallMessageComment extends AbstractMessageEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wall_message_id", referencedColumnName = "id")
    @ToString.Exclude
    private WallMessage wallMessage;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ToString.Exclude
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        WallMessageComment that = (WallMessageComment) o;

        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}