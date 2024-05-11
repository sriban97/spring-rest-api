package com.green.springrestapi.repository;

import com.green.springrestapi.entity.Employee;
import com.green.springrestapi.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(name = "active user", value = "select * from app_user where active='Y'", nativeQuery = true)
    Optional<List<User>> getActiveUser();

    @Procedure("role_employee")
    List<Employee> plus1inout(@Param("role") String role);
}
