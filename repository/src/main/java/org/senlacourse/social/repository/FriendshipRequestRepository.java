package org.senlacourse.social.repository;

import org.senlacourse.social.domain.FriendshipRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendshipRequestRepository extends JpaRepository<FriendshipRequest, Long> {

    @EntityGraph(attributePaths = {"sender", "recipient"})
    Page<FriendshipRequest> findAllBySenderId(Long senderId, Pageable pageable);

    @EntityGraph(attributePaths = {"sender", "recipient"})
    Page<FriendshipRequest> findAllByRecipientId(Long recipientId, Pageable pageable);

    @Query("select fr from FriendshipRequest fr where "
    + "(fr.sender.id = :senderId and fr.recipient.id = :recipientId) or "
    + "(fr.recipient.id = :senderId and fr.sender.id = :recipientId)")
    Optional<FriendshipRequest> findOneByBothUserIds(Long senderId, Long recipientId);
}