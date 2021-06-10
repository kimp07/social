package org.senlacourse.social.repository;

import org.senlacourse.social.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findAllByFirstNameIsLikeAndSurnameIsLike(String firstName, String surname, Pageable pageable);

    @EntityGraph(attributePaths = {"role"})
    Optional<User> findOneByLogin(String userLogin);

    @EntityGraph(attributePaths = {"role"})
    Optional<User> findOneByEmail(String email);
}
