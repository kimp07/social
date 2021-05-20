package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.api.security.IAuthorizedUserService;
import org.senlacourse.social.api.service.IFriendshipRequestService;
import org.senlacourse.social.domain.Friendship;
import org.senlacourse.social.domain.FriendshipMember;
import org.senlacourse.social.domain.FriendshipRequest;
import org.senlacourse.social.domain.User;
import org.senlacourse.social.dto.FriendshipRequestDto;
import org.senlacourse.social.dto.NewFriendshipRequestDto;
import org.senlacourse.social.mapstruct.FriendshipRequestDtoMapper;
import org.senlacourse.social.repository.FriendshipMemberRepository;
import org.senlacourse.social.repository.FriendshipRepository;
import org.senlacourse.social.repository.FriendshipRequestRepository;
import org.senlacourse.social.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j
@RequiredArgsConstructor
@Transactional
public class FriendshipRequestService extends AbstractService<FriendshipRequest> implements IFriendshipRequestService {

    private static final String USER_NOT_DEFINED_FOR_ID = "User not defined for id=";
    private static final String USER_NOT_DEFINED_FOR_LOGIN = "User not defined for login=";

    private final FriendshipRequestRepository friendshipRequestRepository;
    private final FriendshipRequestDtoMapper friendshipRequestDtoMapper;
    private final UserRepository userRepository;
    private final IAuthorizedUserService authorizedUserService;
    private final FriendshipRepository friendshipRepository;
    private final FriendshipMemberRepository friendshipMemberRepository;

    @Override
    protected FriendshipRequest findEntityById(Long id) throws ObjectNotFoundException {
        FriendshipRequest friendshipRequest = friendshipRequestRepository.findById(id).orElse(null);
        return validateEntityNotNull(friendshipRequest, USER_NOT_DEFINED_FOR_ID + id);
    }

    private User findUserById(Long id) throws ObjectNotFoundException {
        User user = userRepository.findById(id).orElse(null);
        return validateEntityNotNull(user, USER_NOT_DEFINED_FOR_ID + id);
    }

    @Override
    public Optional<FriendshipRequestDto> findFriendshipRequestById(Long id) throws ObjectNotFoundException {
        FriendshipRequest friendshipRequest = findEntityById(id);
        return Optional.ofNullable(friendshipRequestDtoMapper.fromEntity(friendshipRequest));
    }

    @Override
    public Page<FriendshipRequestDto> findAllBySenderId(Long senderId, Pageable pageable) throws ServiceException {
        authorizedUserService.injectAuthorizedUserId(senderId);
        return friendshipRequestDtoMapper.map(
                friendshipRequestRepository.findAllBySenderId(senderId, pageable));
    }

    @Override
    public Page<FriendshipRequestDto> findAllByRecipientId(Long recipientId, Pageable pageable)
            throws ServiceException {
        authorizedUserService.injectAuthorizedUserId(recipientId);
        return friendshipRequestDtoMapper.map(
                friendshipRequestRepository.findAllByRecipientId(recipientId, pageable));
    }

    @Override
    public FriendshipRequestDto saveNewFriendshipRequest(NewFriendshipRequestDto dto)
            throws ObjectNotFoundException, ServiceException {
        authorizedUserService.injectAuthorizedUserId(dto);
        User sender =  findUserById(dto.getSenderId());
        User recipient = findUserById(dto.getRecipientId());
        FriendshipRequest friendshipRequest = new FriendshipRequest()
                .setSender(sender)
                .setRecipient(recipient)
                .setRequestDate(LocalDateTime.now());
        return friendshipRequestDtoMapper.fromEntity(friendshipRequestRepository.save(friendshipRequest));
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
    }
}