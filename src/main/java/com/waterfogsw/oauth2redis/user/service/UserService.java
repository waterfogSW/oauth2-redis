package com.waterfogsw.oauth2redis.user.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.waterfogsw.oauth2redis.common.jwt.JwtToken;
import com.waterfogsw.oauth2redis.common.jwt.JwtTokenProvider;
import com.waterfogsw.oauth2redis.common.jwt.JwtTokenRedisRepository;
import com.waterfogsw.oauth2redis.user.entity.Provider;
import com.waterfogsw.oauth2redis.user.entity.User;
import com.waterfogsw.oauth2redis.user.repository.UserRepository;

import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final UserCrudService userCrudService;
  private final JwtTokenProvider jwtTokenProvider;
  private final JwtTokenRedisRepository jwtTokenRedisRepository;

  public User signup(
      OAuth2User oAuth2User,
      String provider
  ) {
    Assert.notNull(oAuth2User, "OAuth2User must be provided");
    Assert.hasText(provider, "Provider must be provided");

    String providerId = oAuth2User.getName();

    return userRepository
        .findByProviderAndProviderId(Provider.valueOf(provider), providerId)
        .orElseGet(() -> createUser(oAuth2User, Provider.valueOf(provider)));
  }

  public void signin(
      User user,
      HttpServletResponse response
  ) {
    Assert.notNull(user, "User must be provided");
    Assert.notNull(response, "Response must be provided");

    JwtToken accessToken = jwtTokenProvider.createAccessToken(user);
    JwtToken refreshToken = jwtTokenProvider.createRefreshToken(user);

    response.setHeader("AccessToken", accessToken.getToken());
    response.setHeader("RefreshToken", refreshToken.getToken());

    jwtTokenRedisRepository.save(refreshToken);
  }

  public void reissue(
      String principal,
      String refreshToken,
      HttpServletResponse response
  ) {
    Assert.hasText(principal, "Principal must be provided");
    Assert.hasText(refreshToken, "Refresh token must be provided");
    Assert.notNull(response, "Response must be provided");

    jwtTokenProvider.validateToken(refreshToken);

    long userId = Long.parseLong(principal);
    User user = userCrudService.findById(userId);

    JwtToken accessToken = jwtTokenProvider.createAccessToken(user);

    response.setHeader("AccessToken", accessToken.getToken());
  }

  private User createUser(
      OAuth2User oAuth2User,
      Provider provider
  ) {
    return userRepository.save(User.createDefaultUser(oAuth2User, provider));
  }

}
