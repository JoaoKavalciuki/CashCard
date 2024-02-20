package com.cashcard.cashcard.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;


@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcRequestBuilder = new MvcRequestMatcher.Builder(introspector);
        http.csrf(AbstractHttpConfigurer::disable);
        return http
                .authorizeHttpRequests(request -> request
                        .requestMatchers(mvcRequestBuilder.pattern("/cashcards/**"))
                        .hasRole("CARD-OWNER")
                        )
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService testsUsers(){
        User.UserBuilder users = User.builder();
        UserDetails jason = users
                .username("Jason")
                .password(passwordEncoder().encode("12345"))
                .roles("CARD-OWNER")
                .build();

        UserDetails jose = users
                .username("Jose")
                .password(passwordEncoder().encode("RBAC"))
                .roles("NON-CARD-OWNER").build();

        UserDetails henry = users
                .username("Henry")
                .password(passwordEncoder().encode("1127"))
                .roles("CARD-OWNER").build();



        return new InMemoryUserDetailsManager(jason, jose, henry);
    }
}
