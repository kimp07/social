package org.senlacourse.social.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.senlacourse.social.domain.TalkMessage;

@Repository
public interface TalkMessageRepository extends JpaRepository<TalkMessage, Long> {

    @Query(value = "select tm from TalkMessage tm where tm.talk.id = :talkId")
    Page<TalkMessage> findAllByTalkId(Long talkId, Pageable pageable);
}