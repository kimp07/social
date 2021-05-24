package org.senlacourse.social.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class UserImageDto {

    private Long id;
    @JsonIgnore
    private UserSimpleDto user;
    private String imgFileName;
}
