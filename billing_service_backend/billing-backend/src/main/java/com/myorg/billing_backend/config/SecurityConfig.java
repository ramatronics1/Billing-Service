package com.myorg.billing_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Development-only security config that disables authentication and CSRF.
 * Use this while you build APIs. Replace with a proper security config later.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // disable CSRF (safe for local dev; DO NOT do this in production)
                .csrf(csrf -> csrf.disable())

                // allow every request without authentication
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )

                // disable form login and http basic (not needed if permitAll, but keep it explicit)
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable());

        return http.build();
    }
}
