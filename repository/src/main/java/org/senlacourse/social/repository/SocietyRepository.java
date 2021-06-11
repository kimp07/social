package org.senlacourse.social.repository;

import org.senlacourse.social.domain.Society;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocietyRepository extends JpaRepository<Society, Long> {

    @EntityGraph(attributePaths = {"wall"})
    @Override
    Optional<Society> findById(Long aLong);

    Page<Society> findAllByOwnerId(Long ownerId, Pageable pageable);

    Page<Society> findAllByTitleIsLike(String title, Pageable pageable);
}