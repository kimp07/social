package org.senlacourse.social.repository;

import org.senlacourse.social.domain.Wall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WallRepository extends JpaRepository<Wall, Long> {

    @Query(value = "select w from Wall w where w.society.id = :societyId")
    Optional<Wall> findBySocietyId(Long societyId);

    @Query(value = "select w from Wall w where w.root = true")
    Optional<Wall> findOneByRootIsTrue();
}