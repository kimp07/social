package org.senlacourse.social.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.Objects;

@MappedSuperclass
@Getter
@Setter
@ToString
@Accessors(chain = true)
public abstract class AbstractMessageEntity extends AbstractEntity {

    @Column(name = "message_date")
    private LocalDateTime messageDate;
    @Column(name = "message")
    private String message;
    @Column(name = "likes_count")
    private Integer likesCount;
    @Column(name = "dislikes_count")
    private Integer dislikesCount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractMessageEntity that = (AbstractMessageEntity) o;
        return Objects.equals(messageDate, that.messageDate) && Objects.equals(message, that.message) && Objects.equals(likesCount, that.likesCount) && Objects.equals(dislikesCount, that.dislikesCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageDate, message, likesCount, dislikesCount);
    }
}
