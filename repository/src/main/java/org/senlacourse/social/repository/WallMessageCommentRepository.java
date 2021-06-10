package org.senlacourse.social.repository;

import org.senlacourse.social.domain.WallMessageComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WallMessageCommentRepository extends JpaRepository<WallMessageComment, Long> {

    @EntityGraph(attributePaths = {"answeredComment", "user"})
    Page<WallMessageComment> findAllByWallMessageId(Long wallMessageId, Pageable pageable);

    @EntityGraph(attributePaths = {"answeredComment", "user", "wallMessage"})
    @Override
    Optional<WallMessageComment> findById(Long aLong);

    void deleteAllByWallMessageId(Long wallMessageId);

    @Query("delete from WallMessageComment w where w.wallMessage.wall.id = :wallId")
    void deleteAllByWallId(Long wallId);
}