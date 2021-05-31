package org.senlacourse.social.mapstruct;

import org.mapstruct.Mapper;
import org.senlacourse.social.domain.SocietyMember;
import org.senlacourse.social.dto.SocietyMemberDto;
import org.springframework.data.domain.Page;

@Mapper
public interface SocietyMemberDtoMapper {

    SocietyMemberDto fromEntity(SocietyMember entity);

    default Page<SocietyMemberDto> map(Page<SocietyMember> entities) {
        return entities.map(this::fromEntity);
    }
}
