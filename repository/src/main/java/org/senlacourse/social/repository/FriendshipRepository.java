package org.senlacourse.social.repository;

import org.senlacourse.social.domain.Friendship;
import org.senlacourse.social.domain.projection.IFriendshipMembersCountView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    @Query("select f from Friendship f left join FriendshipMember fm on f.id = fm.friendship.id where fm.user.id = :userId")
    Page<Friendship> findAllByMemberId(Long userId, Pageable pageable);

    @Query("select f.id as friendshipId, count(f) as membersCount from FriendshipMember fm "
            + "left join Friendship f on fm.friendship.id = f.id "
            + " where fm.user.id in :userIds group by f.id order by membersCount desc")
    Page<IFriendshipMembersCountView> findAllByUserIds(Long[] userIds, Pageable pageable);
}