package com.waterfogsw.oauth2redis.common.jwt;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.waterfogsw.oauth2redis.common.properties.JwtProperties;
import com.waterfogsw.oauth2redis.user.entity.User;
import com.waterfogsw.oauth2redis.user.service.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

  private final JwtProperties jwtProperties;
  private final UserService userService;

  public JwtToken createAccessToken(User user) {
    Assert.notNull(user, "User must be provided");

    return createToken(user, jwtProperties.getAccessTokenExpirySeconds());
  }

  public JwtToken createRefreshToken(User user) {
    Assert.notNull(user, "User must be provided");

    return createToken(user, jwtProperties.getRefreshTokenExpirySeconds());
  }

  public Authentication getAuthentication(String token) {
    Assert.hasText(token, "Token must be provided");

    long userId = Long.parseLong(getUserId(token));
    User user = userService.findById(userId);
    return JwtAuthenticationToken.of(user, token);
  }

  private String getUserId(String token) {

    return Jwts
        .parser()
        .setSigningKey(jwtProperties.getClientSecret())
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  public String resolveAccessToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(jwtProperties.getHeader());
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(jwtProperties.getPrefix())) {
      return bearerToken.substring(7);
    }
    return null;
  }

  public boolean validateToken(String token) {
    try {
      Jwts
          .parser()
          .setSigningKey(jwtProperties.getClientSecret())
          .parseClaimsJws(token);
      return true;
    } catch (SecurityException | MalformedJwtException e) {
      log.info("Invalid JWT signature");
      return false;
    } catch (UnsupportedJwtException e) {
      log.info("Unsupported JWT token");
      return false;
    } catch (IllegalArgumentException e) {
      log.info("JWT token is invalid");
      return false;
    }
  }

  private JwtToken createToken(
      User user,
      Long expirySeconds
  ) {
    Assert.notNull(user, "User must be provided");
    Assert.notNull(expirySeconds, "ExpirySeconds must be provided");

    Claims claims = user.getClaims();
    Date now = new Date();
    Date expiration = new Date(now.getTime() + expirySeconds);

    String token = Jwts
        .builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(expiration)
        .signWith(SignatureAlgorithm.HS256, jwtProperties.getClientSecret())
        .compact();

    return JwtToken.of(user.getId(), token, expirySeconds);
  }

}
