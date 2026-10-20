package io.fireflysoftware.experience.cards.infra.factories;

import io.fireflysoftware.domain.banking.cards.sdk.api.CardsApi;
import io.fireflysoftware.domain.banking.cards.sdk.invoker.ApiClient;
import io.fireflysoftware.experience.cards.infra.properties.DomainBankingCardsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DomainBankingCardsClientFactory {

    private final ApiClient apiClient;

    public DomainBankingCardsClientFactory(DomainBankingCardsProperties properties) {
        this.apiClient = new ApiClient();
        this.apiClient.setBasePath(properties.getBasePath());
    }

    @Bean
    public CardsApi cardsApi() {
        return new CardsApi(apiClient);
    }
}
