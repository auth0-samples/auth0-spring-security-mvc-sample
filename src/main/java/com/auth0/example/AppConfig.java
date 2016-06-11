package com.auth0.example;

import com.auth0.spring.security.mvc.Auth0AuthenticationEntryPoint;
import com.auth0.spring.security.mvc.Auth0AuthenticationFilter;
import com.auth0.spring.security.mvc.Auth0AuthenticationProvider;
import com.auth0.spring.security.mvc.Auth0CORSFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;


@Configuration
@EnableWebSecurity(debug = true)
public class AppConfig extends WebSecurityConfigurerAdapter {

    @Value(value = "${auth0.clientId}")
    String clientId;

    @Value(value = "${auth0.clientSecret}")
    private String clientSecret;

    @Value(value = "${auth0.securedRoute}")
    private String securedRoute;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Bean(name = "auth0AuthenticationManager")
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public Auth0CORSFilter simpleCORSFilter() {
        return new Auth0CORSFilter();
    }

    @Bean(name = "auth0AuthenticationProvider")
    public Auth0AuthenticationProvider auth0AuthenticationProvider() {
        final Auth0AuthenticationProvider authenticationProvider = new Auth0AuthenticationProvider();
        authenticationProvider.setClientId(getClientId());
        authenticationProvider.setClientSecret(getClientSecret());
        authenticationProvider.setSecuredRoute(getSecuredRoute());
        return authenticationProvider;
    }

    @Bean(name = "auth0EntryPoint")
    public Auth0AuthenticationEntryPoint auth0AuthenticationEntryPoint() {
        return new Auth0AuthenticationEntryPoint();
    }

    @Bean(name = "auth0Filter")
    public Auth0AuthenticationFilter auth0AuthenticationFilter(Auth0AuthenticationEntryPoint entryPoint) {
        final Auth0AuthenticationFilter filter = new Auth0AuthenticationFilter();
        filter.setEntryPoint(entryPoint);
        return filter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(auth0AuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Disable CSRF for JWT usage
        http
                .csrf()
                .disable();
        // Add Auth0 Authentication Filter
        http
                .addFilterAfter(auth0AuthenticationFilter(auth0AuthenticationEntryPoint()), SecurityContextPersistenceFilter.class)
                .addFilterBefore(simpleCORSFilter(), Auth0AuthenticationFilter.class);

        // Apply the Authentication and Authorization Strategies your application endpoints require
        http
                .authorizeRequests()
                .antMatchers("/css/**", "/fonts/**", "/js/**", "/login").permitAll()
//                .antMatchers("/portal/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .antMatchers("/portal/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(getSecuredRoute()).authenticated();

        // ensure session management is enabled - we must retain session state
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
    }

    protected String getClientId() {
        return clientId;
    }

    protected String getClientSecret() {
        return clientSecret;
    }

    protected String getSecuredRoute() {
        return securedRoute;
    }

}
