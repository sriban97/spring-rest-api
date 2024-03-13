package com.green.springrestapi.repository;

import com.green.springrestapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(name = "active user", value = "select * from app_user where active='Y'", nativeQuery = true)
    Optional<List<User>> getActiveUser();
}
