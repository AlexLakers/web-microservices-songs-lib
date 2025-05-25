package com.alex.web.microservices.songs.lib.author.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HeaderUtil {

    public final static String AUTHORIZATION = "Authorization";
    public final static String TRACING_ID = "Tracing-Id";

    public static ServerWebExchange setRequestHeader(ServerWebExchange exchange, String key, String value) {
        return exchange.mutate()
                .request(
                        exchange.getRequest().mutate()
                                .header(key, value)
                                .build())
                .build();
    }
    public static String getRequestHeader(ServerWebExchange exchange, String key) {
        return exchange.getRequest().getHeaders().getFirst(key);
    }

    public static String getAuthToken(HttpHeaders requestHeaders){
       return requestHeaders.getFirst(AUTHORIZATION);
    }
    public static String getTracingId(HttpHeaders requestHeaders){
        return requestHeaders.getFirst(TRACING_ID);
    }
    public static boolean isTracingIdPresent(HttpHeaders requestHeaders){
        return requestHeaders.getFirst(TRACING_ID)!=null;
    }
    public static boolean isAuthorizationPresent(HttpHeaders requestHeaders){
        return requestHeaders.getFirst(AUTHORIZATION)!=null;
    }

}
