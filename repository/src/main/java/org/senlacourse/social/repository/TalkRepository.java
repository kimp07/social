package org.senlacourse.social.repository;

import org.senlacourse.social.domain.Talk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TalkRepository extends JpaRepository<Talk, Long> {

    @Query(value = "select t from Talk t left join TalkMember tm on t.id = tm.user.id where tm.user.id in :talkMemberId")
    Page<Talk> findAllByTalkMemberId(Long[] talkMemberId, Pageable pageable);

    @Query(value = "select t from Talk t left join TalkMember tm on t.id = tm.user.id where tm.user.id in :talkMemberIds")
    Optional<Talk> findByTalkMemberIds(Long[] talkMemberIds);
}