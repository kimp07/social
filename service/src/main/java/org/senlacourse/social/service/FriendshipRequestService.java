package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.api.service.IFriendshipRequestService;
import org.senlacourse.social.api.service.IFriendshipService;
import org.senlacourse.social.api.service.IUserService;
import org.senlacourse.social.domain.Friendship;
import org.senlacourse.social.domain.FriendshipMember;
import org.senlacourse.social.domain.FriendshipRequest;
import org.senlacourse.social.dto.FriendshipDto;
import org.senlacourse.social.dto.FriendshipRequestDto;
import org.senlacourse.social.dto.NewFriendshipRequestDto;
import org.senlacourse.social.dto.UserIdDto;
import org.senlacourse.social.mapstruct.FriendshipDtoMapper;
import org.senlacourse.social.mapstruct.FriendshipRequestDtoMapper;
import org.senlacourse.social.repository.FriendshipMemberRepository;
import org.senlacourse.social.repository.FriendshipRepository;
import org.senlacourse.social.repository.FriendshipRequestRepository;
import org.senlacourse.social.security.service.AuthorizedUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
@Log4j
@RequiredArgsConstructor
public class FriendshipRequestService extends AbstractService<FriendshipRequest> implements IFriendshipRequestService {

    private final FriendshipRequestRepository friendshipRequestRepository;
    private final FriendshipRequestDtoMapper friendshipRequestDtoMapper;
    private final FriendshipDtoMapper friendshipDtoMapper;
    private final IUserService userService;
    private final FriendshipRepository friendshipRepository;
    private final IFriendshipService friendshipService;
    private final FriendshipMemberRepository friendshipMemberRepository;

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

    private Optional<FriendshipRequest> findByBothUserIds(Long[] userIds) {
        return friendshipRequestRepository.findByBothUserIds(userIds[0], userIds[1]);
    }

    private void friendshipOrFriendshipRequestNotExists(Long[] userIds) throws ServiceException {
        if (friendshipService.friendshipExistsByBothUserIds(userIds)) {
            ServiceException e = new ServiceException("Friendship for users exists");
            log.debug(e.getMessage(), e);
            throw e;
        }
        if (findByBothUserIds(userIds).isPresent()) {
            ServiceException e = new ServiceException("FriendshipRequest for users exists");
            log.debug(e.getMessage(), e);
            throw e;
        }
    }

    @AuthorizedUser
    @Override
    public FriendshipRequestDto saveNewFriendshipRequest(NewFriendshipRequestDto dto)
            throws ObjectNotFoundException, ServiceException {
        friendshipOrFriendshipRequestNotExists(new Long[]{dto.getRecipientId(), dto.getSenderId()});
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

    @Override
    public void deleteById(Long id) throws ObjectNotFoundException {
        friendshipRequestRepository.deleteById(
                findEntityById(id)
                        .getId());
    }

    @Override
    public FriendshipDto confirmFriendshipRequestById(Long id) throws ObjectNotFoundException {
        FriendshipRequest friendshipRequest = findEntityById(id);
        Friendship friendship = new Friendship();
        FriendshipDto friendshipDto = friendshipDtoMapper.fromEntity(friendshipRepository.save(friendship));

        List<FriendshipMember> members = new ArrayList<>();
        members.add(new FriendshipMember()
                .setFriendship(friendship)
                .setUser(friendshipRequest.getSender()));
        members.add(new FriendshipMember()
                .setFriendship(friendship)
                .setUser(friendshipRequest.getRecipient()));
        friendshipMemberRepository.saveAll(members);

        friendshipRequestRepository.deleteById(id);

        return friendshipDto;
    }
}