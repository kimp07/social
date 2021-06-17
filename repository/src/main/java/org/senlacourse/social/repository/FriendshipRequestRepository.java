package org.senlacourse.social.repository;

import org.senlacourse.social.domain.FriendshipRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipRequestRepository extends JpaRepository<FriendshipRequest, Long>,
        FriendshipRequestRepositoryCustom {

    @EntityGraph(attributePaths = {"sender", "recipient"})
    Page<FriendshipRequest> findAllBySenderId(Long senderId, Pageable pageable);

    @EntityGraph(attributePaths = {"sender", "recipient"})
    Page<FriendshipRequest> findAllByRecipientId(Long recipientId, Pageable pageable);

}