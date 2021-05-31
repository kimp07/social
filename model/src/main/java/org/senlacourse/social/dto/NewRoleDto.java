package org.senlacourse.social.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class NewRoleDto {
    @NotNull
    private String roleName;
    private Boolean roleDisabled;
}
