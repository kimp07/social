package senlacourse.social.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class User extends AbstractEntity {

    @Column(name = "user_login")
    private String login;
    @Column(name = "user_password")
    private String password;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
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
}