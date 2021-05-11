package org.senlacourse.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import senlacourse.social.domain.FriendshipMember;

@Repository
public interface FriendshipMemberRepository extends JpaRepository<FriendshipMember, Long> {
}