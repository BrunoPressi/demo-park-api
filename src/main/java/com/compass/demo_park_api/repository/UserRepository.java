package com.compass.demo_park_api.repository;

import com.compass.demo_park_api.entity.User;
import com.compass.demo_park_api.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("select role from User where username = :username")
    Role findRoleByUsername(String username);
}
