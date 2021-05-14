package org.senlacourse.social.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class FriendshipRequestDto {

    private Long id;
    private String requestDate;
    private UserSimpleDto sender;
    private UserSimpleDto recipient;
}
