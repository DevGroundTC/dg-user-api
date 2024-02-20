package com.example.dguserapi.filter;

import com.example.dguserapi.database.repository.PersonRepository;
import com.example.dguserapi.exception.JwtTokenMalformedException;
import com.example.dguserapi.exception.JwtTokenMissingException;
import com.example.dguserapi.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNullApi;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.apache.logging.log4j.util.Strings.isEmpty;

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
        if (isEmpty(header) || !header.startsWith("Bearer ")) {
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

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails == null ?
                        List.of() : userDetails.getAuthorities()
                );

        authenticationToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
