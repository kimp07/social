package org.senlacourse.social.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "talk_messages")
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class TalkMessage extends AbstractEntity {

    @Column(name = "message_date")
    private LocalDateTime messageDate;
    @Column(name = "message")
    private String message;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "talk_id", referencedColumnName = "id")
    @ToString.Exclude
    private Talk talk;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answered_message_id", referencedColumnName = "id")
    @BatchSize(size = 1)
    @ToString.Exclude
    private TalkMessage answeredMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    @ToString.Exclude
    private User sender;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ToString.Exclude
    private User user;
    @Column(name = "unread")
    private Boolean unread;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o.getClass() != getClass()) return false;
        TalkMessage talkMessage = (TalkMessage) o;
        return id != null && id.equals(talkMessage.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}