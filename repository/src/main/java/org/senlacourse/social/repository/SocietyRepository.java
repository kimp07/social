package org.senlacourse.social.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import senlacourse.social.domain.Society;

@Repository
public interface SocietyRepository extends JpaRepository<Society, Long> {

    @Query(value = "select s from Society s where s.owner.id = :ownerId")
    Page<Society> findAllByOwnerId(Long ownerId, Pageable pageable);

    @Query(value = "select s from Society s where s.title like :title")
    Page<Society> findAllByTitle(String title, Pageable pageable);

    @Query(value = "select s from Society s left join SocietyMember sm on s.id = sm.society.id where sm.user.id = :societyMemberId")
    Page<Society> findAllBySocietyMemberId(Long societyMemberId, Pageable pageable);
}