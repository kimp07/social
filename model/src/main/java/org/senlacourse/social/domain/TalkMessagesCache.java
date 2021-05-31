package org.senlacourse.social.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "talk_messages_cache")
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class TalkMessagesCache extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", referencedColumnName = "id")
    @ToString.Exclude
    private User recipient;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "talk_message_id", referencedColumnName = "id")
    @ToString.Exclude
    private TalkMessage talkMessage;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TalkMessagesCache that = (TalkMessagesCache) o;

        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}