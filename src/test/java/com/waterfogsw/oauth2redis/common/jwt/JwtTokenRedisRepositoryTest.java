package com.waterfogsw.oauth2redis.common.jwt;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.jsonwebtoken.lang.Assert;

@SpringBootTest
class JwtTokenRedisRepositoryTest {

  @Autowired
  JwtTokenRedisRepository jwtTokenRedisRepository;

  @Test
  void testFindById() {
    //given
    JwtToken jwtToken = JwtToken.of(1L, "test", 1000L);

    //when
    jwtTokenRedisRepository.save(jwtToken);

    //then
    Optional<JwtToken> foundResult = jwtTokenRedisRepository.findByUserId(1L);
    Assert.isTrue(foundResult.isPresent());
  }

}
