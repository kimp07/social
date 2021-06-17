package org.senlacourse.social.repository;

import org.senlacourse.social.domain.TalkMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TalkMessageRepository extends JpaRepository<TalkMessage, Long>, TalkMessageRepositoryCustom {

    @EntityGraph(attributePaths = {"sender", "answeredMessage"})
    Page<TalkMessage> findAllByTalkId(Long talkId, Pageable pageable);
}