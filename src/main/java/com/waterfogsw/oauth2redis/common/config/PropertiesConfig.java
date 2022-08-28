package com.waterfogsw.oauth2redis.common.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.waterfogsw.oauth2redis.common.properties.JwtProperties;
import com.waterfogsw.oauth2redis.common.properties.RedisProperties;

@Configuration
@EnableConfigurationProperties({
    JwtProperties.class,
    RedisProperties.class,
})
public class PropertiesConfig {

}
