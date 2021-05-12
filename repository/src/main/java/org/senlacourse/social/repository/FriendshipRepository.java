package org.senlacourse.social.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.senlacourse.social.domain.Friendship;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    @Query(value = "select f from Friendship f left join FriendshipMember fm on f.id = fm.friendship.id where fm.user.id = :userId")
    Page<Friendship> findAllFriendshipsByMemberId(Long userId, Pageable pageable);
}