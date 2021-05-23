package org.senlacourse.social.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.senlacourse.social.domain.AuthProvider;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class UserDto implements IAuthorizedUserDto {

    @NotNull
    private Long id;
    @NotNull
    @NotEmpty
    private String login;
    @JsonIgnore
    private String password;
    @NotNull
    private RoleDto role;
    @NotNull
    @NotEmpty
    @Email(regexp = "[A-Za-z0-9._%-+]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}")
    private String email;
    @NotNull
    private String firstName;
    @NotNull
    private String surname;
    @NotNull
    private String birthDate;
    private String aboutMe;
    @NotNull
    private Boolean enabled;
    @NotNull
    private Boolean nonLocked;
    @NotNull
    private Boolean nonExpired;
    @NotNull
    private Boolean credentialsNonExpired;
    private AuthProvider authProvider;
    private String authProviderId;

    @Override
    public void setAuthorizedUserId(Long id) {
        this.id = id;
    }

    @Override
    public Long getAuthorizedUserId() {
        return id;
    }
}
