package org.senlacourse.social.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import senlacourse.social.domain.Friendship;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    @Query(value = "select f from FriendshipMember fm left join Friendship f on fm.friendship.id = fm.id where fm.user.id = :userId")
    Page<Friendship> findAllFriendshipsByMemberId(Long userId, Pageable pageable);
}