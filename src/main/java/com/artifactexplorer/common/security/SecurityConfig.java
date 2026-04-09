package com.artifactexplorer.common.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

// common/security/SecurityConfig.java
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtFilter jwtFilter;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .sessionManagement(s -> s.sessionCreationPolicy(STATELESS))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/api/admin/auth/**").permitAll()

                                                // public reads
                                                .requestMatchers(HttpMethod.GET,
                                                                "/api/museums/**", "/api/artifacts/**",
                                                                "/api/artifact-types/**", "/api/deities/**",
                                                                "/api/dynasties/**", "/api/regions/**",
                                                                "/api/exhibits/**")
                                                .permitAll()
                                                .requestMatchers("/api/admin/users/**").hasRole("ADMIN")
                                                .requestMatchers("/api/admin/roles/**").hasRole("ADMIN")
                                                .requestMatchers("/api/admin/logs/**").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.POST,
                                                                "/api/artifacts/**", "/api/exhibits/**",
                                                                "/api/museums/**", "/api/dynasties/**",
                                                                "/api/regions/**", "/api/deities/**")
                                                .hasAnyRole("ADMIN", "CURATOR")
                                                .requestMatchers(HttpMethod.PUT,
                                                                "/api/artifacts/**", "/api/exhibits/**",
                                                                "/api/museums/**", "/api/dynasties/**",
                                                                "/api/regions/**", "/api/deities/**")
                                                .hasAnyRole("ADMIN", "CURATOR")
                                                .requestMatchers(HttpMethod.PATCH,
                                                                "/api/artifacts/**", "/api/exhibits/**")
                                                .hasAnyRole("ADMIN", "CURATOR")
                                                .requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")
                                                .anyRequest().authenticated())
                                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}