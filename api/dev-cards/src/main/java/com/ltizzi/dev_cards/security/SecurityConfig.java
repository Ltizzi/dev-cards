package com.ltizzi.dev_cards.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author Leonardo Terlizzi
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(req->req
                .requestMatchers(HttpMethod.GET, "/task/all", "/task/byId").permitAll()
                .requestMatchers(HttpMethod.POST, "/task/new").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/task/delete").permitAll()
                .requestMatchers(HttpMethod.PATCH, "/task/update").permitAll()
                .anyRequest().permitAll()

        ).csrf().disable().httpBasic(Customizer.withDefaults());
        return http.build();
    }

}
