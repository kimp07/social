package org.senlacourse.social.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class NewWallMessageCommentDto {

    private Long wallMessageId;
    private Long userId;
    private String message;
}
