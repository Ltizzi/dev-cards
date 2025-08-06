package com.ltizzi.dev_cards.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author Leonardo Terlizzi
 */
@Component
@Slf4j
public class RequestLoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIP = getClientIP(request);
        String method = request.getMethod();
        String endpoint = request.getRequestURI();
        String queryString = request.getQueryString();
        log.info("Incoming request: {} {} from IP: {} | Query: {}", method, endpoint, clientIP, queryString);
        //return HandlerInterceptor.super.preHandle(request, response, handler);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
       // HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
        log.info("Request completed: {} {} | Status: {}", request.getMethod(), request.getRequestURI(), response.getStatus());
    }


    private String getClientIP(HttpServletRequest req){
        String xForwardedHeader = req.getHeader("X-Forwarded-For");
        if(xForwardedHeader == null){
            return req.getRemoteAddr();
        }
        return xForwardedHeader.split(",")[0];
    }
}
