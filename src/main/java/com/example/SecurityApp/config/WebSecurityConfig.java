package com.example.SecurityApp.config;

import com.example.SecurityApp.JwtAuthFilter.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public WebSecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        /*
        //any request will come must be authenticate
                httpSecurity.authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults());

         */

        //will authenticate all the request which have /post/**
        //only having admin role can access /post/**
        httpSecurity.authorizeHttpRequests(auth -> auth
                .requestMatchers("/post/**","/auth/**").permitAll()
                      // .requestMatchers("/post/**","/auth/**").hasAnyRole("ADMIN")
                .anyRequest().authenticated())
                .csrf(csrfConfig ->csrfConfig.disable())
                .sessionManagement(SessionConfig -> SessionConfig
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
               // .formLogin(Customizer.withDefaults());

        return httpSecurity.build();
    }

   /* @Bean
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
*/



    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)throws Exception{
        return configuration.getAuthenticationManager();
    }
}
