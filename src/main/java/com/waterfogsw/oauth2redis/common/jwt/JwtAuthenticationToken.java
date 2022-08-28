package com.waterfogsw.oauth2redis.common.jwt;

import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.waterfogsw.oauth2redis.user.entity.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

  private final Object principal;
  private final String credentials;

  @Builder(access = AccessLevel.PRIVATE)
  public JwtAuthenticationToken(
      Object principal,
      String credentials,
      Collection<? extends GrantedAuthority> authorities
  ) {
    super(authorities);
    super.setAuthenticated(true);

    this.principal = principal;
    this.credentials = credentials;
  }

  public static JwtAuthenticationToken of(
      User user,
      String token
  ) {
    JwtAuthentication jwtAuthentication = JwtAuthentication.of(token, user.getId());
    List<GrantedAuthority> authorities = List.of(user.getRole());

    return JwtAuthenticationToken
        .builder()
        .principal(jwtAuthentication)
        .credentials(null)
        .authorities(authorities)
        .build();
  }

}
