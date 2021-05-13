package org.senlacourse.social.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.senlacourse.social.domain.SocietyMember;

@Repository
public interface SocietyMemberRepository extends JpaRepository<SocietyMember, Long> {

    @Query(value = "select sm from SocietyMember sm where sm.society.id = :societyId")
    Page<SocietyMember> findAllBySocietyId(Long societyId, Pageable pageable);
}