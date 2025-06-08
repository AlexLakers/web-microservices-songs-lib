package com.alex.web.microservices.songs.lib.author.config.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;


@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "token")
public class TokenProperties {
    private String resourceId;
    private String principalAttribute;

    public Optional<String> getPrincipalAttribute() {
        return Optional.ofNullable(principalAttribute);
    }
}
