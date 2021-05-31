package org.senlacourse.social.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class FriendshipMemberDto {

    private Long id;
    private UserSimpleDto user;
    private FriendshipDto friendship;
}
