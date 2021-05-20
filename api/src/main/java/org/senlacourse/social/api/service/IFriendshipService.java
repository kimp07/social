package org.senlacourse.social.api.service;

import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.dto.FriendshipMemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IFriendshipService {

    void deleteById(Long id) throws ObjectNotFoundException;

    Page<FriendshipMemberDto> findAllFriendshipMembersByUserId(Long userId, Pageable pageable) throws ServiceException;
}
