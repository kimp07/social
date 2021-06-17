package org.senlacourse.social.repository;

import org.senlacourse.social.domain.TalkMessage;

import java.util.Optional;

public interface TalkMessageRepositoryCustom {

    Optional<TalkMessage> findByTalkMessageId(Long id);
}
