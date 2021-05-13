package org.senlacourse.social.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class NewFriendshipRequestDto {

    @NotNull
    private Long senderId;
    @NotNull
    private Long recipientId;
}
