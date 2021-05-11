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
import javax.persistence.Table;
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
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ToString.Exclude
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TalkMessage that = (TalkMessage) o;

        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}