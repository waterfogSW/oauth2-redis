package com.waterfogsw.oauth2redis.user.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.waterfogsw.oauth2redis.common.jwt.JwtToken;
import com.waterfogsw.oauth2redis.common.jwt.JwtTokenProvider;
import com.waterfogsw.oauth2redis.common.jwt.JwtTokenRedisRepository;
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

  public void signup(
      String username,
      String profileImgUrl
  ) {
    Assert.hasText(username, "Username must be provided");
    Assert.hasText(profileImgUrl, "ProfileImgUrl must be provided");

    User newUser = User.createDefaultUser(username, profileImgUrl);
    userRepository.save(newUser);
  }

  @Transactional
  public void signin(
      String principal,
      HttpServletResponse response
  ) {
    Assert.hasText(principal, "Principal must be provided");
    Assert.notNull(response, "Response must be provided");

    long userId = Long.parseLong(principal);
    User user = userCrudService.findById(userId);

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

}
