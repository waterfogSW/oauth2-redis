package com.waterfogsw.oauth2redis.user.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.waterfogsw.oauth2redis.user.entity.User;
import com.waterfogsw.oauth2redis.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  UserRepository userRepository;

  @InjectMocks
  UserService userService;

  @Nested
  @DisplayName("signup 메서드는")
  class DescribeSignUp {

    @Nested
    @DisplayName("username에 빈값이 전달되면")
    class ContextWithUsernameNullOrEmpty {

      @ParameterizedTest
      @NullAndEmptySource
      @DisplayName("IllegalArgumentException 에러를 발생시킨다")
      void ItThrowsIllegalArgumentException(String src) {
        //when, then
        assertThatThrownBy(() -> userService.signup(src, "test.url"))
            .isInstanceOf(IllegalArgumentException.class);
      }

    }

    @Nested
    @DisplayName("profileImgUrl에 빈값이 전달되면")
    class ContextWithProfileImgUrlNullOrEmpty {

      @ParameterizedTest
      @NullAndEmptySource
      @DisplayName("IllegalArgumentException 에러를 발생시킨다")
      void ItThrowsIllegalArgumentException(String src) {
        //when, then
        assertThatThrownBy(() -> userService.signup("testname", src))
            .isInstanceOf(IllegalArgumentException.class);
      }

    }

    @Nested
    @DisplayName("모든 값이 정상적으로 전달되면")
    class ContextWithValidParameterPassed {

      @Test
      @DisplayName("user를 생성하여 저장한다")
      void ItCreateUserAndSave() {
        //given
        String username = "test";
        String profileImgUrl = "testImageUrl";

        //when
        userService.signup(username, profileImgUrl);

        //then
        verify(userRepository).save(any(User.class));

      }

    }

  }

}
