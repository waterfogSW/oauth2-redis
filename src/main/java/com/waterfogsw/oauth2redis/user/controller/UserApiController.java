package com.waterfogsw.oauth2redis.user.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.waterfogsw.oauth2redis.user.controller.dto.UserSignupRequest;
import com.waterfogsw.oauth2redis.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserApiController {

  private final UserService userService;

  @PostMapping("signup")
  @ResponseStatus(HttpStatus.CREATED)
  public void signup(
      @Valid @RequestBody
      UserSignupRequest request
  ) {
    userService.signup(request.username(), request.profileImgUrl());
  }

}
