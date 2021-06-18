package org.senlacourse.social.repository;

import org.senlacourse.social.projection.UnreadTalkMessagesGroupByTalkIdDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CorrespondenceRepositoryCustom {

    Page<UnreadTalkMessagesGroupByTalkIdDto> findCountUnreadMessagesByUserIdGroupByTalkIdCriteria(Long userId,
                                                                                                  Pageable pageable);
}
