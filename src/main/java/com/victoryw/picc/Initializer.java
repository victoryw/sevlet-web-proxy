package com.victoryw.picc;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

// tag::class[]
public class Initializer
        extends AbstractHttpSessionApplicationInitializer { // <1>

    public Initializer() {
        super(Config.class); // <2>
    }
}
// end::class[]

// end::class[]