package com.waterfogsw.oauth2redis.user.controller.dto;

import javax.validation.constraints.NotBlank;

public record UserSignupRequest(
    @NotBlank
    String username,
    @NotBlank
    String profileImgUrl
) {

}
