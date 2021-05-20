package org.senlacourse.social.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class NewSocietyDto implements IAuthorizedUserDto {

    private Long ownerId;
    private String title;

    @Override
    public void setAuthorizedUserId(Long id) {
        this.ownerId = id;
    }

    @Override
    public Long getAuthorizedUserId() {
        return ownerId;
    }
}
