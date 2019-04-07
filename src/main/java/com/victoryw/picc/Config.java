package com.victoryw.picc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

// tag::class[]
@Configuration
@EnableRedisHttpSession // <1>
public class Config {
}
