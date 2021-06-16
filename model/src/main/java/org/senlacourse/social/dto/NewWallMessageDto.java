package org.senlacourse.social.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class NewWallMessageDto implements IAuthorizedUserDto {

    @NotNull
    private Long societyId;
    private Long userId;
    @NotNull
    @NotEmpty
    private String message;

    @Override
    public void setAuthorizedUserId(Long id) {
        this.userId = id;
    }

    @Override
    public Long getAuthorizedUserId() {
        return userId;
    }
}
