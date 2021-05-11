package org.senlacourse.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import senlacourse.social.domain.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query(value = "select r from Role r where r.roleName = :roleName")
    Optional<Role> findByName(String roleName);
}
