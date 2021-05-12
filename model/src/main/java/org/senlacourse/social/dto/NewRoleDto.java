package org.senlacourse.social.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class NewRoleDto {
    @NotNull
    private String roleName;
}
