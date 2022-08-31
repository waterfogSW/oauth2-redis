package com.waterfogsw.oauth2redis.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.waterfogsw.oauth2redis.common.filter.JwtAuthenticationFilter;
import com.waterfogsw.oauth2redis.common.handler.CustomAccessDeniedHandler;
import com.waterfogsw.oauth2redis.common.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

  private final JwtTokenProvider jwtTokenProvider;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .and()
        /**
         * formLogin, csrf, headers, http-basic, rememberMe, logout filter 비활성화
         */
        .formLogin()
        .disable()
        .csrf()
        .disable()
        .headers()
        .disable()
        .httpBasic()
        .disable()
        .rememberMe()
        .disable()
        .logout()
        .disable()
        /**
         * 세션 비활성화
         */
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        /**
         * 예외처리
         */
        .exceptionHandling()
        .authenticationEntryPoint(new Http403ForbiddenEntryPoint())
        .accessDeniedHandler(customAccessDeniedHandler())
        .and()
        .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  private JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter(jwtTokenProvider);
  }

  private AccessDeniedHandler customAccessDeniedHandler() {
    return new CustomAccessDeniedHandler();
  }

}
