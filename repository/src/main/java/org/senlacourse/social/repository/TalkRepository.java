package org.senlacourse.social.repository;

import org.senlacourse.social.domain.Talk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TalkRepository extends JpaRepository<Talk, Long> {
}