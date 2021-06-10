package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.api.service.IFriendshipService;
import org.senlacourse.social.domain.Friendship;
import org.senlacourse.social.dto.FriendshipMemberDto;
import org.senlacourse.social.dto.UserIdDto;
import org.senlacourse.social.mapstruct.FriendshipMemberDtoMapper;
import org.senlacourse.social.repository.FriendshipRepository;
import org.senlacourse.social.security.service.AuthorizedUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = {Throwable.class})
@Log4j
@RequiredArgsConstructor
public class FriendshipService implements IFriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final FriendshipMemberDtoMapper friendshipMemberDtoMapper;

    @Override
    public Friendship findEntityById(Long id) throws ObjectNotFoundException {
        return null;
    }

    @Override
    public void deleteById(Long id) throws ObjectNotFoundException {

    }

    @AuthorizedUser
    @Override
    public Page<FriendshipMemberDto> findAllFriendshipMembersByUserId(UserIdDto dto, Pageable pageable)
            throws ServiceException {
        return null;
    }

    @Override
    public boolean friendshipExistsByBothUserIds(Long[] userIds) {
        return true;
    }
}
