package com.alex.web.microservices.songs.lib.gateway;

import com.alex.web.microservices.songs.lib.util.HeaderUtil;
import io.opentelemetry.api.trace.Span;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Configuration
@Slf4j
public class PostFilter {

    @Bean
    public GlobalFilter postGlobalFilter() {
        return (exchange, chain) -> {
            return chain.filter(exchange).then(Mono.fromRunnable(() ->
            {
                HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
                String tracingIdManual = HeaderUtil.getTracingId(requestHeaders);
                Span span =Span.current();
                String traceIdMicrometer=span.getSpanContext().getTraceId();
                exchange.getResponse().getHeaders().add(HeaderUtil.TRACING_ID, traceIdMicrometer);
                log.info("The tracing id is: {} set in response from URI: {}", traceIdMicrometer, exchange.getRequest().getURI());

            }));
        };
    }
}
