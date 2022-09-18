package com.waterfogsw.oauth2redis.user.entity;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.validator.constraints.Length;
import org.springframework.security.oauth2.core.user.OAuth2User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
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

  @Column(length = 512)
  private String profileImgUrl;

  @Enumerated(EnumType.STRING)
  private Role role;

  @Enumerated(EnumType.STRING)
  private Provider provider;

  @Column(length = 80)
  private String providerId;

  @Builder
  public User(
      String username,
      String profileImgUrl,
      Role role,
      Provider provider,
      String providerId
  ) {
    this.username = username;
    this.profileImgUrl = profileImgUrl;
    this.role = role;
    this.provider = provider;
    this.providerId = providerId;
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
      OAuth2User oAuth2User,
      Provider provider
  ) {
    Map<String, Object> attributes = oAuth2User.getAttributes();

    String providerId = oAuth2User.getName();
    String username = (String)attributes.get("name");
    String profileImage = (String)attributes.get("picture");

    return User
        .builder()
        .username(username)
        .profileImgUrl(profileImage)
        .provider(provider)
        .providerId(providerId)
        .build();
  }

}
