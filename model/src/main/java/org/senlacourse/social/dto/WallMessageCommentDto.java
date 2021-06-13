package org.senlacourse.social.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class WallMessageCommentDto {

    private Long id;
    private UserSimpleDto user;
    @JsonIgnore
    private WallMessageDto wallMessage;
    private WallMessageCommentDto answeredComment;
    private String messageDate;
    private String message;
    private Integer likesCount;
    private Integer dislikesCount;
}
