package org.nico.quotedserver.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class AuthConfig {

    @Value("${security.enable-csrf}")
    private boolean csrfEnabled;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http.authorizeHttpRequests((authz) -> authz
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());

        // CSRF in general not required for stateless REST APIs that use POST, PUT, DELETE and/or PATCH
        if (!csrfEnabled)
            http.csrf().disable();

        return http.build();
    }
}