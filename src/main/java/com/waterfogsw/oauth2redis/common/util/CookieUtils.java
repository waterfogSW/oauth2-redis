package com.waterfogsw.oauth2redis.common.util;

import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.SerializationUtils;

public class CookieUtils {

  public static Optional<Cookie> getCookie(
      HttpServletRequest request,
      String name
  ) {
    Cookie[] cookies = request.getCookies();

    if (cookies != null && cookies.length > 0) {
      return Arrays
          .stream(cookies)
          .filter(cookie -> cookie.getName().equals(name))
          .findFirst();
    }

    return Optional.empty();
  }

  public static Cookie addCookie(
      HttpServletResponse response,
      String name,
      String value,
      int maxAge
  ) {
    Cookie cookie = new Cookie(name, value);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setMaxAge(maxAge);
    response.addCookie(cookie);
    return cookie;
  }

  public static String serialize(Object object) {
    return Base64
        .getUrlEncoder()
        .encodeToString(SerializationUtils.serialize(object));
  }

}
