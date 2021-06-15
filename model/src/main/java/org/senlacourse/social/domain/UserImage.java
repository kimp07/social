package org.senlacourse.social.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "user_images")
@Getter
@Setter
@Accessors(chain = true)
public class UserImage {

    @EmbeddedId
    private UserImageId id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserImage userImage = (UserImage) o;
        return getId().equals(userImage.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
