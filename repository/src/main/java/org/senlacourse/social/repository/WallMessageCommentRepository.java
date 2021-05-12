package org.senlacourse.social.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.senlacourse.social.domain.WallMessageComment;

@Repository
public interface WallMessageCommentRepository extends JpaRepository<WallMessageComment, Long> {

    @Query(value = "select wmc from WallMessageComment wmc where wmc.wallMessage.id = :wallMessageId")
    Page<WallMessageComment> findAllByWallMessageId(Long wallMessageId, Pageable pageable);

    @Query(value = "delete from WallMessageComment wmc where wmc.wallMessage.id = :wallMessageId")
    void deleteAllByWallMessageId(Long wallMessageId);
}