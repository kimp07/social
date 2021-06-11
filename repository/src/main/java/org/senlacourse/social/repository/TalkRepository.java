package org.senlacourse.social.repository;

import org.senlacourse.social.domain.Talk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TalkRepository extends JpaRepository<Talk, Long> {

    @Query("select t from Talk t inner join TalkMember tm on t.id = tm.id.talk.id where tm.id.user.id = :userId")
    Page<Talk> findAllByUserId(@Param("userId") Long userId, Pageable pageable);
}