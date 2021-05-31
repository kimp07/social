package org.senlacourse.social.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user_images")
@Getter
@Setter
@ToString(callSuper = true)
@Accessors(chain = true)
public class UserImage extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private User user;
    @Column(name = "img_file_name")
    private String imgFileName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserImage userImage = (UserImage) o;

        return id != null && id.equals(userImage.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
