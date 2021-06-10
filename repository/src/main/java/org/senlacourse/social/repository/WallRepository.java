package org.senlacourse.social.repository;

import org.senlacourse.social.domain.Wall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WallRepository extends JpaRepository<Wall, Long> {

    Optional<Wall> findOneBySocietyId(Long societyId);

    Optional<Wall> findOneByRootIsTrue();
}