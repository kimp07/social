package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.api.service.IFriendshipService;
import org.senlacourse.social.dto.FriendshipDto;
import org.senlacourse.social.dto.UserIdDto;
import org.senlacourse.social.mapstruct.FriendshipDtoMapper;
import org.senlacourse.social.repository.FriendshipRepository;
import org.senlacourse.social.security.service.AuthorizedUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j
@RequiredArgsConstructor
public class FriendshipService implements IFriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final FriendshipDtoMapper friendshipDtoMapper;

    @AuthorizedUser
    @Transactional
    public void deleteByUserIds(UserIdDto dto, Long friendId) throws ObjectNotFoundException {
        friendshipRepository.deleteByUserIdAndFriendId(dto.getAuthorizedUserId(), friendId);
    }

    @AuthorizedUser
    @Override
    public Page<FriendshipDto> findAllByUserId(UserIdDto dto, Pageable pageable)
            throws ServiceException {
        return friendshipDtoMapper.map(
                friendshipRepository
                        .findAllByUserIdCriteria(dto.getAuthorizedUserId(), pageable));
    }

    @Override
    public boolean friendshipExistsByBothUserIds(Long userId, Long friendId) {
        return friendshipRepository
                .findByUserIdAndFriendId(userId, friendId)
                .isPresent();
    }
}
