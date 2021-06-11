package org.senlacourse.social.api.service;

import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.dto.FriendshipDto;
import org.senlacourse.social.dto.UserIdDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IFriendshipService {

    void deleteByUserIds(UserIdDto dto, Long friendId) throws ObjectNotFoundException;

    Page<FriendshipDto> findAllByUserId(UserIdDto dto, Pageable pageable) throws ServiceException;

    boolean friendshipExistsByBothUserIds(Long userId, Long friendId);
}
