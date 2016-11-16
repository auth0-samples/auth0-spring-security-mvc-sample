package com.auth0.example;

import com.auth0.spring.security.mvc.Auth0Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class AppConfig extends Auth0Config {

    @Value(value = "${auth0.customLogin}")
    protected boolean customLogin;

    @Value(value = "${auth0.connection}")
    protected String connection;


    @Override
    protected void authorizeRequests(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/css/**", "/fonts/**", "/js/**", "/login").permitAll()
                .antMatchers(securedRoute).authenticated();
    }

    public boolean isCustomLogin() {
        return customLogin;
    }

    public String getConnection() {
        return connection;
    }

}
