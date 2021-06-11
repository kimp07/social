package org.senlacourse.social.repository;

import org.senlacourse.social.domain.TalkMember;
import org.senlacourse.social.domain.TalkMemberId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TalkMemberRepository extends JpaRepository<TalkMember, TalkMemberId> {

    @EntityGraph(attributePaths = {"user"})
    Page<TalkMember> findAllByIdTalkId(Long talkId, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "path"})
    Optional<TalkMember> findOneByIdTalkIdAndIdUserId(Long talkId, Long userId);

    @EntityGraph(attributePaths = {"talk"})
    Page<TalkMember> findAllByIdUserId(Long userId, Pageable pageable);
}