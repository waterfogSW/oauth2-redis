package com.waterfogsw.oauth2redis.common.jwt;

import org.springframework.util.Assert;

import lombok.AccessLevel;
import lombok.Builder;

public class JwtAuthentication {

  private final String token;

  private final Long userId;

  @Builder(access = AccessLevel.PRIVATE)
  private JwtAuthentication(
      String token,
      Long userId
  ) {
    Assert.hasText(token, "token must be provided");
    Assert.isTrue(userId > 0, "UserId must be provided");

    this.token = token;
    this.userId = userId;
  }

  public static JwtAuthentication of(
      String token,
      Long userId
  ) {
    return JwtAuthentication
        .builder()
        .userId(userId)
        .token(token)
        .build();
  }

}
