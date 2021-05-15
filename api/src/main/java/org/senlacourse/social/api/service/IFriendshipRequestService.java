package org.senlacourse.social.api.service;

import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.dto.FriendshipRequestDto;
import org.senlacourse.social.dto.NewFriendshipRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IFriendshipRequestService {
    Optional<FriendshipRequestDto> findFriendshipRequestById(Long id) throws ObjectNotFoundException;

    Page<FriendshipRequestDto> findAllBySenderId(Long senderId, Pageable pageable);

    Page<FriendshipRequestDto> findAllByRecipientId(Long recipientId, Pageable pageable);

    FriendshipRequestDto saveNewFriendshipRequest(NewFriendshipRequestDto dto) throws ObjectNotFoundException;

    void deleteById(Long id) throws ObjectNotFoundException;

    void confirmFriendshipRequestById(Long id) throws ObjectNotFoundException;
}