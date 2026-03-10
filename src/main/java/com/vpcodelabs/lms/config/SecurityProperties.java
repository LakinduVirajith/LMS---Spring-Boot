package com.vpcodelabs.lms.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.security")
public class SecurityProperties {

    private List<String> whitelist;
    
    private List<String> publicEndpoints;
}
