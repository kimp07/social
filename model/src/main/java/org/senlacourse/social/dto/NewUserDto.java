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

    @NotNull(message = "Login can't be null")
    @NotEmpty(message = "Login can't be empty")
    private String login;
    @NotNull(message = "Password can't be null")
    @NotEmpty(message = "Password can't be empty")
    private String password;
    private Long roleId;
    @NotNull
    @Email(regexp = "[A-Za-z0-9._%-+]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}")
    private String email;
    @NotNull
    @NotEmpty
    private String firstName;
    @NotNull
    @NotEmpty
    private String surname;
    @NotNull
    @NotEmpty
    private String birthDate;
    @NotNull
    private String aboutMe;
    private AuthProvider authProvider;
    private String authProviderId;
}
