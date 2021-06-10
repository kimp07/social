package org.senlacourse.social.repository;

import org.senlacourse.social.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
