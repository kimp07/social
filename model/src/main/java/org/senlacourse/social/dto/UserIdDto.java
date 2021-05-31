package org.senlacourse.social.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserIdDto implements IAuthorizedUserDto {

    private Long userId;

    @Override
    public void setAuthorizedUserId(Long id) {
        this.userId = id;
    }

    @Override
    public Long getAuthorizedUserId() {
        return userId;
    }
}
