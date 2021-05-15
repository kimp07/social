package org.senlacourse.social.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class TalkMessageDto {

    private Long id;
    private String messageDate;
    private UserSimpleDto user;
    private TalkDto talk;
    private String message;
}
