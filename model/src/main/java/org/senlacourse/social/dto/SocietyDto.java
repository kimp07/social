package org.senlacourse.social.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class SocietyDto {

    private Long id;
    private String title;
    private UserSimpleDto owner;
    private Boolean root;
}
