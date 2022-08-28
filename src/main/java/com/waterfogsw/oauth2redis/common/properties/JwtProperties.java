package com.waterfogsw.oauth2redis.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

  private final String header;
  private final String issuer;
  private final String clientSecret;
  private final Long accessTokenExpirySeconds;
  private final Long refreshTokenExpirySeconds;

}
