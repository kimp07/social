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

    @Query("select u from User u where lower(u.firstName) like :firstName and lower(u.surname) like :surname")
    Page<User> findAllByFirstNameAndSurname(String firstName, String surname, Pageable pageable);

    @Query("select u from User u join fetch u.role r where u.login = :userLogin")
    Optional<User> findOneByUserLogin(String userLogin);

    @Query("select u from User u join fetch u.role r where u.email = :email")
    Optional<User> findOneByEmail(String email);

    @Query("select u from User u inner join Friendship f1 on u.id = f1.user.id "
            + "inner join Friendship f2 on u.id = f2.friend.id where f1.friend.id = :userId and f2.user.id = :userId")
    Page<User> findAllFriendsByUserId(Long userId, Pageable pageable);
}
