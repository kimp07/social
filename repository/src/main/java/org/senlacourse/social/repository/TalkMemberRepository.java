package org.senlacourse.social.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import senlacourse.social.domain.TalkMember;

@Repository
public interface TalkMemberRepository extends JpaRepository<TalkMember, Long> {

    @Query(value = "select tm from TalkMember tm where tm.id = :talkMember")
    Page<TalkMember> findAllByTalkId(Long talkId, Pageable pageable);
}