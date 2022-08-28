package com.waterfogsw.oauth2redis.common.config;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.waterfogsw.oauth2redis.common.properties.RedisProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import redis.embedded.RedisServer;

@Slf4j
@Profile("local")
@Configuration
@RequiredArgsConstructor
public class RedisEmbeddedConfig {

  private final RedisProperties redisProperties;
  private RedisServer redisServer;

  @PostConstruct
  public void redisServerStart() {
    redisServer = new RedisServer(redisProperties.getPort());
    redisServer.start();
    log.info("Embedded Redis start");
  }

  @PreDestroy
  public void redisServerStop() {
    if (redisServer != null) {
      redisServer.stop();
    }
  }

}
