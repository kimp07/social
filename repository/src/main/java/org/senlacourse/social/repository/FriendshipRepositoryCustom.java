package org.senlacourse.social.repository;

import org.senlacourse.social.domain.Friendship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FriendshipRepositoryCustom {

    Page<Friendship> findAllByUserIdCriteria(Long userId, Pageable pageable);
}
