package com.ltizzi.dev_cards.security;

import com.ltizzi.dev_cards.security.filter.JwtGenerationFilter;
import com.ltizzi.dev_cards.security.filter.JwtValidatorFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author Leonardo Terlizzi
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(corsCustomizer-> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(Collections.singletonList("http://localhost:5173"));
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setExposedHeaders(Arrays.asList("Authorization"));
                        config.setMaxAge(3600L);
                        return config;
                    }
                }))
                .addFilterAfter(new JwtGenerationFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new JwtValidatorFilter(), BasicAuthenticationFilter.class)

                .authorizeHttpRequests(req->req
                //USER
                .requestMatchers(HttpMethod.POST, "/user/new").permitAll()

                //TASK
                .requestMatchers(HttpMethod.GET, "/task/all", "/task/byId").permitAll()
                .requestMatchers(HttpMethod.POST, "/task/new").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/task/delete").permitAll()
                .requestMatchers(HttpMethod.PATCH, "/task/update").permitAll()
                .anyRequest().permitAll()

        ).csrf().disable().httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
