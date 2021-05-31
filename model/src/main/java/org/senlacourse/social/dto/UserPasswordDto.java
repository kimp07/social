package org.senlacourse.social.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class UserPasswordDto implements IAuthorizedUserDto {

    private Long id;
    @NotNull
    @NotEmpty
    private String password;

    @Override
    public void setAuthorizedUserId(Long id) {
        this.id = id;
    }

    @Override
    public Long getAuthorizedUserId() {
        return id;
    }
}
