package org.senlacourse.social.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class UpdateUserDto implements IAuthorizedUserDto {

    private Long id;
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
    @NotEmpty
    private String aboutMe;

    @Override
    public void setAuthorizedUserId(Long id) {
        this.id = id;
    }

    @Override
    public Long getAuthorizedUserId() {
        return id;
    }
}
