package org.senlacourse.social.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class NewUserImageDto implements IAuthorizedUserDto {

    private Long userId;
    private String imgFileName;

    @Override
    public void setAuthorizedUserId(Long id) {
        this.userId = id;
    }

    @Override
    public Long getAuthorizedUserId() {
        return userId;
    }
}
