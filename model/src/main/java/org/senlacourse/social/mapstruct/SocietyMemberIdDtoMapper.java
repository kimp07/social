package org.senlacourse.social.mapstruct;

import org.mapstruct.Mapper;
import org.senlacourse.social.domain.SocietyMemberId;
import org.senlacourse.social.dto.SocietyMemberIdDto;
import org.springframework.data.domain.Page;

@Mapper
public interface SocietyMemberIdDtoMapper {

    SocietyMemberIdDto fromEntity(SocietyMemberId entity);

    default Page<SocietyMemberIdDto> map(Page<SocietyMemberId> entities) {
        return entities.map(this::fromEntity);
    }
}
