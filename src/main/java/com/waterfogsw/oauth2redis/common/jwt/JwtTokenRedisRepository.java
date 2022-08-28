package com.waterfogsw.oauth2redis.common.jwt;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtTokenRedisRepository extends CrudRepository<JwtToken, Long> {

  Optional<JwtToken> findByUserId(Long userId);

}
