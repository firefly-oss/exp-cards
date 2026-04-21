package com.firefly.experience.cards.infra.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Configuration properties for the Domain Banking Cards API.
 * <p>
 * Binds to {@code api-configuration.domain-platform.banking-cards} in application.yaml.
 */
@Component
@ConfigurationProperties(prefix = "api-configuration.domain-platform.banking-cards")
@Data
public class DomainBankingCardsProperties {

    /** Base URL of the Domain Banking Cards service (e.g. {@code http://localhost:18080}). */
    private String basePath;

    /** Read/connect timeout for SDK calls. Defaults to 5 seconds. */
    private Duration timeout = Duration.ofSeconds(5);
}
