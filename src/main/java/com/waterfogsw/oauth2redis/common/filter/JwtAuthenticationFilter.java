package com.waterfogsw.oauth2redis.common.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.waterfogsw.oauth2redis.common.jwt.JwtTokenProvider;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {
    String path = request.getServletPath();

    if (path.startsWith("/api/auth/reissue")) {
      filterChain.doFilter(request, response);
      return;
    }

    String accessToken = jwtTokenProvider.resolveAccessToken(request);

    try {
      boolean isValidToken = jwtTokenProvider.validateToken(accessToken);
      if (StringUtils.hasText(accessToken) && isValidToken) {
        setAuthentication(accessToken);
      }
      filterChain.doFilter(request, response);
    } catch (ExpiredJwtException e) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied : Jwt Expired");
    }
  }

  private void setAuthentication(String token) {
    Authentication authentication = jwtTokenProvider.getAuthentication(token);
    SecurityContextHolder
        .getContext()
        .setAuthentication(authentication);
  }

}
