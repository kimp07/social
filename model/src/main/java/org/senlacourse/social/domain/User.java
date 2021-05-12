package org.senlacourse.social.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString(callSuper = true)
@Accessors(chain = true)
public class User extends AbstractEntity {

    @Column(name = "user_login")
    private String login;
    @Column(name = "user_password")
    private String password;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private Role role;
    @Column(name = "email")
    private String email;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "surname")
    private String surname;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    @Column(name = "about_me")
    private String aboutMe;
    @Column(name = "u_enabled")
    private Boolean enabled;
    @Column(name = "u_non_locked")
    private Boolean nonLocked;
    @Column(name = "u_non_expired")
    private Boolean nonExpired;
    @Column(name = "u_credentials_non_expired")
    private Boolean credentialsNonExpired;
    @Column(name = "auth_provider")
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;
    @Column(name = "auth_provider_id")
    private String authProviderId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;

        return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}