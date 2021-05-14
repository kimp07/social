package org.senlacourse.social.api.service;

import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.dto.WallDto;

import java.util.Optional;

public interface IWallService {
    Optional<WallDto> findById(Long id) throws ObjectNotFoundException;

    Optional<WallDto> findWallBySocietyId(Long societyId) throws ObjectNotFoundException;

    Optional<WallDto> findRootWall() throws ObjectNotFoundException;
}
