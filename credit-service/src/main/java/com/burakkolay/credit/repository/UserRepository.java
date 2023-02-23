package com.burakkolay.credit.repository;

import com.burakkolay.credit.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query(value = "SELECT * FROM USERS WHERE USER_NAME=(:username)",nativeQuery = true)
    User getUserByUserName(@Param("username") String userName);

    boolean existsByUserName(String userName);
}
