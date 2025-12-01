package com.mydeseret.mydeseret.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.mydeseret.mydeseret.service.TokenBlackListService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired private TokenBlackListService tokenBlacklistService;

@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/api/v1/users/register").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/users/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/users/login").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/actuator/**").hasAuthority("ROLE_SUPER_ADMIN")
                .anyRequest().authenticated()
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

            .logout(logout -> logout
                .logoutUrl("/api/v1/users/logout")
                .addLogoutHandler((request, response, auth) -> {
                    String authHeader = request.getHeader("Authorization");
                    
                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        String token = authHeader.substring(7);
                        tokenBlacklistService.blacklistToken(token);
                        System.out.println("Token blacklisted successfully.");
                    }
                })
                .logoutSuccessHandler((request, response, authentication) -> {
                    SecurityContextHolder.clearContext();
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("{\"message\": \"Logged out successfully\"}");
                })
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}