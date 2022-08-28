package com.waterfogsw.oauth2redis.common.jwt;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import io.jsonwebtoken.lang.Assert;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@RedisHash("jwtToken")
public class JwtToken {

  @Id
  @Indexed
  private Long userId;
  private String token;
  @TimeToLive
  private Long expiredTime;

  @Builder(access = AccessLevel.PRIVATE)
  public JwtToken(
      Long userId,
      String token,
      Long expiredTime
  ) {
    Assert.notNull(userId, "UserId must be provided");
    Assert.hasText(token, "Token must be provided");
    Assert.notNull(expiredTime, "ExpiredTime must be provided");

    this.userId = userId;
    this.token = token;
    this.expiredTime = expiredTime;
  }

  public static JwtToken of(
      Long userId,
      String token,
      Long expiredTime
  ) {
    return JwtToken
        .builder()
        .userId(userId)
        .token(token)
        .expiredTime(expiredTime)
        .build();
  }

}
