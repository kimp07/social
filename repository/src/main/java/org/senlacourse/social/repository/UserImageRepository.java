package org.senlacourse.social.repository;

import org.senlacourse.social.domain.UserImage;
import org.senlacourse.social.domain.UserImageId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserImageRepository extends JpaRepository<UserImage, UserImageId> {

    @EntityGraph(attributePaths = {"image"})
    Page<UserImage> findAllByIdUserId(Long userId, Pageable pageable);

    @EntityGraph(attributePaths = {"id.user","id.image"})
    Optional<UserImage> findByIdUserIdAndIdImageId(Long userId, Long imageId);

    void deleteAllByIdUserId(Long userId);

    void deleteByIdUserIdAndIdImageId(Long userId, Long imageId);
}
