package org.senlacourse.social.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class WallMessageCommentDto {

    private Long id;
    private UserSimpleDto user;
    private WallMessageDto wallMessage;
    private String messageDate;
    private String message;
    private Integer likesCount;
    private Integer dislikesCount;
}
