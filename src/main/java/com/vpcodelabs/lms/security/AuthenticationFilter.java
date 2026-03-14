package com.vpcodelabs.lms.security;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {
    private final TokenValidator tokenValidator;

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull FilterChain filterChain)
            throws ServletException, IOException {

                log.error("1");
        String token = extractToken(request);

        log.error("2");
        if (token != null && tokenValidator.validateToken(token)) {
            log.error("3");

            String userId = tokenValidator.extractUserId(token);
            log.error("4");
            String email = tokenValidator.extractEmail(token);
            log.error("5");
            String firstName = tokenValidator.extractFirstName(token);
            log.error("6");
            String lastName = tokenValidator.extractLastName(token);

            log.error("7");
            UserPrincipal userPrincipal = new UserPrincipal(userId, email, firstName, lastName);

            log.error("8");
            List<String> roles = tokenValidator.extractRoles(token);
            log.error("9");
            List<GrantedAuthority> authorities = roles != null ?
                    roles.stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                            .collect(Collectors.toList()) :
                    new ArrayList<>();
                    log.error("10");

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userPrincipal, null, authorities);
                    log.error("11");
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.error("12");
        }

        log.error("13");
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}