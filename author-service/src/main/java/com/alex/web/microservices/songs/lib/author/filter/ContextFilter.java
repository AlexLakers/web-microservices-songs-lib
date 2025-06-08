package com.alex.web.microservices.songs.lib.author.filter;

import com.alex.web.microservices.songs.lib.util.HeaderUtil;
import com.alex.web.microservices.songs.lib.util.context.UserContextHolder;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.io.IOException;

/**
 * This class as a filter for pre-handling all the request for this service.
 * It allows get traceId from request and save it in own context.
 * But it is not used because
 */
@Slf4j
@Controller
public class ContextFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String tracingId=((HttpServletRequest)servletRequest).getHeader(HeaderUtil.TRACING_ID);
        UserContextHolder.getUserContext().setTracingId(tracingId);
        log.info("The tracing id: {} is set in Author-service request context",tracingId);
        //or you can use SecurityContextHolder.getContext().getAuthentication()-JwtAutToken.getAccessToken() in interceptor for restTemplate/feign
       String authorizationToken= (((HttpServletRequest)servletRequest).getHeader(HeaderUtil.AUTHORIZATION));
        UserContextHolder.getUserContext().setAuthorizationToken(authorizationToken);
        log.info("The authorization token : {} is set in Author-service request context",authorizationToken);

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
