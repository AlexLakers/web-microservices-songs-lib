package com.alex.web.microservices.songs.lib.frontend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * This class as a config for security of this service.
 * It contains the main bean 'securityFilterChain' for create security-policy.
 */
@Configuration
@EnableMethodSecurity(jsr250Enabled = true,securedEnabled = true,prePostEnabled = true)
public class SecurityConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/dashboard/services/authors/**","/logout").authenticated()
                                .requestMatchers("login/oauth2/code/keycloak").permitAll()
                                .requestMatchers("/**").permitAll()
                                .anyRequest().denyAll()
                )
               .logout(logout->logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .addLogoutHandler(keycloakLogoutHandler())
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                )
                .oauth2Login(auth->auth
                        .defaultSuccessUrl("/dashboard",true)
                                .loginPage("/oauth2/authorization/keycloak")
                        ).build();

    }

    @Bean
    public LogoutHandler keycloakLogoutHandler(){
        return (req,resp,auth)->{
                    //Use if available toke_id
           // ((OAuth2User)auth.getPrincipal()).getAttribute("id_token");
               String logoutUrl = "http://keycloak:8080/realms/alexlakers-realm/protocol/openid-connect/logout?client_id=songs-lib&post_logout_redirect_uri="
                           + URLEncoder.encode("http://localhost:8089", StandardCharsets.UTF_8);
                try {
                resp.sendRedirect(logoutUrl);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
