package com.waterfogsw.oauth2redis.common.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.waterfogsw.oauth2redis.common.properties.JwtProperties;

@Configuration
@EnableConfigurationProperties({
    JwtProperties.class,
})
public class PropertiesConfig {

}
