package org.senlacourse.social.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "talk_members")
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class TalkMember {

    @EmbeddedId
    private TalkMemberId id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o.getClass() != getClass()) return false;
        TalkMember talkMember = (TalkMember) o;
        return id != null && id.equals(talkMember.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}