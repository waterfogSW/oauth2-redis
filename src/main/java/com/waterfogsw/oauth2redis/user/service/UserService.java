package com.waterfogsw.oauth2redis.user.service;

import org.springframework.stereotype.Service;

import com.waterfogsw.oauth2redis.common.exception.NotFoundException;
import com.waterfogsw.oauth2redis.user.entity.User;
import com.waterfogsw.oauth2redis.user.repository.UserRepository;

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

}
