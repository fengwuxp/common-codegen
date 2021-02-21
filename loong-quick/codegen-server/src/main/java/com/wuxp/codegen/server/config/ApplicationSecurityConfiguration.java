package com.wuxp.codegen.server.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;


/**
 * @author wxup
 */
@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {



    @Override
    protected void configure(HttpSecurity http) throws Exception {


    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        auth.inMemoryAuthentication().withUser(
                User.withUsername("test")
                .password("test")
                .authorities(Collections.emptyList())
                .build());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //静态资源不拦截
        web.ignoring().antMatchers(
                "/js/**",
                "/css/**",
                "/images/**",
                "/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/configuration/ui",
                "/swagger-resources",
                "/swagger-ui/**",
                "/webjars/**"
        );
    }


}
