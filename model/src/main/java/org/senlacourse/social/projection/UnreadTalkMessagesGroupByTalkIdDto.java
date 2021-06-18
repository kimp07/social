package org.senlacourse.social.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UnreadTalkMessagesGroupByTalkIdDto {

    private Long talkMessagesCount;
    private Long talkId;
}
