package com.alex.web.microservices.songs.lib.songs.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;

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
                                /*.requestMatchers("/api/v1/songs/**").hasAnyAuthority("ADMIN","USER")*/
                                .anyRequest().permitAll()
                )
                .oauth2ResourceServer(oauth->oauth.jwt(jwt-> jwt.jwtAuthenticationConverter(keycloakJwtTokenConverter))).build();
    }
}
