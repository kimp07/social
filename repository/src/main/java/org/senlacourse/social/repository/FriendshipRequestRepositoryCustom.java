package org.senlacourse.social.repository;

import org.senlacourse.social.domain.FriendshipRequest;

import java.util.Optional;

@SuppressWarnings("NullableProblems")
public interface FriendshipRequestRepositoryCustom {

    Optional<FriendshipRequest> findOneByBothUserIds(Long senderId, Long recipientId);
}
