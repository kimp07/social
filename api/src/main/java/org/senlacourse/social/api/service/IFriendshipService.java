package org.senlacourse.social.api.service;

import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.domain.Friendship;
import org.senlacourse.social.dto.FriendshipMemberDto;
import org.senlacourse.social.dto.UserIdDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IFriendshipService extends IService<Friendship> {

    void deleteById(Long id) throws ObjectNotFoundException;

    Page<FriendshipMemberDto> findAllFriendshipMembersByUserId(UserIdDto dto, Pageable pageable) throws ServiceException;

    boolean friendshipExistsByBothUserIds(Long[] userIds);
}
