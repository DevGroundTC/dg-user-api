package com.example.dguserapi.config;

import com.example.dguserapi.filter.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and().csrf(AbstractHttpConfigurer::disable);

        httpSecurity.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        httpSecurity.exceptionHandling()
                .authenticationEntryPoint(
                        ((request, response, authException) -> {
                            response.sendError(
                                    HttpServletResponse.SC_UNAUTHORIZED,
                                    authException.getMessage()
                            );
                        })
                )
                .and();
        httpSecurity.authorizeRequests()
                .antMatchers("/auth/login", "/auth/register").permitAll()
                .anyRequest().authenticated();

        httpSecurity.addFilterBefore(
                jwtTokenFilter, UsernamePasswordAuthenticationFilter.class
        );

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
