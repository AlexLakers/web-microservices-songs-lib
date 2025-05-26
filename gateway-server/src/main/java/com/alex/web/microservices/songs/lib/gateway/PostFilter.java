package com.alex.web.microservices.songs.lib.gateway;

import com.alex.web.microservices.songs.lib.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class PostFilter {

    @Bean
    public GlobalFilter postGlobalFilter() {
        return (exchange, chain) -> {
            return chain.filter(exchange).then(Mono.fromRunnable(() ->
            {
                HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
                String tracingId = HeaderUtil.getTracingId(requestHeaders);
                exchange.getResponse().getHeaders().add(HeaderUtil.TRACING_ID, tracingId);
                log.info("The tracing id is: {} set in response from URI: {}", tracingId, exchange.getRequest().getURI());

            }));
        };
    }
}
