package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.api.service.IFriendshipRequestService;
import org.senlacourse.social.api.service.IFriendshipService;
import org.senlacourse.social.api.service.IUserService;
import org.senlacourse.social.domain.Friendship;
import org.senlacourse.social.domain.FriendshipId;
import org.senlacourse.social.domain.FriendshipRequest;
import org.senlacourse.social.dto.FriendshipDto;
import org.senlacourse.social.dto.FriendshipRequestDto;
import org.senlacourse.social.dto.NewFriendshipRequestDto;
import org.senlacourse.social.dto.UserIdDto;
import org.senlacourse.social.mapstruct.FriendshipDtoMapper;
import org.senlacourse.social.mapstruct.FriendshipRequestDtoMapper;
import org.senlacourse.social.repository.FriendshipRepository;
import org.senlacourse.social.repository.FriendshipRequestRepository;
import org.senlacourse.social.security.service.AuthorizedUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Log4j
@RequiredArgsConstructor
public class FriendshipRequestService extends AbstractService<FriendshipRequest> implements IFriendshipRequestService {

    private final FriendshipRequestRepository friendshipRequestRepository;
    private final FriendshipRequestDtoMapper friendshipRequestDtoMapper;
    private final FriendshipDtoMapper friendshipDtoMapper;
    private final IUserService userService;
    private final FriendshipRepository friendshipRepository;
    private final IFriendshipService friendshipService;

    @Override
    public FriendshipRequest findEntityById(Long id) throws ObjectNotFoundException {
        FriendshipRequest friendshipRequest = friendshipRequestRepository.findById(id).orElse(null);
        return validateEntityNotNull(friendshipRequest);
    }

    @Override
    public FriendshipRequestDto findFriendshipRequestById(Long id) throws ObjectNotFoundException {
        return friendshipRequestDtoMapper
                .fromEntity(
                        findEntityById(id));
    }

    @AuthorizedUser
    @Override
    public Page<FriendshipRequestDto> findAllBySenderId(UserIdDto dto, Pageable pageable) throws ServiceException {
        return friendshipRequestDtoMapper.map(
                friendshipRequestRepository.findAllBySenderId(dto.getAuthorizedUserId(), pageable));
    }

    @AuthorizedUser
    @Override
    public Page<FriendshipRequestDto> findAllByRecipientId(UserIdDto dto, Pageable pageable)
            throws ServiceException {
        return friendshipRequestDtoMapper.map(
                friendshipRequestRepository
                        .findAllByRecipientId(
                                dto.getAuthorizedUserId(),
                                pageable));
    }

    private Optional<FriendshipRequest> findByBothUserIds(Long userId, Long friendId) {
        return friendshipRequestRepository.findOneByBothUserIds(userId, friendId);
    }

    private void friendshipOrFriendshipRequestNotExists(Long userId, Long friendId) throws ServiceException {
        if (friendshipService.friendshipExistsByBothUserIds(userId, friendId)) {
            ServiceException e = new ServiceException("Friendship for users exists");
            log.debug(e.getMessage(), e);
            throw e;
        }
        if (findByBothUserIds(userId, friendId).isPresent()) {
            ServiceException e = new ServiceException("FriendshipRequest for users exists");
            log.debug(e.getMessage(), e);
            throw e;
        }
    }

    @AuthorizedUser
    @Transactional
    @Override
    public FriendshipRequestDto saveNewFriendshipRequest(NewFriendshipRequestDto dto)
            throws ObjectNotFoundException, ServiceException {
        friendshipOrFriendshipRequestNotExists(dto.getRecipientId(), dto.getSenderId());
        FriendshipRequest friendshipRequest = new FriendshipRequest()
                .setSender(userService
                        .findEntityById(dto.getSenderId()))
                .setRecipient(userService
                        .findEntityById(dto.getRecipientId()))
                .setRequestDate(LocalDateTime.now());
        return friendshipRequestDtoMapper
                .fromEntity(friendshipRequestRepository
                        .save(friendshipRequest));
    }

    private void deleteById(Long id) throws ObjectNotFoundException {
        friendshipRequestRepository.deleteById(
                findEntityById(id)
                        .getId());
    }

    @AuthorizedUser
    @Transactional
    @Override
    public void decline(UserIdDto dto, Long requestId) throws ObjectNotFoundException, ServiceException {
        FriendshipRequest friendshipRequest = findEntityById(requestId);
        if (!friendshipRequest.getRecipient().getId().equals(dto.getAuthorizedUserId())
                && !friendshipRequest.getSender().getId().equals(dto.getAuthorizedUserId())) {
            ServiceException e = new ServiceException("User id=" + dto.getAuthorizedUserId() + "can`t accept request");
            log.error(e.getMessage(), e);
            throw e;
        }
        deleteById(requestId);
    }

    @AuthorizedUser
    @Transactional
    @Override
    public FriendshipDto confirmFriendshipRequestById(UserIdDto dto, Long requestId)
            throws ObjectNotFoundException, ServiceException {
        FriendshipRequest friendshipRequest = findEntityById(requestId);
        if (!friendshipRequest.getRecipient().getId().equals(dto.getAuthorizedUserId())) {
            ServiceException e = new ServiceException("User id=" + dto.getAuthorizedUserId() + "can`t decline request");
            log.error(e.getMessage(), e);
            throw e;
        }
        FriendshipDto friendshipDto = friendshipDtoMapper.fromEntity(
                createNewFriendshipFromFriendshipRequest(friendshipRequest));
        friendshipRequestRepository.deleteById(requestId);
        return friendshipDto;
    }

    private Friendship createNewFriendshipFromFriendshipRequest(FriendshipRequest friendshipRequest) throws ObjectNotFoundException {
        return friendshipRepository.save(new Friendship()
                .setId(new FriendshipId()
                        .setUser(
                                userService.findEntityById(friendshipRequest.getSender().getId()))
                        .setFriend(
                                userService.findEntityById(friendshipRequest.getRecipient().getId()))
                ));
    }
}