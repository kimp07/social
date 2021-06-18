package org.senlacourse.social.repository;

import org.senlacourse.social.domain.Friendship;
import org.senlacourse.social.domain.FriendshipId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, FriendshipId>, FriendshipRepositoryCustom {

    @Query("delete from Friendship f where (f.id.user.id = :userId and f.id.friend.id = :friendId) "
    + "or (f.id.friend.id = :userId and f.id.user.id = :friendId)")
    void deleteByUserIdAndFriendId(@Param("userId") Long userId, @Param("friendId") Long friendId);

    @EntityGraph(attributePaths = {"id.user", "id.friend"})
    @Query("select f from Friendship f where (f.id.user.id = :userId and f.id.friend.id = :friendId) "
            + "or (f.id.friend.id = :userId and f.id.user.id = :friendId)")
    Optional<Friendship> findByUserIdAndFriendId(@Param("userId") Long userId, @Param("friendId") Long friendId);

    @EntityGraph(attributePaths = {"id.user", "id.friend"})
    @Query("select f from Friendship f where f.id.user.id = :userId or f.id.friend.id = :userId")
    Page<Friendship> findAllByUserId(@Param("userId") Long userId, Pageable pageable);
}