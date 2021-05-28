package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.api.service.IFriendshipRequestService;
import org.senlacourse.social.api.service.IUserService;
import org.senlacourse.social.domain.Friendship;
import org.senlacourse.social.domain.FriendshipMember;
import org.senlacourse.social.domain.FriendshipRequest;
import org.senlacourse.social.domain.User;
import org.senlacourse.social.dto.FriendshipRequestDto;
import org.senlacourse.social.dto.NewFriendshipRequestDto;
import org.senlacourse.social.dto.UserIdDto;
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

@Service
@Transactional(rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
@Log4j
@RequiredArgsConstructor
public class FriendshipRequestService extends AbstractService<FriendshipRequest> implements IFriendshipRequestService {

    private final FriendshipRequestRepository friendshipRequestRepository;
    private final FriendshipRequestDtoMapper friendshipRequestDtoMapper;
    private final IUserService userService;
    private final FriendshipRepository friendshipRepository;
    private final FriendshipMemberRepository friendshipMemberRepository;

    @Override
    public FriendshipRequest findEntityById(Long id) throws ObjectNotFoundException {
        FriendshipRequest friendshipRequest = friendshipRequestRepository.findById(id).orElse(null);
        return validateEntityNotNull(friendshipRequest);
    }

    @Override
    public FriendshipRequestDto findFriendshipRequestById(Long id) throws ObjectNotFoundException {
        FriendshipRequest friendshipRequest = findEntityById(id);
        return friendshipRequestDtoMapper.fromEntity(friendshipRequest);
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
                friendshipRequestRepository.findAllByRecipientId(dto.getAuthorizedUserId(), pageable));
    }

    @AuthorizedUser
    @Override
    public FriendshipRequestDto saveNewFriendshipRequest(NewFriendshipRequestDto dto)
            throws ObjectNotFoundException, ServiceException {
        User sender = userService.findEntityById(dto.getSenderId());
        User recipient = userService.findEntityById(dto.getRecipientId());
        FriendshipRequest friendshipRequest = new FriendshipRequest()
                .setSender(sender)
                .setRecipient(recipient)
                .setRequestDate(LocalDateTime.now());
        return friendshipRequestDtoMapper
                .fromEntity(
                        friendshipRequestRepository
                                .save(friendshipRequest));
    }

    @Override
    public void deleteById(Long id) throws ObjectNotFoundException {
        FriendshipRequest friendshipRequest = findEntityById(id);
        friendshipRequestRepository.deleteById(friendshipRequest.getId());
    }

    @Override
    public void confirmFriendshipRequestById(Long id) throws ObjectNotFoundException {
        FriendshipRequest friendshipRequest = findEntityById(id);
        User sender = friendshipRequest.getSender();
        User recipient = friendshipRequest.getRecipient();
        Friendship friendship = new Friendship();
        friendshipRepository.save(friendship);

        List<FriendshipMember> members = new ArrayList<>();
        members.add(new FriendshipMember().setFriendship(friendship).setUser(sender));
        members.add(new FriendshipMember().setFriendship(friendship).setUser(recipient));
        friendshipMemberRepository.saveAll(members);

        friendshipRequestRepository.deleteById(id);
    }
}