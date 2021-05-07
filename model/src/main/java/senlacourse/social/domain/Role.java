package senlacourse.social.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Role extends AbstractEntity {

    @Column(name = "role_name")
    private String roleName;
    @Column(name = "role_disabled")
    private Boolean roleDisabled;
}
