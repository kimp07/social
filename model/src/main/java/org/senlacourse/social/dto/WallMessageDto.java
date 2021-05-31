package org.senlacourse.social.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class WallMessageDto {

    private Long id;
    private WallDto wall;
    private UserSimpleDto user;
    private String messageDate;
    private String message;
    private Integer likesCount;
    private Integer dislikesCount;
}
