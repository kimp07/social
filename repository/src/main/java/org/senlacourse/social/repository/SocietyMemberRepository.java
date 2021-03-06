package org.senlacourse.social.repository;

import org.senlacourse.social.domain.SocietyMember;
import org.senlacourse.social.domain.SocietyMemberId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocietyMemberRepository extends JpaRepository<SocietyMember, SocietyMemberId> {

    @EntityGraph(attributePaths = {"id.society", "id.user"})
    Page<SocietyMember> findAllByIdSocietyId(Long societyId, Pageable pageable);

    @EntityGraph(attributePaths = {"id.society", "id.user"})
    Optional<SocietyMember> findOneByIdUserIdAndIdSocietyId(Long userId, Long societyId);

    @EntityGraph(attributePaths = {"id.society", "id.user"})
    Page<SocietyMember> findAllByIdUserId(Long id, Pageable pageable);

    void deleteAllByIdSocietyId(Long societyId);

    void deleteByIdUserIdAndIdSocietyId(Long userId, Long societyId);
}