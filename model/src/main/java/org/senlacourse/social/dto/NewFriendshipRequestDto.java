package org.senlacourse.social.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class NewFriendshipRequestDto implements IAuthorizedUserDto {

    private Long senderId;
    @NotNull
    private Long recipientId;

    @Override
    public void setAuthorizedUserId(Long id) {
        this.senderId = id;
    }

    @Override
    public Long getAuthorizedUserId() {
        return senderId;
    }
}
