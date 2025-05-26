package com.alex.web.microservices.songs.lib.gateway;


import com.alex.web.microservices.songs.lib.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Order(1)
@Component
@Slf4j
public class PreFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
        if (HeaderUtil.isTracingIdPresent(requestHeaders)) {
            log.info("The tracing id is available: {}", HeaderUtil.getTracingId(requestHeaders));
        } else {
            String generatedTracingId = UUID.randomUUID().toString();
            log.info("The tracing id is not found and it was generated :{}", generatedTracingId);
            exchange= HeaderUtil.setRequestHeader(exchange, HeaderUtil.TRACING_ID, generatedTracingId);
        }

        return chain.filter(exchange);
    }



}
