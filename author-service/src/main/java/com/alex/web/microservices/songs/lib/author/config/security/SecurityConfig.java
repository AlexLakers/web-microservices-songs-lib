package com.alex.web.microservices.songs.lib.author.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * This class as a config for security of this service.
 * It contains the main bean 'securityFilterChain' for create security-policy.
 */
@Configuration
public class SecurityConfig {
    private final KeycloakJwtTokenConverter keycloakJwtTokenConverter;

    public SecurityConfig(TokenProperties properties) {
        this.keycloakJwtTokenConverter = new KeycloakJwtTokenConverter( properties);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        return http.csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(auth->
                        auth.requestMatchers("/v3/api-docs/**","/swagger-ui/**").permitAll()
                                .requestMatchers("/api/v1/authors/**").hasAnyAuthority("ADMIN","USER")
                                .anyRequest().denyAll()
                )
                .oauth2ResourceServer(oauth->oauth.jwt(jwt-> jwt.jwtAuthenticationConverter(keycloakJwtTokenConverter))).build();
    }
}
