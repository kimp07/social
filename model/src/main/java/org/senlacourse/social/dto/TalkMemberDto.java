package org.senlacourse.social.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class TalkMemberDto {

    private Long id;
    private TalkDto talk;
    private UserSimpleDto user;
}
