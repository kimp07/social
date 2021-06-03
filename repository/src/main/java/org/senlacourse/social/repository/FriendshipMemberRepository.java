package org.senlacourse.social.repository;

import org.senlacourse.social.domain.FriendshipMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipMemberRepository extends JpaRepository<FriendshipMember, Long> {

    @Query(value = "delete from FriendshipMember fm where fm.friendship.id = :friendshipId")
    void deleteAllByFriendshipId(Long friendshipId);

    @Query(value = "select fm from FriendshipMember fm where fm.friendship.id = :friendshipId")
    Page<FriendshipMember> findAllByFriendshipId(Long friendshipId, Pageable pageable);

    @Query(value = "select fm from FriendshipMember fm where fm.user.id <> :userId and fm.friendship in " +
            "(select f.friendship from FriendshipMember f where f.user.id = :userId)")
    Page<FriendshipMember> findAllByUserId(Long userId, Pageable pageable);
}