package com.paul.supplychain.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditConfig {

    /**
     * Returns the current actor for @CreatedBy / @LastModifiedBy.
     * Replace "system" with the authenticated username once Spring Security is added:
     *
     *   SecurityContextHolder.getContext().getAuthentication().getName()
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.of("system");
    }
}