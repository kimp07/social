package org.senlacourse.social.repository;

import org.senlacourse.social.domain.WallMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WallMessageRepository extends JpaRepository<WallMessage, Long> {

    @Query(value = "select wm from WallMessage wm where wm.wall.id = :wallId")
    Page<WallMessage> findAllByWallId(Long wallId, Pageable pageable);

    @Query(value = "delete from WallMessage wm where wm.wall.id = :wallId")
    void deleteAllByWallId(Long wallId);
}