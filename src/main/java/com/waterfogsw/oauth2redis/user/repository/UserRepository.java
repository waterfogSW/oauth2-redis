package com.waterfogsw.oauth2redis.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.waterfogsw.oauth2redis.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
