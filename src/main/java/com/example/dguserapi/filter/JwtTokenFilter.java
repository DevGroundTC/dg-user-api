package com.example.dguserapi.filter;

import com.example.dguserapi.database.repository.PersonRepository;
import com.example.dguserapi.exception.JwtTokenMalformedException;
import com.example.dguserapi.exception.JwtTokenMissingException;
import com.example.dguserapi.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final PersonRepository personRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.startsWithIgnoreCase(header, "Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        //Get jwt token and validate
        final String token = header.split(" ")[1].trim();
        try {
            jwtService.validateToken(token);
        } catch (JwtTokenMalformedException | JwtTokenMissingException ex) {
            logger.info("Token didn't pass validation");
            filterChain.doFilter(request, response);
            return;
        }

        //Get user identity and set in on the spring security context
        UserDetails userDetails = personRepository.findByLogin(jwtService.getUsername(token))
                .map(user -> new User(
                        user.getLogin(),
                        user.getPassword(),
                        Collections.singleton(user.getRole())
                )).orElse(null);


        Collection<? extends GrantedAuthority> authorities = null;
        if (userDetails != null) {
            authorities = userDetails.getAuthorities();
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        authorities
                );

        authenticationToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
