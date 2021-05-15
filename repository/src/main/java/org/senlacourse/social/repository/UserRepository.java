package org.senlacourse.social.repository;

import org.senlacourse.social.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select u from User u where u.firstName like :firstName and u.surname like :surname")
    Page<User> findAllByFirstNameAndSurname(String firtsName, String surname, Pageable pageable);

    @Query(value = "select u from User u where u.login = :userLogin")
    Optional<User> findOneByUserLogin(String userLogin);

    @Query(value = "select u from User u where u.email = :email")
    Optional<User> findOneByEmail(String email);
}
