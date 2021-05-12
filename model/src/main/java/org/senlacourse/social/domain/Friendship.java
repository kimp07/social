package org.senlacourse.social.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "friendships")
@Getter
@Setter
@ToString(callSuper = true)
@Accessors(chain = true)
public class Friendship extends AbstractEntity {

    @OneToMany(mappedBy = "friendship", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<FriendshipMember> friendshipMembers;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Friendship that = (Friendship) o;

        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}