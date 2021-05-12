package org.senlacourse.social.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class RoleDto {

    @NotNull
    private Long id;
    @NotNull
    private String roleName;
    @NotNull
    private Boolean roleDisabled;
}
