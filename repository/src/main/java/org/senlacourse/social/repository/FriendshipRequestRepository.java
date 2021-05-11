package org.senlacourse.social.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import senlacourse.social.domain.FriendshipRequest;

@Repository
public interface FriendshipRequestRepository extends JpaRepository<FriendshipRequest, Long> {

    @Query(value = "select fr from FriendshipRequest fr where fr.sender.id = :senderId")
    Page<FriendshipRequest> findAllBySenderId(Long senderId, Pageable pageable);

    @Query(value = "select fr from FriendshipRequest fr where fr.recipient.id = :senderId")
    Page<FriendshipRequest> findAllByRecipientId(Long recipientId, Pageable pageable);
}