package com.waterfogsw.oauth2redis.user.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.waterfogsw.oauth2redis.common.config.WebSecurityConfig;
import com.waterfogsw.oauth2redis.common.jwt.JwtTokenProvider;
import com.waterfogsw.oauth2redis.user.service.UserService;

@Import({WebSecurityConfig.class})
@MockBeans(
    @MockBean(JwtTokenProvider.class)
)
@WebMvcTest(controllers = UserApiController.class)
@AutoConfigureRestDocs
class UserApiControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  UserService userService;

  @Nested
  @DisplayName("/signup uri는")
  class DescribeSignupUri {

    String requestURI = "/api/v1/users/signup";

    @Nested
    @DisplayName("유효한 값이 전달되면")
    class ContextWith {

      @Test
      @DisplayName("Created 응답을 반환한다")
      void ItResponseCreated() throws Exception {
        //given
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("username", "test");
        requestMap.put("profileImgUrl", "testUrl");

        final var requestBody = objectMapper.writeValueAsString(requestMap);

        //when
        final var request = MockMvcRequestBuilders
            .post(requestURI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody);

        final var response = mockMvc.perform(request);

        //then
        response
            .andExpect(status().isCreated())
            .andDo(
                document(
                    "Create report to user",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("username")
                            .type(JsonFieldType.STRING)
                            .description("유저 이름"),
                        fieldWithPath("profileImgUrl")
                            .type(JsonFieldType.STRING)
                            .description("프로필 이미지 URL")
                    )
                )
            );
      }

    }

    @Nested
    @DisplayName("username이 빈값이 전달되면")
    class ContextWithUsernameBlank {

      @ParameterizedTest
      @NullAndEmptySource
      @DisplayName("BadRequest를 응답한다")
      void ItResponseBadRequest(String src) throws Exception {
        //given
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("username", src);
        requestMap.put("profileImgUrl", "testUrl");

        final var requestBody = objectMapper.writeValueAsString(requestMap);

        //when
        final var request = MockMvcRequestBuilders
            .post(requestURI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody);

        final var response = mockMvc.perform(request);

        //then
        response.andExpect(status().isBadRequest());
      }

    }

    @Nested
    @DisplayName("profileImgUrl 가 빈값이 전달되면")
    class ContextWithProfileImgUrl {

      @ParameterizedTest
      @NullAndEmptySource
      @DisplayName("BadRequest를 응답한다")
      void ItResponseBadRequest(String src) throws Exception {
        //given
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("username", "test");
        requestMap.put("profileImgUrl", src);

        final var requestBody = objectMapper.writeValueAsString(requestMap);

        //when
        final var request = MockMvcRequestBuilders
            .post(requestURI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody);

        final var response = mockMvc.perform(request);

        //then
        response.andExpect(status().isBadRequest());
      }

    }

  }

}
