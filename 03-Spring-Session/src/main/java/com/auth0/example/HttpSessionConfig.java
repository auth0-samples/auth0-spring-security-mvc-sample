package com.auth0.example;

import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

// tag::class[]
@EnableRedisHttpSession
public class HttpSessionConfig {
}
