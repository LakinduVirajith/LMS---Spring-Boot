package com.vpcodelabs.lms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.vpcodelabs.lms.security.ClerkValidator;
import com.vpcodelabs.lms.security.JwtValidator;
import com.vpcodelabs.lms.security.TokenValidator;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ValidatorConfiguration {

    @Bean
    @ConditionalOnProperty(name = "auth.validator.type", havingValue = "jwt")
    public TokenValidator jwtTokenValidator(@Value("${jwt.secret}") String jwtSecret) {
        log.info("JWT validator configured as primary TokenValidator");
        return new JwtValidator(jwtSecret);
    }

    @Bean
    @ConditionalOnProperty(name = "auth.validator.type", havingValue = "clerk", matchIfMissing = true)
    public TokenValidator clerkTokenValidator(@Value("${clerk.jwks.url}") String clerkJwksUrl) {
        log.info("Clerk validator configured as primary TokenValidator");
        return new ClerkValidator(clerkJwksUrl);
    }
}
