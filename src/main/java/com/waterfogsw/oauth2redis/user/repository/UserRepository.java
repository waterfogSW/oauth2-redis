package com.waterfogsw.oauth2redis.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.waterfogsw.oauth2redis.user.entity.Provider;
import com.waterfogsw.oauth2redis.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByProviderAndProviderId(
      Provider provider,
      String providerId
  );

}
