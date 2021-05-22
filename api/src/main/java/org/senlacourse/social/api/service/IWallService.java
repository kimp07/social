package org.senlacourse.social.api.service;

import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.dto.WallDto;

public interface IWallService {

    WallDto findById(Long id) throws ObjectNotFoundException;

    WallDto findWallBySocietyId(Long societyId) throws ObjectNotFoundException;

    WallDto findRootWall() throws ObjectNotFoundException;
}
