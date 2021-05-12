package org.senlacourse.social.dto;

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
public class NewUserDto {

    @NotNull
    private String login;
    private String password;
    private RoleDto role;
    @NotNull
    @NotEmpty
    @Email(regexp = "[A-Za-z0-9._%-+]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}")
    private String email;
    private String firstName;
    private String surname;
    private String birthDate;
    private String aboutMe;
    private AuthProvider authProvider;
    private String authProviderId;
}
