package com.wipro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.wipro.security.RoleHeaderFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public static final String[] SWAGGER_WHITELIST = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "swagger-resources"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(configurer ->
            configurer
            .requestMatchers(SWAGGER_WHITELIST).permitAll()
            .requestMatchers("/entry/clock-in").hasAnyRole("MANAGER","EMPLOYEE")
            .requestMatchers("/entry/clock-out").hasAnyRole("MANAGER","EMPLOYEE")
            .requestMatchers("/entry/report").hasRole("MANAGER")
            .anyRequest().authenticated()
        )
        .addFilterBefore(new RoleHeaderFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}