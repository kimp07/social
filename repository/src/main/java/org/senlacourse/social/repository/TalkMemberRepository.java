package org.senlacourse.social.repository;

import org.senlacourse.social.domain.TalkMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TalkMemberRepository extends JpaRepository<TalkMember, Long> {

    @Query(value = "select tm from TalkMember tm where tm.talk.id = :talkId")
    Page<TalkMember> findAllByTalkId(Long talkId, Pageable pageable);

    @Query(value = "select tm from TalkMember tm where tm.talk.id = :talkId and tm.user.id = :userId")
    Optional<TalkMember> findOneByTalkIdAndUserId(Long talkId, Long userId);
}