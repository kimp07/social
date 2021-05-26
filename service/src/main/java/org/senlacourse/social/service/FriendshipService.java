package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.api.service.IFriendshipService;
import org.senlacourse.social.domain.Friendship;
import org.senlacourse.social.dto.FriendshipMemberDto;
import org.senlacourse.social.mapstruct.FriendshipMemberDtoMapper;
import org.senlacourse.social.repository.FriendshipMemberRepository;
import org.senlacourse.social.repository.FriendshipRepository;
import org.senlacourse.social.security.service.AuthorizedUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
@Log4j
@RequiredArgsConstructor
public class FriendshipService extends AbstractService<Friendship> implements IFriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final FriendshipMemberRepository friendshipMemberRepository;
    private final FriendshipMemberDtoMapper friendshipMemberDtoMapper;

    @Override
    public Friendship findEntityById(Long id) throws ObjectNotFoundException {
        Friendship friendship = friendshipRepository.findById(id).orElse(null);
        return validateEntityNotNull(friendship);
    }

    @Override
    public void deleteById(Long id) throws ObjectNotFoundException {
        Friendship friendship = findEntityById(id);
        friendshipMemberRepository.deleteAllByFriendshipId(friendship.getId());
        friendshipRepository.deleteById(friendship.getId());
    }

    @AuthorizedUser
    @Override
    public Page<FriendshipMemberDto> findAllFriendshipMembersByUserId(Long userId, Pageable pageable)
            throws ServiceException {
        return friendshipMemberDtoMapper.map(
                friendshipMemberRepository.findAllFriendshipMembersByUserId(userId, pageable));
    }
}
