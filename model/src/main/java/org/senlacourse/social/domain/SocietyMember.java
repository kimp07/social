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
@Table(name = "society_members")
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class SocietyMember {

    @EmbeddedId
    private SocietyMemberPk id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o.getClass() != getClass()) return false;
        SocietyMember societyMember = (SocietyMember) o;
        return id != null && id.equals(societyMember.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}