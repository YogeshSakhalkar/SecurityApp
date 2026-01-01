package com.example.SecurityApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        /*
        //any request will come must be authenticate
                httpSecurity.authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults());

         */

        //will authenticate all the request which have /post/**
        //only haveing admin role can access /post/**
        httpSecurity.authorizeHttpRequests(auth -> auth
                .requestMatchers("/post/**").permitAll()
                        .requestMatchers("/post/**").hasAnyRole("ADMIN")
                .anyRequest().authenticated())
                .csrf(csrfConfig ->csrfConfig.disable())
                .sessionManagement(SessionConfig -> SessionConfig
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(Customizer.withDefaults());

        return httpSecurity.build();
    }

    @Bean
    UserDetailsService myInMemoryUserDetailService(){
        UserDetails normalUser = User
                .withUsername("Yogesh")
                .password(passwordEncoder().encode("yogesh123"))
                .roles("user").build();
        
        UserDetails adminUser = User
                .withUsername("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("admin").build();

        return new InMemoryUserDetailsManager(normalUser,adminUser);
    }


    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
