package com.waterfogsw.oauth2redis.user.service;

import org.springframework.stereotype.Service;

import com.waterfogsw.oauth2redis.common.exception.NotFoundException;
import com.waterfogsw.oauth2redis.user.entity.User;
import com.waterfogsw.oauth2redis.user.repository.UserRepository;

import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public User findById(long id) {
    return userRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("User not found"));
  }

  public void signup(
      String username,
      String profileImgUrl
  ) {
    Assert.hasText(username, "Username must be provided");
    Assert.hasText(profileImgUrl, "ProfileImgUrl must be provided");

    User newUser = User.createDefaultUser(username, profileImgUrl);
    userRepository.save(newUser);
  }

}
