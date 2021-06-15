package org.senlacourse.social.repository;

import org.senlacourse.social.domain.WallMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WallMessageRepository extends JpaRepository<WallMessage, Long> {

    @EntityGraph(attributePaths = {"user"})
    Page<WallMessage> findAllByWallId(Long wallId, Pageable pageable);

    @EntityGraph(attributePaths = {"wall", "user"})
    @Override
    Optional<WallMessage> findById(Long id);

    @Query(value = "delete from WallMessage wm where wm.wall.id = :wallId")
    void deleteAllByWallId(Long wallId);
}