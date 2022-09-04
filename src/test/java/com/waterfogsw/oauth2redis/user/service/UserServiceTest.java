package com.waterfogsw.oauth2redis.user.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;

import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;

import com.waterfogsw.oauth2redis.common.jwt.JwtToken;
import com.waterfogsw.oauth2redis.common.jwt.JwtTokenProvider;
import com.waterfogsw.oauth2redis.common.jwt.JwtTokenRedisRepository;
import com.waterfogsw.oauth2redis.user.entity.User;
import com.waterfogsw.oauth2redis.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  HttpServletResponse response;

  @Mock
  UserRepository userRepository;

  @Mock
  UserCrudService userCrudService;

  @Mock
  JwtTokenProvider jwtTokenProvider;

  @Mock
  JwtTokenRedisRepository jwtTokenRedisRepository;

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

  @Nested
  @DisplayName("signin 메서드는")
  class DescribeSignin {

    @Nested
    @DisplayName("principal이 빈값이면")
    class ContextWithBlankPrincipal {

      @ParameterizedTest
      @NullAndEmptySource
      @DisplayName("IllegalArgumentException 에러를 발생시킨다")
      void ItThrowsIllegalArgumentException(String src) {
        //when, then
        assertThatThrownBy(() -> userService.signin(src, new MockHttpServletResponse()))
            .isInstanceOf(IllegalArgumentException.class);
      }

    }

    @Nested
    @DisplayName("reponse가 null이 전달되면")
    class ContextWithNullResponse {

      @Test
      @DisplayName("IllegalArgumentExecption 에러를 발생시킨다")
      void ItThrowsIllegalArgumentException() {
        //when, then
        assertThatThrownBy(() -> userService.signin("test", null))
            .isInstanceOf(IllegalArgumentException.class);
      }

    }

    @Nested
    @DisplayName("유효한 값이 전달되면")
    class ContextWithValidParametersPassed {

      @Test
      @DisplayName("accessToken과 refreshToken을 헤더에 반환하고, refreshToken을 redis에 저장한다")
      void ItSetAccessTokenToHeaderAndSaveRefreshToken() {
        //given
        JwtToken mockToken = JwtToken.of(1L, "test", 1L);
        User mockUser = User.createDefaultUser("test", "test");

        given(jwtTokenProvider.createRefreshToken(any(User.class))).willReturn(mockToken);
        given(jwtTokenProvider.createAccessToken(any(User.class))).willReturn(mockToken);
        given(userCrudService.findById(anyLong())).willReturn(mockUser);

        //when
        userService.signin("1", response);

        //then
        verify(response, times(2)).setHeader(anyString(), anyString());
        verify(jwtTokenRedisRepository).save(any(JwtToken.class));
      }

    }

  }

}
