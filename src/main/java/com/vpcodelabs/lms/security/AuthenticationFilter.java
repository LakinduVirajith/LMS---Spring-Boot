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
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {
    private final TokenValidator tokenValidator;

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractToken(request);

        if (token != null && tokenValidator.validateToken(token)) {

            // Extract user info from the token
            String userId = tokenValidator.extractUserId(token);
            String email = tokenValidator.extractEmail(token);
            String firstName = tokenValidator.extractFirstName(token);
            String lastName = tokenValidator.extractLastName(token);

            // Extract roles and convert to Spring Security authorities
            List<String> roles = tokenValidator.extractRoles(token);
            List<GrantedAuthority> authorities = roles != null ?
                    roles.stream()
                         .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                         .collect(Collectors.toList()) :
                    List.of();

            // Create UserPrincipal with authorities
            UserPrincipal userPrincipal = new UserPrincipal(userId, email, firstName, lastName, authorities);

            // Create Authentication and set context
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userPrincipal, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.debug("Authentication set for user: {} with roles: {}", email, authorities);
        }
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