package com.wuxp.codegen.server.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * @author wxup
 */
@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfiguration {

    private final ObjectMapper objectMapper;

    public ApplicationSecurityConfiguration(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .formLogin(form -> form
                        .failureHandler((request, response, exception) -> handleUnAuthorized(response))
                        .successHandler((request, response, authentication) -> {
                        })
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> handleUnAuthorized(response))
                )
                .rememberMe(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    private void handleUnAuthorized(HttpServletResponse response) throws IOException {
        Map<String, String> resp = new HashMap<>();
        resp.put("message", "登录失败");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(objectMapper.writeValueAsString(resp));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        return new InMemoryUserDetailsManager(
                User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin1234"))
                        .authorities(Collections.emptyList())
                        .build()
        );
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                PathPatternRequestMatcher.withDefaults().matcher("/js/**"),
                PathPatternRequestMatcher.withDefaults().matcher("/css/**"),
                PathPatternRequestMatcher.withDefaults().matcher("/images/**"),
                PathPatternRequestMatcher.withDefaults().matcher("/error"),
                PathPatternRequestMatcher.withDefaults().matcher("/v3/api-docs"),
                PathPatternRequestMatcher.withDefaults().matcher("/v3/api-docs/**"),
                PathPatternRequestMatcher.withDefaults().matcher("/swagger-ui/**"),
                PathPatternRequestMatcher.withDefaults().matcher("/configuration/ui"),
                PathPatternRequestMatcher.withDefaults().matcher("/swagger-resources"),
                PathPatternRequestMatcher.withDefaults().matcher("/webjars/**")
        );
    }
}