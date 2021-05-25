package org.senlacourse.social.api.service;

import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.domain.FriendshipRequest;
import org.senlacourse.social.dto.FriendshipRequestDto;
import org.senlacourse.social.dto.NewFriendshipRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IFriendshipRequestService extends IService<FriendshipRequest> {

    FriendshipRequestDto findFriendshipRequestById(Long id) throws ObjectNotFoundException;

    Page<FriendshipRequestDto> findAllBySenderId(Long userId, Pageable pageable) throws ServiceException;

    Page<FriendshipRequestDto> findAllByRecipientId(Long userId, Pageable pageable) throws ServiceException;

    FriendshipRequestDto saveNewFriendshipRequest(NewFriendshipRequestDto dto)
            throws ObjectNotFoundException, ServiceException;

    void deleteById(Long id) throws ObjectNotFoundException;

    void confirmFriendshipRequestById(Long id) throws ObjectNotFoundException;
}
