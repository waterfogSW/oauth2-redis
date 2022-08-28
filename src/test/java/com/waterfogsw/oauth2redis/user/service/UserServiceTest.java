package com.waterfogsw.oauth2redis.user.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.waterfogsw.oauth2redis.common.exception.NotFoundException;
import com.waterfogsw.oauth2redis.user.entity.Role;
import com.waterfogsw.oauth2redis.user.entity.User;
import com.waterfogsw.oauth2redis.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  UserRepository userRepository;

  @InjectMocks
  UserService userService;

  @Nested
  @DisplayName("findById 메서드는")
  class DescribeFindById {

    @Nested
    @DisplayName("해당하는 유저가 존재하지 않으면")
    class ContextWithUserNotExists {

      @Test
      @DisplayName("NotFoundException 에러를 발생시킨다")
      void ItThrowsNotFoundException() {
        //given
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> userService.findById(1L)).isInstanceOf(NotFoundException.class);
      }

    }

    @Nested
    @DisplayName("해당하는 유저가 존재하면")
    class ContextWithUserExists {

      @Test
      @DisplayName("해당 유저를 반환한다")
      void ItReturnUser() {
        //given
        Long userId = 1L;
        User user = getUser(userId);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        //when
        User foundUser = userService.findById(userId);

        //then
        assertThat(foundUser.getId()).isEqualTo(userId);
      }

    }

  }

  private User getUser(long id) {
    User user = User
        .builder()
        .username("name")
        .role(Role.USER)
        .build();

    ReflectionTestUtils.setField(user, "id", 1L);
    return user;
  }

}
