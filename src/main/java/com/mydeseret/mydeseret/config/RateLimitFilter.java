package com.mydeseret.mydeseret.config;

import com.mydeseret.mydeseret.service.RateLimitingService;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1) // This basicall just runs this early in the chain
public class RateLimitFilter implements Filter {

    @Autowired
    private RateLimitingService rateLimitingService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Identify the User/Tenant
        // Use the Tenant ID or User Email from the SecurityContext
        // If not logged in, we can use IP Address
        String key = getRateLimitKey(httpRequest);

        // Special check for AI endpoints (Expensive)
        Bucket bucket;
        if (httpRequest.getRequestURI().startsWith("/api/v1/ai")) {
            bucket = rateLimitingService.resolveAiBucket(key);
        } else {
            bucket = rateLimitingService.resolveBucket(key);
        }

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            // Success! Adds header showing remaining tokens
            httpResponse.setHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            chain.doFilter(request, response);
        } else {
            // Failure! 429 Too Many Requests
            httpResponse.setStatus(429); // HTTP 429 Too Many Requests
            httpResponse.setHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(probe.getNanosToWaitForRefill() / 1_000_000_000));
            httpResponse.getWriter().write("Too many requests. Please try again later.");
        }
    }

    private String getRateLimitKey(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            // Logged in: Limit by Username/Email
            return auth.getName();
        } else {
            // Anonymous: Limit by IP Address
            return request.getRemoteAddr();
        }
    }
}