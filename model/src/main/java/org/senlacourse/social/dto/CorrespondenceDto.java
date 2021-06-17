package org.senlacourse.social.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class CorrespondenceDto {

    private CorrespondenceIdDto id;
    private Boolean unread;
}
