package com.waterfogsw.oauth2redis.common.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException e
  ) throws IOException, ServletException {
    Authentication authentication = getAuthentication();
    Object principal = authentication != null ? authentication.getPrincipal() : null;

    log.warn("{} is denied", principal, e);

    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.setContentType("text/plain;charset=UTF-8");
    response.getWriter().write("ACCESS DENIED");
    response.getWriter().flush();
    response.getWriter().close();
  }

  private Authentication getAuthentication() {
    return SecurityContextHolder
        .getContext()
        .getAuthentication();
  }


}
