package senlacourse.social.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
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
}
