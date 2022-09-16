package com.waterfogsw.oauth2redis.user.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.validator.constraints.Length;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.lang.Assert;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Length(min = 1, max = 200)
  private String username;

  private String profileImgUrl;

  @Enumerated(EnumType.STRING)
  private Role role;

  @Enumerated(EnumType.STRING)
  private Provider provider;

  private String providerId;

  @Builder
  public User(
      String username,
      String profileImgUrl,
      Role role
  ) {
    Assert.hasText(username, "Username must be provided");
    Assert.notNull(role, "Role must be provided");

    this.username = username;
    this.profileImgUrl = profileImgUrl;
    this.role = role;
  }

  public Claims getClaims() {
    String sub = String.valueOf(getId());
    Claims claims = Jwts
        .claims()
        .setSubject(sub);

    claims.put("roles", getRole());
    return claims;
  }

  public static User createDefaultUser(
      String username,
      String profileImgUrl
  ) {
    return User
        .builder()
        .username(username)
        .profileImgUrl(profileImgUrl)
        .role(Role.USER)
        .build();
  }

}
