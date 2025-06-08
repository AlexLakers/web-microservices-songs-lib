package com.alex.web.microservices.songs.lib.author.config;

import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * This class as a set-properties for pagination.
 * They map from .yaml.
 */
@ConfigurationProperties(prefix = "config")
@Component
@Getter
@Setter
public class AuthorConfig {
    private Integer defaultPage;
    private Integer defaultSize;
}
