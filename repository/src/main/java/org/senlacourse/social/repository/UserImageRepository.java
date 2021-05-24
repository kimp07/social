package org.senlacourse.social.repository;

import org.senlacourse.social.domain.UserImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserImageRepository extends JpaRepository<UserImage, Long> {

    @Query("select ui from UserImage ui where ui.user.id = :userId")
    Page<UserImage> findAllByUserId(Long userId, Pageable pageable);

    @Query("delete from UserImage ui where ui.user.id = :userId")
    void deleteAllByUserId(Long userId);
}
