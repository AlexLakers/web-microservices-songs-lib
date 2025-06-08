package com.alex.web.microservices.songs.lib.songs.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "pagination.default")
@Component
@Getter
@Setter
public class PaginationConfig {
    private Integer page;
    private Integer size;
    private String column;
    private String direction;
}
