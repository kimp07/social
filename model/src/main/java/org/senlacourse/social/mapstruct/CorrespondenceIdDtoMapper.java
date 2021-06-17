package org.senlacourse.social.mapstruct;

import org.mapstruct.Mapper;
import org.senlacourse.social.domain.CorrespondenceId;
import org.senlacourse.social.dto.CorrespondenceIdDto;

@Mapper
public interface CorrespondenceIdDtoMapper {

    CorrespondenceIdDto fromEntity(CorrespondenceId entity);
}
